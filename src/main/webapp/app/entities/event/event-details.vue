<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="event">
        <h2 class="jh-entity-heading" data-cy="eventDetailsHeading">
          <span v-text="t$('learnMariadbApp.event.detail.title')"></span> {{ event.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('learnMariadbApp.event.name')"></span>
          </dt>
          <dd>
            <span>{{ event.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.event.date')"></span>
          </dt>
          <dd>
            <span v-if="event.date">{{ formatDateLong(event.date) }}</span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.event.vip')"></span>
          </dt>
          <dd>
            <span v-for="(vip, i) in event.vips" :key="vip.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'VipView', params: { vipId: vip.id } }">{{ vip.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="t$('learnMariadbApp.event.guest')"></span>
          </dt>
          <dd>
            <span v-for="(guest, i) in event.guests" :key="guest.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'GuestView', params: { guestId: guest.id } }">{{ guest.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="event.id" :to="{ name: 'EventEdit', params: { eventId: event.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./event-details.component.ts"></script>
