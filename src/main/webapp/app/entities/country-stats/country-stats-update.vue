<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="learnMariadbApp.countryStats.home.createOrEditLabel"
          data-cy="CountryStatsCreateUpdateHeading"
          v-text="t$('learnMariadbApp.countryStats.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="countryStats.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="countryStats.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.countryStats.year')" for="country-stats-year"></label>
            <input
              type="number"
              class="form-control"
              name="year"
              id="country-stats-year"
              data-cy="year"
              :class="{ valid: !v$.year.$invalid, invalid: v$.year.$invalid }"
              v-model.number="v$.year.$model"
              required
            />
            <div v-if="v$.year.$anyDirty && v$.year.$invalid">
              <small class="form-text text-danger" v-for="error of v$.year.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.countryStats.population')" for="country-stats-population"></label>
            <input
              type="number"
              class="form-control"
              name="population"
              id="country-stats-population"
              data-cy="population"
              :class="{ valid: !v$.population.$invalid, invalid: v$.population.$invalid }"
              v-model.number="v$.population.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.countryStats.gdp')" for="country-stats-gdp"></label>
            <input
              type="number"
              class="form-control"
              name="gdp"
              id="country-stats-gdp"
              data-cy="gdp"
              :class="{ valid: !v$.gdp.$invalid, invalid: v$.gdp.$invalid }"
              v-model.number="v$.gdp.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('learnMariadbApp.countryStats.country')" for="country-stats-country"></label>
            <select class="form-control" id="country-stats-country" data-cy="country" name="country" v-model="countryStats.country">
              <option :value="null"></option>
              <option
                :value="countryStats.country && countryOption.id === countryStats.country.id ? countryStats.country : countryOption"
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
<script lang="ts" src="./country-stats-update.component.ts"></script>
