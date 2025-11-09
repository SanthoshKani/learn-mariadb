<template>
  <div>
    <h2 id="page-heading" data-cy="EventHeading">
      <span v-text="t$('learnMariadbApp.event.home.title')" id="event-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('learnMariadbApp.event.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'EventCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-event"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('learnMariadbApp.event.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && events && events.length === 0">
      <span v-text="t$('learnMariadbApp.event.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="events && events.length > 0">
      <table class="table table-striped" aria-describedby="events">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('learnMariadbApp.event.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('date')">
              <span v-text="t$('learnMariadbApp.event.date')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'date'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="event in events" :key="event.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EventView', params: { eventId: event.id } }">{{ event.id }}</router-link>
            </td>
            <td>{{ event.name }}</td>
            <td>{{ formatDateShort(event.date) || '' }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'EventView', params: { eventId: event.id } }"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                </router-link>
                <router-link
                  :to="{ name: 'EventEdit', params: { eventId: event.id } }"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                </router-link>
                <b-button
                  @click="prepareRemove(event)"
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
        <span id="learnMariadbApp.event.delete.question" data-cy="eventDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-event-heading" v-text="t$('learnMariadbApp.event.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-event"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeEvent()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="events && events.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./event.component.ts"></script>
