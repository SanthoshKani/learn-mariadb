<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="continent">
        <h2 class="jh-entity-heading" data-cy="continentDetailsHeading">
          <span v-text="t$('learnMariadbApp.continent.detail.title')"></span> {{ continent.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('learnMariadbApp.continent.name')"></span>
          </dt>
          <dd>
            <span>{{ continent.name }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="continent.id"
          :to="{ name: 'ContinentEdit', params: { continentId: continent.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./continent-details.component.ts"></script>
