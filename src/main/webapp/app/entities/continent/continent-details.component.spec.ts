import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ContinentDetails from './continent-details.vue';
import ContinentService from './continent.service';
import AlertService from '@/shared/alert/alert.service';

type ContinentDetailsComponentType = InstanceType<typeof ContinentDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const continentSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Continent Management Detail Component', () => {
    let continentServiceStub: SinonStubbedInstance<ContinentService>;
    let mountOptions: MountingOptions<ContinentDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      continentServiceStub = sinon.createStubInstance<ContinentService>(ContinentService);

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
          continentService: () => continentServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        continentServiceStub.find.resolves(continentSample);
        route = {
          params: {
            continentId: `${123}`,
          },
        };
        const wrapper = shallowMount(ContinentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.continent).toMatchObject(continentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        continentServiceStub.find.resolves(continentSample);
        const wrapper = shallowMount(ContinentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
