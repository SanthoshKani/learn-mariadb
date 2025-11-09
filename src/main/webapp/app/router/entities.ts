import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const Continent = () => import('@/entities/continent/continent.vue');
const ContinentUpdate = () => import('@/entities/continent/continent-update.vue');
const ContinentDetails = () => import('@/entities/continent/continent-details.vue');

const Region = () => import('@/entities/region/region.vue');
const RegionUpdate = () => import('@/entities/region/region-update.vue');
const RegionDetails = () => import('@/entities/region/region-details.vue');

const Country = () => import('@/entities/country/country.vue');
const CountryUpdate = () => import('@/entities/country/country-update.vue');
const CountryDetails = () => import('@/entities/country/country-details.vue');

const Language = () => import('@/entities/language/language.vue');
const LanguageUpdate = () => import('@/entities/language/language-update.vue');
const LanguageDetails = () => import('@/entities/language/language-details.vue');

const CountryStats = () => import('@/entities/country-stats/country-stats.vue');
const CountryStatsUpdate = () => import('@/entities/country-stats/country-stats-update.vue');
const CountryStatsDetails = () => import('@/entities/country-stats/country-stats-details.vue');

const Vip = () => import('@/entities/vip/vip.vue');
const VipUpdate = () => import('@/entities/vip/vip-update.vue');
const VipDetails = () => import('@/entities/vip/vip-details.vue');

const Guest = () => import('@/entities/guest/guest.vue');
const GuestUpdate = () => import('@/entities/guest/guest-update.vue');
const GuestDetails = () => import('@/entities/guest/guest-details.vue');

const Event = () => import('@/entities/event/event.vue');
const EventUpdate = () => import('@/entities/event/event-update.vue');
const EventDetails = () => import('@/entities/event/event-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'continent',
      name: 'Continent',
      component: Continent,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'continent/new',
      name: 'ContinentCreate',
      component: ContinentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'continent/:continentId/edit',
      name: 'ContinentEdit',
      component: ContinentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'continent/:continentId/view',
      name: 'ContinentView',
      component: ContinentDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'region',
      name: 'Region',
      component: Region,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'region/new',
      name: 'RegionCreate',
      component: RegionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'region/:regionId/edit',
      name: 'RegionEdit',
      component: RegionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'region/:regionId/view',
      name: 'RegionView',
      component: RegionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country',
      name: 'Country',
      component: Country,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country/new',
      name: 'CountryCreate',
      component: CountryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country/:countryId/edit',
      name: 'CountryEdit',
      component: CountryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country/:countryId/view',
      name: 'CountryView',
      component: CountryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'language',
      name: 'Language',
      component: Language,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'language/new',
      name: 'LanguageCreate',
      component: LanguageUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'language/:languageId/edit',
      name: 'LanguageEdit',
      component: LanguageUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'language/:languageId/view',
      name: 'LanguageView',
      component: LanguageDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country-stats',
      name: 'CountryStats',
      component: CountryStats,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country-stats/new',
      name: 'CountryStatsCreate',
      component: CountryStatsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country-stats/:countryStatsId/edit',
      name: 'CountryStatsEdit',
      component: CountryStatsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country-stats/:countryStatsId/view',
      name: 'CountryStatsView',
      component: CountryStatsDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vip',
      name: 'Vip',
      component: Vip,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vip/new',
      name: 'VipCreate',
      component: VipUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vip/:vipId/edit',
      name: 'VipEdit',
      component: VipUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vip/:vipId/view',
      name: 'VipView',
      component: VipDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'guest',
      name: 'Guest',
      component: Guest,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'guest/new',
      name: 'GuestCreate',
      component: GuestUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'guest/:guestId/edit',
      name: 'GuestEdit',
      component: GuestUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'guest/:guestId/view',
      name: 'GuestView',
      component: GuestDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'event',
      name: 'Event',
      component: Event,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'event/new',
      name: 'EventCreate',
      component: EventUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'event/:eventId/edit',
      name: 'EventEdit',
      component: EventUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'event/:eventId/view',
      name: 'EventView',
      component: EventDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
