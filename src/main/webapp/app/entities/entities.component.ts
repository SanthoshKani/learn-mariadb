import { defineComponent, provide } from 'vue';

import ContinentService from './continent/continent.service';
import RegionService from './region/region.service';
import CountryService from './country/country.service';
import LanguageService from './language/language.service';
import CountryStatsService from './country-stats/country-stats.service';
import VipService from './vip/vip.service';
import GuestService from './guest/guest.service';
import EventService from './event/event.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('continentService', () => new ContinentService());
    provide('regionService', () => new RegionService());
    provide('countryService', () => new CountryService());
    provide('languageService', () => new LanguageService());
    provide('countryStatsService', () => new CountryStatsService());
    provide('vipService', () => new VipService());
    provide('guestService', () => new GuestService());
    provide('eventService', () => new EventService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
