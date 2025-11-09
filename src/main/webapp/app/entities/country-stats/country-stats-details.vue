<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="countryStats">
        <h2 class="jh-entity-heading" data-cy="countryStatsDetailsHeading">
          <span v-text="t$('learnMariadbApp.countryStats.detail.title')"></span> {{ countryStats.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('learnMariadbApp.countryStats.year')"></span>
          </dt>
          <dd>
            <span>{{ countryStats.year }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.countryStats.population')"></span>
          </dt>
          <dd>
            <span>{{ countryStats.population }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.countryStats.gdp')"></span>
          </dt>
          <dd>
            <span>{{ countryStats.gdp }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.countryStats.country')"></span>
          </dt>
          <dd>
            <div v-if="countryStats.country">
              <router-link :to="{ name: 'CountryView', params: { countryId: countryStats.country.id } }">{{
                countryStats.country.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="countryStats.id"
          :to="{ name: 'CountryStatsEdit', params: { countryStatsId: countryStats.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./country-stats-details.component.ts"></script>
