<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="country">
        <h2 class="jh-entity-heading" data-cy="countryDetailsHeading">
          <span v-text="t$('learnMariadbApp.country.detail.title')"></span> {{ country.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('learnMariadbApp.country.name')"></span>
          </dt>
          <dd>
            <span>{{ country.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.country.area')"></span>
          </dt>
          <dd>
            <span>{{ country.area }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.country.nationalDay')"></span>
          </dt>
          <dd>
            <span>{{ country.nationalDay }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.country.countryCode2')"></span>
          </dt>
          <dd>
            <span>{{ country.countryCode2 }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.country.countryCode3')"></span>
          </dt>
          <dd>
            <span>{{ country.countryCode3 }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.country.language')"></span>
          </dt>
          <dd>
            <span v-for="(language, i) in country.languages" :key="language.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'LanguageView', params: { languageId: language.id } }">{{ language.language }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.country.region')"></span>
          </dt>
          <dd>
            <div v-if="country.region">
              <router-link :to="{ name: 'RegionView', params: { regionId: country.region.id } }">{{ country.region.name }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="country.id" :to="{ name: 'CountryEdit', params: { countryId: country.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./country-details.component.ts"></script>
