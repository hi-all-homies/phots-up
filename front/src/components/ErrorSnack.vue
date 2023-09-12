<script setup lang="ts">

defineProps<{
    message: string
}>()
</script>


<template>
    <v-snackbar :timeout="2500" color="error" location="top end">
        {{ message }}
        <template v-slot:actions>
            <v-btn icon="mdi-window-close" @click="$emit('update:modelValue',false)"></v-btn>
        </template>
    </v-snackbar>
</template>