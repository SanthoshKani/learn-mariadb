<template>
  <div>
    <b-dropdown-item to="/continent">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.continent')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/region">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.region')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/country">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.country')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/language">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.language')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/country-stats">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.countryStats')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/vip">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.vip')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/guest">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.guest')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/event">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.event')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
