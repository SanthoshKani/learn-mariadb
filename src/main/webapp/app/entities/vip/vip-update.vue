<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="learnMariadbApp.vip.home.createOrEditLabel"
          data-cy="VipCreateUpdateHeading"
          v-text="t$('learnMariadbApp.vip.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="vip.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="vip.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.vip.name')" for="vip-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="vip-name"
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
            <label v-text="t$('learnMariadbApp.vip.guest')" for="vip-guest"></label>
            <select
              class="form-control"
              id="vip-guests"
              data-cy="guest"
              multiple
              name="guest"
              v-if="vip.guests !== undefined"
              v-model="vip.guests"
            >
              <option :value="getSelected(vip.guests, guestOption, 'id')" v-for="guestOption in guests" :key="guestOption.id">
                {{ guestOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('learnMariadbApp.vip.event')" for="vip-event"></label>
            <select
              class="form-control"
              id="vip-events"
              data-cy="event"
              multiple
              name="event"
              v-if="vip.events !== undefined"
              v-model="vip.events"
            >
              <option :value="getSelected(vip.events, eventOption, 'id')" v-for="eventOption in events" :key="eventOption.id">
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
<script lang="ts" src="./vip-update.component.ts"></script>
