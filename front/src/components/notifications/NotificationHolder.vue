<script setup lang="ts">
import NotificationSnack from './NotificationSnack.vue';
import { ws, type Subscriber } from '@/plugins/ws';
import { ref, onMounted, onUnmounted } from 'vue';
import type { Notification } from '@/types/Notification';
import { useChatStore } from '@/store/chats';
import { storeToRefs } from 'pinia';


const notifications = ref<Notification[]>([])
const { chatDialog } = storeToRefs(useChatStore())

const subscriber: Subscriber = {
    handleMessage: msg => {
        let notification: Notification = JSON.parse(msg.body)
        if (chatDialog.value && notification.type === 'NEW_MESSAGE') return
        else notifications.value.push(notification)
    }
}

onMounted(() => ws.addSubscriber(subscriber))
onUnmounted(() => ws.removeSubscriber(subscriber))

const onUnstack = (ind: number) => {
    if (ind === -1) notifications.value = []
    else notifications.value.splice(ind, 1)
}

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