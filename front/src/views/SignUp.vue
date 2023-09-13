<script setup lang="ts">
import { useAppStore } from '@/store/app';
import { useUserStore } from '@/store/user';
import { ref } from 'vue';
import { useRouter } from 'vue-router';


const validated = ref(false)
const visible = ref(false)
const loading = ref(false)

const username = ref('')
const password = ref('')
const confirmedPass = ref('')

const rules = {
    required: (value: string) => !!value || 'Required field',
    usernameLength: (val: string) => val.length > 2 && val.length < 17 || 'Min length - 3, max - 16',
    passLength: (val: string) => val.length > 7 && val.length < 17 || 'Min length - 8, max - 16',
    passMatch: (val: string) => val === password.value || 'Passwords must match'
}

const userStore = useUserStore()
const router = useRouter()
const appStore = useAppStore()


function signUp(){
    loading.value = true
    userStore.signUp(username.value, password.value)
        .then(() => router.push('/login'))
        .catch(() => {
            loading.value = false
            appStore.$patch({ erorrs: {
                message: 'such username already exists',
                active: true
            }})
        })
}
</script>


<template>
      <v-card
        class="mx-auto mt-13 pa-12 pb-8"
        elevation="8"
        max-width="525"
        rounded="lg">
        
        <div class="text-h4 d-flex justify-center">
            Sign up
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
  
        <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
          Confirm password
        </div>
  
        <v-text-field
            v-model="confirmedPass"
            :rules="[rules.required, rules.passMatch]"
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            :type="visible ? 'text' : 'password'"
            density="compact"
            placeholder="Confirm your password"
            prepend-inner-icon="mdi-lock-outline"
            variant="outlined"
            @click:append-inner="visible = !visible">
        </v-text-field>
  
        <v-btn
            @click="signUp"
            :disabled="!validated"
            block
            class="mb-8"
            color="secondary"
            size="large"
            variant="tonal"
            :loading="loading">
            Sign Up
        </v-btn>

        </v-form>
      </v-card>
  </template>