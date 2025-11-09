<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="learnMariadbApp.event.home.createOrEditLabel"
          data-cy="EventCreateUpdateHeading"
          v-text="t$('learnMariadbApp.event.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="event.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="event.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.event.name')" for="event-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="event-name"
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
            <label class="form-control-label" v-text="t$('learnMariadbApp.event.date')" for="event-date"></label>
            <div class="d-flex">
              <input
                id="event-date"
                data-cy="date"
                type="datetime-local"
                class="form-control"
                name="date"
                :class="{ valid: !v$.date.$invalid, invalid: v$.date.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.date.$model)"
                @change="updateInstantField('date', $event)"
              />
            </div>
            <div v-if="v$.date.$anyDirty && v$.date.$invalid">
              <small class="form-text text-danger" v-for="error of v$.date.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="t$('learnMariadbApp.event.vip')" for="event-vip"></label>
            <select
              class="form-control"
              id="event-vips"
              data-cy="vip"
              multiple
              name="vip"
              v-if="event.vips !== undefined"
              v-model="event.vips"
            >
              <option :value="getSelected(event.vips, vipOption, 'id')" v-for="vipOption in vips" :key="vipOption.id">
                {{ vipOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('learnMariadbApp.event.guest')" for="event-guest"></label>
            <select
              class="form-control"
              id="event-guests"
              data-cy="guest"
              multiple
              name="guest"
              v-if="event.guests !== undefined"
              v-model="event.guests"
            >
              <option :value="getSelected(event.guests, guestOption, 'id')" v-for="guestOption in guests" :key="guestOption.id">
                {{ guestOption.name }}
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
<script lang="ts" src="./event-update.component.ts"></script>
