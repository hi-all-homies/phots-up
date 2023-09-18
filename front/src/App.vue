<script lang="ts" setup>
import { useUserStore } from '@/store/user';
import AppBar from '@/components/AppBar.vue';
import AppFooter from '@/components/AppFooter.vue';
import SideBar from '@/components/SideBar.vue';
import { onMounted } from 'vue';
import AddPost from '@/components/AddPost.vue';
import ErrorSnack from '@/components/ErrorSnack.vue';
import EditPost from '@/components/EditPost.vue';
import NotificationHolder from '@/components/notifications/NotificationHolder.vue';
import { ws } from '@/plugins/ws';


const userStore = useUserStore()

onMounted(() => {
  userStore.getUser()
    .then(() => ws.connect(userStore.currentUser?.username))
    .catch(() => ws.disconnect())
})
</script>


<template>
  <v-app>
    <AppBar/>

    <SideBar/>

    <v-main>
      <router-view />
      
      <AddPost/>

      <EditPost/>
    </v-main>
    
    <NotificationHolder/>
    <ErrorSnack/>

    <AppFooter/>
  </v-app>
</template>