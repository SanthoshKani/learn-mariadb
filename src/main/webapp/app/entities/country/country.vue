<template>
  <div>
    <h2 id="page-heading" data-cy="CountryHeading">
      <span v-text="t$('learnMariadbApp.country.home.title')" id="country-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('learnMariadbApp.country.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CountryCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-country"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('learnMariadbApp.country.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && countries && countries.length === 0">
      <span v-text="t$('learnMariadbApp.country.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="countries && countries.length > 0">
      <table class="table table-striped" aria-describedby="countries">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('learnMariadbApp.country.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('area')">
              <span v-text="t$('learnMariadbApp.country.area')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'area'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nationalDay')">
              <span v-text="t$('learnMariadbApp.country.nationalDay')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nationalDay'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('countryCode2')">
              <span v-text="t$('learnMariadbApp.country.countryCode2')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'countryCode2'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('countryCode3')">
              <span v-text="t$('learnMariadbApp.country.countryCode3')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'countryCode3'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('region.name')">
              <span v-text="t$('learnMariadbApp.country.region')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'region.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="country in countries" :key="country.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CountryView', params: { countryId: country.id } }">{{ country.id }}</router-link>
            </td>
            <td>{{ country.name }}</td>
            <td>{{ country.area }}</td>
            <td>{{ country.nationalDay }}</td>
            <td>{{ country.countryCode2 }}</td>
            <td>{{ country.countryCode3 }}</td>
            <td>
              <div v-if="country.region">
                <router-link :to="{ name: 'RegionView', params: { regionId: country.region.id } }">{{ country.region.name }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CountryView', params: { countryId: country.id } }"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                </router-link>
                <router-link
                  :to="{ name: 'CountryEdit', params: { countryId: country.id } }"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                </router-link>
                <b-button
                  @click="prepareRemove(country)"
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
        <span id="learnMariadbApp.country.delete.question" data-cy="countryDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-country-heading" v-text="t$('learnMariadbApp.country.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-country"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCountry()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="countries && countries.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./country.component.ts"></script>
