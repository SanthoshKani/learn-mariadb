import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ContinentService from './continent.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Continent, type IContinent } from '@/shared/model/continent.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ContinentUpdate',
  setup() {
    const continentService = inject('continentService', () => new ContinentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const continent: Ref<IContinent> = ref(new Continent());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveContinent = async continentId => {
      try {
        const res = await continentService().find(continentId);
        continent.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.continentId) {
      retrieveContinent(route.params.continentId);
    }

    const initRelationships = () => {};

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      regions: {},
    };
    const v$ = useVuelidate(validationRules, continent as any);
    v$.value.$validate();

    return {
      continentService,
      alertService,
      continent,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.continent.id) {
        this.continentService()
          .update(this.continent)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('learnMariadbApp.continent.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.continentService()
          .create(this.continent)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('learnMariadbApp.continent.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
