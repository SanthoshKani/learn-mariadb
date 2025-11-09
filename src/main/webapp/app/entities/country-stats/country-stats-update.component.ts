import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CountryStatsService from './country-stats.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';
import { type ICountry } from '@/shared/model/country.model';
import { CountryStats, type ICountryStats } from '@/shared/model/country-stats.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CountryStatsUpdate',
  setup() {
    const countryStatsService = inject('countryStatsService', () => new CountryStatsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const countryStats: Ref<ICountryStats> = ref(new CountryStats());

    const countryService = inject('countryService', () => new CountryService());

    const countries: Ref<ICountry[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCountryStats = async countryStatsId => {
      try {
        const res = await countryStatsService().find(countryStatsId);
        countryStats.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.countryStatsId) {
      retrieveCountryStats(route.params.countryStatsId);
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
      year: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      population: {},
      gdp: {},
      country: {},
    };
    const v$ = useVuelidate(validationRules, countryStats as any);
    v$.value.$validate();

    return {
      countryStatsService,
      alertService,
      countryStats,
      previousState,
      isSaving,
      currentLanguage,
      countries,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.countryStats.id) {
        this.countryStatsService()
          .update(this.countryStats)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('learnMariadbApp.countryStats.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.countryStatsService()
          .create(this.countryStats)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('learnMariadbApp.countryStats.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
