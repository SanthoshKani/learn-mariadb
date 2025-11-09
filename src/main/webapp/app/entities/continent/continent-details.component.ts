import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ContinentService from './continent.service';
import { type IContinent } from '@/shared/model/continent.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ContinentDetails',
  setup() {
    const continentService = inject('continentService', () => new ContinentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const continent: Ref<IContinent> = ref({});

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

    return {
      alertService,
      continent,

      previousState,
      t$: useI18n().t,
    };
  },
});
