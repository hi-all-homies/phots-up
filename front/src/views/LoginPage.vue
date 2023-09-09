<script setup lang="ts">
import { useAuthorStore } from '@/store/author';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const validated = ref(false)
const visible = ref(false)
const loading = ref(false)

const username = ref('')
const password = ref('')

const rules = {
    required: (value: string) => !!value || 'Required field',
    usernameLength: (val: string) => val.length > 2 && val.length < 17 || 'Min length - 3, max - 16',
    passLength: (val: string) => val.length > 7 && val.length < 17 || 'Min length - 8, max - 16'
}


const authorStore = useAuthorStore()
const router = useRouter()

function login(){
    loading.value = true
    authorStore.login(username.value, password.value)
        .then(() => {
            loading.value = false
            router.push('/')})
        .catch(() => {
            loading.value = false
            //TODO - error handling
        })
}
</script>


<template>
      <v-card
        class="mx-auto mt-13 pa-12 pb-8"
        elevation="8"
        max-width="525"
        rounded="lg">
        
        <div class="text-h4 d-flex justify-end align-center">
            <v-icon class="mx-3">mdi-send</v-icon>
            PhotsUp
        </div>

        <v-form v-model="validated" validate-on="input" @submit.prevent>
        <div class="text-subtitle-1 text-medium-emphasis">Account</div>
  
        <v-text-field
            v-model="username"
            :rules="[rules.required, rules.usernameLength]"
            density="compact"
            placeholder="Username"
            prepend-inner-icon="mdi-account-check"
            variant="outlined"
            maxlenght="16">
        </v-text-field>
  
        <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
          Password
        </div>
  
        <v-text-field
            v-model="password"
            :rules="[rules.required, rules.passLength]"
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            :type="visible ? 'text' : 'password'"
            density="compact"
            placeholder="Enter your password"
            prepend-inner-icon="mdi-lock-outline"
            variant="outlined"
            @click:append-inner="visible = !visible">
        </v-text-field>
  
        <v-btn
            @click="login"
            :disabled="!validated"
            block
            class="mb-8"
            color="secondary"
            size="large"
            variant="tonal"
            :loading="loading">
            Log In
        </v-btn>
        </v-form>

        <v-card-text class="text-center">
            <v-btn
                to="/signup"
                density="compact"
                color="#039BE5"
                variant="plain"
                append-icon="mdi-chevron-right">
            Sign up now
            </v-btn>
        </v-card-text>
      </v-card>
  </template>