import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import LanguageService from './language.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';
import { type ICountry } from '@/shared/model/country.model';
import { type ILanguage, Language } from '@/shared/model/language.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LanguageUpdate',
  setup() {
    const languageService = inject('languageService', () => new LanguageService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const language: Ref<ILanguage> = ref(new Language());

    const countryService = inject('countryService', () => new CountryService());

    const countries: Ref<ICountry[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveLanguage = async languageId => {
      try {
        const res = await languageService().find(languageId);
        language.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.languageId) {
      retrieveLanguage(route.params.languageId);
    }

    const initRelationships = () => {
      countryService()
        .retrieve()
        .then(res => {
          countries.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      language: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      countries: {},
    };
    const v$ = useVuelidate(validationRules, language as any);
    v$.value.$validate();

    return {
      languageService,
      alertService,
      language,
      previousState,
      isSaving,
      currentLanguage,
      countries,
      v$,
      t$,
    };
  },
  created(): void {
    this.language.countries = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.language.id) {
        this.languageService()
          .update(this.language)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('learnMariadbApp.language.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.languageService()
          .create(this.language)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('learnMariadbApp.language.created', { param: param.id }).toString());
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
