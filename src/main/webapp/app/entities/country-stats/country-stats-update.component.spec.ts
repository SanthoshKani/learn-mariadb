import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CountryStatsUpdate from './country-stats-update.vue';
import CountryStatsService from './country-stats.service';
import AlertService from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';

type CountryStatsUpdateComponentType = InstanceType<typeof CountryStatsUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const countryStatsSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CountryStatsUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CountryStats Management Update Component', () => {
    let comp: CountryStatsUpdateComponentType;
    let countryStatsServiceStub: SinonStubbedInstance<CountryStatsService>;

    beforeEach(() => {
      route = {};
      countryStatsServiceStub = sinon.createStubInstance<CountryStatsService>(CountryStatsService);
      countryStatsServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          countryStatsService: () => countryStatsServiceStub,
          countryService: () =>
            sinon.createStubInstance<CountryService>(CountryService, {
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
        const wrapper = shallowMount(CountryStatsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.countryStats = countryStatsSample;
        countryStatsServiceStub.update.resolves(countryStatsSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(countryStatsServiceStub.update.calledWith(countryStatsSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        countryStatsServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CountryStatsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.countryStats = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(countryStatsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        countryStatsServiceStub.find.resolves(countryStatsSample);
        countryStatsServiceStub.retrieve.resolves([countryStatsSample]);

        // WHEN
        route = {
          params: {
            countryStatsId: `${countryStatsSample.id}`,
          },
        };
        const wrapper = shallowMount(CountryStatsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.countryStats).toMatchObject(countryStatsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        countryStatsServiceStub.find.resolves(countryStatsSample);
        const wrapper = shallowMount(CountryStatsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
