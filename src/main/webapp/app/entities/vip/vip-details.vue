<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="vip">
        <h2 class="jh-entity-heading" data-cy="vipDetailsHeading">
          <span v-text="t$('learnMariadbApp.vip.detail.title')"></span> {{ vip.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('learnMariadbApp.vip.name')"></span>
          </dt>
          <dd>
            <span>{{ vip.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.vip.guest')"></span>
          </dt>
          <dd>
            <span v-for="(guest, i) in vip.guests" :key="guest.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'GuestView', params: { guestId: guest.id } }">{{ guest.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.vip.event')"></span>
          </dt>
          <dd>
            <span v-for="(event, i) in vip.events" :key="event.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'EventView', params: { eventId: event.id } }">{{ event.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="vip.id" :to="{ name: 'VipEdit', params: { vipId: vip.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./vip-details.component.ts"></script>
