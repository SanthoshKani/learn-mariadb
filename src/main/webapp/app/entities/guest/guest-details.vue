<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="guest">
        <h2 class="jh-entity-heading" data-cy="guestDetailsHeading">
          <span v-text="t$('learnMariadbApp.guest.detail.title')"></span> {{ guest.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('learnMariadbApp.guest.name')"></span>
          </dt>
          <dd>
            <span>{{ guest.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.guest.vip')"></span>
          </dt>
          <dd>
            <span v-for="(vip, i) in guest.vips" :key="vip.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'VipView', params: { vipId: vip.id } }">{{ vip.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.guest.event')"></span>
          </dt>
          <dd>
            <span v-for="(event, i) in guest.events" :key="event.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'EventView', params: { eventId: event.id } }">{{ event.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="guest.id" :to="{ name: 'GuestEdit', params: { guestId: guest.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./guest-details.component.ts"></script>
