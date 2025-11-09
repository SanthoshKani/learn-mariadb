import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import VipDetails from './vip-details.vue';
import VipService from './vip.service';
import AlertService from '@/shared/alert/alert.service';

type VipDetailsComponentType = InstanceType<typeof VipDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const vipSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Vip Management Detail Component', () => {
    let vipServiceStub: SinonStubbedInstance<VipService>;
    let mountOptions: MountingOptions<VipDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      vipServiceStub = sinon.createStubInstance<VipService>(VipService);

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
          vipService: () => vipServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        vipServiceStub.find.resolves(vipSample);
        route = {
          params: {
            vipId: `${123}`,
          },
        };
        const wrapper = shallowMount(VipDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.vip).toMatchObject(vipSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        vipServiceStub.find.resolves(vipSample);
        const wrapper = shallowMount(VipDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
