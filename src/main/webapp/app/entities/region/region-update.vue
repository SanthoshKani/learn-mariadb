<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="learnMariadbApp.region.home.createOrEditLabel"
          data-cy="RegionCreateUpdateHeading"
          v-text="t$('learnMariadbApp.region.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="region.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="region.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.region.name')" for="region-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="region-name"
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
            <label class="form-control-label" v-text="t$('learnMariadbApp.region.continent')" for="region-continent"></label>
            <select class="form-control" id="region-continent" data-cy="continent" name="continent" v-model="region.continent">
              <option :value="null"></option>
              <option
                :value="region.continent && continentOption.id === region.continent.id ? region.continent : continentOption"
                v-for="continentOption in continents"
                :key="continentOption.id"
              >
                {{ continentOption.name }}
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
<script lang="ts" src="./region-update.component.ts"></script>
