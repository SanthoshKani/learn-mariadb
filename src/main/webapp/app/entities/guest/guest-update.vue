<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="learnMariadbApp.guest.home.createOrEditLabel"
          data-cy="GuestCreateUpdateHeading"
          v-text="t$('learnMariadbApp.guest.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="guest.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="guest.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.guest.name')" for="guest-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="guest-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="t$('learnMariadbApp.guest.vip')" for="guest-vip"></label>
            <select
              class="form-control"
              id="guest-vips"
              data-cy="vip"
              multiple
              name="vip"
              v-if="guest.vips !== undefined"
              v-model="guest.vips"
            >
              <option :value="getSelected(guest.vips, vipOption, 'id')" v-for="vipOption in vips" :key="vipOption.id">
                {{ vipOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('learnMariadbApp.guest.event')" for="guest-event"></label>
            <select
              class="form-control"
              id="guest-events"
              data-cy="event"
              multiple
              name="event"
              v-if="guest.events !== undefined"
              v-model="guest.events"
            >
              <option :value="getSelected(guest.events, eventOption, 'id')" v-for="eventOption in events" :key="eventOption.id">
                {{ eventOption.name }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./guest-update.component.ts"></script>
