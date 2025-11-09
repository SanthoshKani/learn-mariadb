import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CountryStatsDetails from './country-stats-details.vue';
import CountryStatsService from './country-stats.service';
import AlertService from '@/shared/alert/alert.service';

type CountryStatsDetailsComponentType = InstanceType<typeof CountryStatsDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const countryStatsSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CountryStats Management Detail Component', () => {
    let countryStatsServiceStub: SinonStubbedInstance<CountryStatsService>;
    let mountOptions: MountingOptions<CountryStatsDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      countryStatsServiceStub = sinon.createStubInstance<CountryStatsService>(CountryStatsService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          countryStatsService: () => countryStatsServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        countryStatsServiceStub.find.resolves(countryStatsSample);
        route = {
          params: {
            countryStatsId: `${123}`,
          },
        };
        const wrapper = shallowMount(CountryStatsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.countryStats).toMatchObject(countryStatsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        countryStatsServiceStub.find.resolves(countryStatsSample);
        const wrapper = shallowMount(CountryStatsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
