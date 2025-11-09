import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CountryStatsService from './country-stats.service';
import { type ICountryStats } from '@/shared/model/country-stats.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CountryStatsDetails',
  setup() {
    const countryStatsService = inject('countryStatsService', () => new CountryStatsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const countryStats: Ref<ICountryStats> = ref({});

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

    return {
      alertService,
      countryStats,

      previousState,
      t$: useI18n().t,
    };
  },
});
