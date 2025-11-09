import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CountryService from './country.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import LanguageService from '@/entities/language/language.service';
import { type ILanguage } from '@/shared/model/language.model';
import RegionService from '@/entities/region/region.service';
import { type IRegion } from '@/shared/model/region.model';
import { Country, type ICountry } from '@/shared/model/country.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CountryUpdate',
  setup() {
    const countryService = inject('countryService', () => new CountryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const country: Ref<ICountry> = ref(new Country());

    const languageService = inject('languageService', () => new LanguageService());

    const languages: Ref<ILanguage[]> = ref([]);

    const regionService = inject('regionService', () => new RegionService());

    const regions: Ref<IRegion[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCountry = async countryId => {
      try {
        const res = await countryService().find(countryId);
        country.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.countryId) {
      retrieveCountry(route.params.countryId);
    }

    const initRelationships = () => {
      languageService()
        .retrieve()
        .then(res => {
          languages.value = res.data;
        });
      regionService()
        .retrieve()
        .then(res => {
          regions.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      area: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      nationalDay: {},
      countryCode2: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 2 }).toString(), 2),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 2 }).toString(), 2),
      },
      countryCode3: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 3 }).toString(), 3),
      },
      countryStats: {},
      languages: {},
      region: {},
    };
    const v$ = useVuelidate(validationRules, country as any);
    v$.value.$validate();

    return {
      countryService,
      alertService,
      country,
      previousState,
      isSaving,
      currentLanguage,
      languages,
      regions,
      v$,
      t$,
    };
  },
  created(): void {
    this.country.languages = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.country.id) {
        this.countryService()
          .update(this.country)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('learnMariadbApp.country.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.countryService()
          .create(this.country)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('learnMariadbApp.country.created', { param: param.id }).toString());
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
