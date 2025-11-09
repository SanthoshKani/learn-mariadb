<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="learnMariadbApp.language.home.createOrEditLabel"
          data-cy="LanguageCreateUpdateHeading"
          v-text="t$('learnMariadbApp.language.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="language.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="language.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.language.language')" for="language-language"></label>
            <input
              type="text"
              class="form-control"
              name="language"
              id="language-language"
              data-cy="language"
              :class="{ valid: !v$.language.$invalid, invalid: v$.language.$invalid }"
              v-model="v$.language.$model"
              required
            />
            <div v-if="v$.language.$anyDirty && v$.language.$invalid">
              <small class="form-text text-danger" v-for="error of v$.language.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="t$('learnMariadbApp.language.country')" for="language-country"></label>
            <select
              class="form-control"
              id="language-countries"
              data-cy="country"
              multiple
              name="country"
              v-if="language.countries !== undefined"
              v-model="language.countries"
            >
              <option
                :value="getSelected(language.countries, countryOption, 'id')"
                v-for="countryOption in countries"
                :key="countryOption.id"
              >
                {{ countryOption.name }}
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
<script lang="ts" src="./language-update.component.ts"></script>
