<template>
  <div>
    <h2 id="page-heading" data-cy="CountryStatsHeading">
      <span v-text="t$('learnMariadbApp.countryStats.home.title')" id="country-stats-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('learnMariadbApp.countryStats.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CountryStatsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-country-stats"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('learnMariadbApp.countryStats.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && countryStats && countryStats.length === 0">
      <span v-text="t$('learnMariadbApp.countryStats.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="countryStats && countryStats.length > 0">
      <table class="table table-striped" aria-describedby="countryStats">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('year')">
              <span v-text="t$('learnMariadbApp.countryStats.year')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'year'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('population')">
              <span v-text="t$('learnMariadbApp.countryStats.population')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'population'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('gdp')">
              <span v-text="t$('learnMariadbApp.countryStats.gdp')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'gdp'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('country.name')">
              <span v-text="t$('learnMariadbApp.countryStats.country')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'country.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="countryStats in countryStats" :key="countryStats.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CountryStatsView', params: { countryStatsId: countryStats.id } }">{{
                countryStats.id
              }}</router-link>
            </td>
            <td>{{ countryStats.year }}</td>
            <td>{{ countryStats.population }}</td>
            <td>{{ countryStats.gdp }}</td>
            <td>
              <div v-if="countryStats.country">
                <router-link :to="{ name: 'CountryView', params: { countryId: countryStats.country.id } }">{{
                  countryStats.country.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CountryStatsView', params: { countryStatsId: countryStats.id } }"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                </router-link>
                <router-link
                  :to="{ name: 'CountryStatsEdit', params: { countryStatsId: countryStats.id } }"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                </router-link>
                <b-button
                  @click="prepareRemove(countryStats)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="learnMariadbApp.countryStats.delete.question"
          data-cy="countryStatsDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-countryStats-heading" v-text="t$('learnMariadbApp.countryStats.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-countryStats"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCountryStats()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="countryStats && countryStats.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./country-stats.component.ts"></script>
