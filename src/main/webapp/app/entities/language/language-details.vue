<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="language">
        <h2 class="jh-entity-heading" data-cy="languageDetailsHeading">
          <span v-text="t$('learnMariadbApp.language.detail.title')"></span> {{ language.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('learnMariadbApp.language.language')"></span>
          </dt>
          <dd>
            <span>{{ language.language }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.language.country')"></span>
          </dt>
          <dd>
            <span v-for="(country, i) in language.countries" :key="country.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'CountryView', params: { countryId: country.id } }">{{ country.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="language.id" :to="{ name: 'LanguageEdit', params: { languageId: language.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./language-details.component.ts"></script>
