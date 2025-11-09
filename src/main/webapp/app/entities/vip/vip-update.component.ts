import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import VipService from './vip.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import GuestService from '@/entities/guest/guest.service';
import { type IGuest } from '@/shared/model/guest.model';
import EventService from '@/entities/event/event.service';
import { type IEvent } from '@/shared/model/event.model';
import { type IVip, Vip } from '@/shared/model/vip.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'VipUpdate',
  setup() {
    const vipService = inject('vipService', () => new VipService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const vip: Ref<IVip> = ref(new Vip());

    const guestService = inject('guestService', () => new GuestService());

    const guests: Ref<IGuest[]> = ref([]);

    const eventService = inject('eventService', () => new EventService());

    const events: Ref<IEvent[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveVip = async vipId => {
      try {
        const res = await vipService().find(vipId);
        vip.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.vipId) {
      retrieveVip(route.params.vipId);
    }

    const initRelationships = () => {
      guestService()
        .retrieve()
        .then(res => {
          guests.value = res.data;
        });
      eventService()
        .retrieve()
        .then(res => {
          events.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      guests: {},
      events: {},
    };
    const v$ = useVuelidate(validationRules, vip as any);
    v$.value.$validate();

    return {
      vipService,
      alertService,
      vip,
      previousState,
      isSaving,
      currentLanguage,
      guests,
      events,
      v$,
      t$,
    };
  },
  created(): void {
    this.vip.guests = [];
    this.vip.events = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.vip.id) {
        this.vipService()
          .update(this.vip)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('learnMariadbApp.vip.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.vipService()
          .create(this.vip)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('learnMariadbApp.vip.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option, pkField = 'id'): any {
      if (selectedVals) {
        return selectedVals.find(value => option[pkField] === value[pkField]) ?? option;
      }
      return option;
    },
  },
});
