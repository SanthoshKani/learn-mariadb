import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import GuestService from './guest.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import VipService from '@/entities/vip/vip.service';
import { type IVip } from '@/shared/model/vip.model';
import EventService from '@/entities/event/event.service';
import { type IEvent } from '@/shared/model/event.model';
import { Guest, type IGuest } from '@/shared/model/guest.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'GuestUpdate',
  setup() {
    const guestService = inject('guestService', () => new GuestService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const guest: Ref<IGuest> = ref(new Guest());

    const vipService = inject('vipService', () => new VipService());

    const vips: Ref<IVip[]> = ref([]);

    const eventService = inject('eventService', () => new EventService());

    const events: Ref<IEvent[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveGuest = async guestId => {
      try {
        const res = await guestService().find(guestId);
        guest.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.guestId) {
      retrieveGuest(route.params.guestId);
    }

    const initRelationships = () => {
      vipService()
        .retrieve()
        .then(res => {
          vips.value = res.data;
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
      vips: {},
      events: {},
    };
    const v$ = useVuelidate(validationRules, guest as any);
    v$.value.$validate();

    return {
      guestService,
      alertService,
      guest,
      previousState,
      isSaving,
      currentLanguage,
      vips,
      events,
      v$,
      t$,
    };
  },
  created(): void {
    this.guest.vips = [];
    this.guest.events = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.guest.id) {
        this.guestService()
          .update(this.guest)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('learnMariadbApp.guest.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.guestService()
          .create(this.guest)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('learnMariadbApp.guest.created', { param: param.id }).toString());
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
