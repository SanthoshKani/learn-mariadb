import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import EventService from './event.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import VipService from '@/entities/vip/vip.service';
import { type IVip } from '@/shared/model/vip.model';
import GuestService from '@/entities/guest/guest.service';
import { type IGuest } from '@/shared/model/guest.model';
import { Event, type IEvent } from '@/shared/model/event.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EventUpdate',
  setup() {
    const eventService = inject('eventService', () => new EventService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const event: Ref<IEvent> = ref(new Event());

    const vipService = inject('vipService', () => new VipService());

    const vips: Ref<IVip[]> = ref([]);

    const guestService = inject('guestService', () => new GuestService());

    const guests: Ref<IGuest[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEvent = async eventId => {
      try {
        const res = await eventService().find(eventId);
        res.date = new Date(res.date);
        event.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.eventId) {
      retrieveEvent(route.params.eventId);
    }

    const initRelationships = () => {
      vipService()
        .retrieve()
        .then(res => {
          vips.value = res.data;
        });
      guestService()
        .retrieve()
        .then(res => {
          guests.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      date: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      vips: {},
      guests: {},
    };
    const v$ = useVuelidate(validationRules, event as any);
    v$.value.$validate();

    return {
      eventService,
      alertService,
      event,
      previousState,
      isSaving,
      currentLanguage,
      vips,
      guests,
      v$,
      ...useDateFormat({ entityRef: event }),
      t$,
    };
  },
  created(): void {
    this.event.vips = [];
    this.event.guests = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.event.id) {
        this.eventService()
          .update(this.event)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('learnMariadbApp.event.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.eventService()
          .create(this.event)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('learnMariadbApp.event.created', { param: param.id }).toString());
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
