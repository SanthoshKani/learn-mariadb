<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="learnMariadbApp.country.home.createOrEditLabel"
          data-cy="CountryCreateUpdateHeading"
          v-text="t$('learnMariadbApp.country.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="country.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="country.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.country.name')" for="country-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="country-name"
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
            <label class="form-control-label" v-text="t$('learnMariadbApp.country.area')" for="country-area"></label>
            <input
              type="number"
              class="form-control"
              name="area"
              id="country-area"
              data-cy="area"
              :class="{ valid: !v$.area.$invalid, invalid: v$.area.$invalid }"
              v-model.number="v$.area.$model"
              required
            />
            <div v-if="v$.area.$anyDirty && v$.area.$invalid">
              <small class="form-text text-danger" v-for="error of v$.area.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.country.nationalDay')" for="country-nationalDay"></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="country-nationalDay"
                  v-model="v$.nationalDay.$model"
                  name="nationalDay"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="country-nationalDay"
                data-cy="nationalDay"
                type="text"
                class="form-control"
                name="nationalDay"
                :class="{ valid: !v$.nationalDay.$invalid, invalid: v$.nationalDay.$invalid }"
                v-model="v$.nationalDay.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.country.countryCode2')" for="country-countryCode2"></label>
            <input
              type="text"
              class="form-control"
              name="countryCode2"
              id="country-countryCode2"
              data-cy="countryCode2"
              :class="{ valid: !v$.countryCode2.$invalid, invalid: v$.countryCode2.$invalid }"
              v-model="v$.countryCode2.$model"
              required
            />
            <div v-if="v$.countryCode2.$anyDirty && v$.countryCode2.$invalid">
              <small class="form-text text-danger" v-for="error of v$.countryCode2.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.country.countryCode3')" for="country-countryCode3"></label>
            <input
              type="text"
              class="form-control"
              name="countryCode3"
              id="country-countryCode3"
              data-cy="countryCode3"
              :class="{ valid: !v$.countryCode3.$invalid, invalid: v$.countryCode3.$invalid }"
              v-model="v$.countryCode3.$model"
              required
            />
            <div v-if="v$.countryCode3.$anyDirty && v$.countryCode3.$invalid">
              <small class="form-text text-danger" v-for="error of v$.countryCode3.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="t$('learnMariadbApp.country.language')" for="country-language"></label>
            <select
              class="form-control"
              id="country-languages"
              data-cy="language"
              multiple
              name="language"
              v-if="country.languages !== undefined"
              v-model="country.languages"
            >
              <option
                :value="getSelected(country.languages, languageOption, 'id')"
                v-for="languageOption in languages"
                :key="languageOption.id"
              >
                {{ languageOption.language }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.country.region')" for="country-region"></label>
            <select class="form-control" id="country-region" data-cy="region" name="region" v-model="country.region">
              <option :value="null"></option>
              <option
                :value="country.region && regionOption.id === country.region.id ? country.region : regionOption"
                v-for="regionOption in regions"
                :key="regionOption.id"
              >
                {{ regionOption.name }}
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
<script lang="ts" src="./country-update.component.ts"></script>
