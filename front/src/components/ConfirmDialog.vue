<script setup lang="ts">

defineProps<{ eventName: string }>()
</script>


<template>
    <v-dialog width="auto" :scrim="false">
         <v-card>
            <v-card-text>Are you sure?</v-card-text>
            <v-card-actions>
               <v-btn @click="$emit('update:modelValue', false)" color="success">no</v-btn>
               <v-btn @click="$emit(eventName)" color="error">yes</v-btn>
            </v-card-actions>
         </v-card>
      </v-dialog>
</template>