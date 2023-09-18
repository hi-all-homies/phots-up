<script setup lang="ts">
import NotificationSnack from './NotificationSnack.vue';
import { ws, type Subscriber } from '@/plugins/ws';
import { ref, onMounted, onUnmounted } from 'vue';
import type { Notification } from '@/types/Notification';


const notifications = ref([] as Notification[])

const subscriber: Subscriber = {
    handleMessage: msg => notifications.value.push(JSON.parse(msg.body))
}

onMounted(() => ws.addSubscriber(subscriber))
onUnmounted(() => ws.removeSubscriber(subscriber))

const onUnstack = (ind: number) => notifications.value.splice(ind, 1)

const calcMargin = (offset: number): any => {
    let value = (offset*75) + 'px'
    let margin = {
        'margin-top': value
    }
    return margin
}
</script>


<template>
    <NotificationSnack v-for="(item, ind) in notifications" :key="ind"
        :causer="item.causer"
        :message="item.reason"
        :snack-id="ind"
        :margin="calcMargin(ind)"
        @unstack="onUnstack"/>
</template>