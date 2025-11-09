import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ContinentUpdate from './continent-update.vue';
import ContinentService from './continent.service';
import AlertService from '@/shared/alert/alert.service';

type ContinentUpdateComponentType = InstanceType<typeof ContinentUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const continentSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ContinentUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Continent Management Update Component', () => {
    let comp: ContinentUpdateComponentType;
    let continentServiceStub: SinonStubbedInstance<ContinentService>;

    beforeEach(() => {
      route = {};
      continentServiceStub = sinon.createStubInstance<ContinentService>(ContinentService);
      continentServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          continentService: () => continentServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ContinentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.continent = continentSample;
        continentServiceStub.update.resolves(continentSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(continentServiceStub.update.calledWith(continentSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        continentServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ContinentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.continent = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(continentServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        continentServiceStub.find.resolves(continentSample);
        continentServiceStub.retrieve.resolves([continentSample]);

        // WHEN
        route = {
          params: {
            continentId: `${continentSample.id}`,
          },
        };
        const wrapper = shallowMount(ContinentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.continent).toMatchObject(continentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        continentServiceStub.find.resolves(continentSample);
        const wrapper = shallowMount(ContinentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
