<script setup lang="ts">
import { useUserStore } from '@/store/user';
import type { Message } from '@/types/Message';
import { computed } from 'vue';


const props = defineProps<{
    message: Message
}>()

const { currentUser } = useUserStore()

const getSide = computed(() => {
    let clazz = 'd-flex my-2 justify-'
    let rounded = 'pa-4 bg-success rounded-xl rounded-'

    const result = props.message.sender === currentUser?.username
    clazz += result ? 'end' : 'start'
    rounded += result ? 'te-0' : 'ts-0'

    const side = { clazz: clazz, rounded: rounded }
    return side
})
</script>


<template>
    <v-row :class="getSide.clazz">
        <div :class="getSide.rounded">

            <span>{{ message.payload }}</span>
            <p class="d-flex justify-end text-caption">
                {{ message.created?.substring(11) }}
            </p>
        </div>
    </v-row>
</template>