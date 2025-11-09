import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import LanguageService from './language.service';
import { type ILanguage } from '@/shared/model/language.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LanguageDetails',
  setup() {
    const languageService = inject('languageService', () => new LanguageService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const language: Ref<ILanguage> = ref({});

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

    return {
      alertService,
      language,

      previousState,
      t$: useI18n().t,
    };
  },
});
