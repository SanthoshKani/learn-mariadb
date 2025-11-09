import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import VipUpdate from './vip-update.vue';
import VipService from './vip.service';
import AlertService from '@/shared/alert/alert.service';

import GuestService from '@/entities/guest/guest.service';
import EventService from '@/entities/event/event.service';

type VipUpdateComponentType = InstanceType<typeof VipUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const vipSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<VipUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Vip Management Update Component', () => {
    let comp: VipUpdateComponentType;
    let vipServiceStub: SinonStubbedInstance<VipService>;

    beforeEach(() => {
      route = {};
      vipServiceStub = sinon.createStubInstance<VipService>(VipService);
      vipServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          vipService: () => vipServiceStub,
          guestService: () =>
            sinon.createStubInstance<GuestService>(GuestService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          eventService: () =>
            sinon.createStubInstance<EventService>(EventService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(VipUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.vip = vipSample;
        vipServiceStub.update.resolves(vipSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(vipServiceStub.update.calledWith(vipSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        vipServiceStub.create.resolves(entity);
        const wrapper = shallowMount(VipUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.vip = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(vipServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        vipServiceStub.find.resolves(vipSample);
        vipServiceStub.retrieve.resolves([vipSample]);

        // WHEN
        route = {
          params: {
            vipId: `${vipSample.id}`,
          },
        };
        const wrapper = shallowMount(VipUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.vip).toMatchObject(vipSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        vipServiceStub.find.resolves(vipSample);
        const wrapper = shallowMount(VipUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
