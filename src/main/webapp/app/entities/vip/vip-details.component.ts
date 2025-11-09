import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import VipService from './vip.service';
import { type IVip } from '@/shared/model/vip.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'VipDetails',
  setup() {
    const vipService = inject('vipService', () => new VipService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const vip: Ref<IVip> = ref({});

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

    return {
      alertService,
      vip,

      previousState,
      t$: useI18n().t,
    };
  },
});
