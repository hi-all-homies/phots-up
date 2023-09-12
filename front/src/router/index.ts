import { createRouter, createWebHistory } from 'vue-router'


const routes = [
  {
    path: '/',
    component: () => import('@/views/Home.vue')
  },

  {
    path: '/login',
    component: () => import('@/views/LoginPage.vue')
  },

  {
    path: '/signup',
    component: () => import('@/views/SignUp.vue')
  },

  {
    path: '/profile/:username',
    component: () => import('@/views/AuthorProfile.vue')
  },

  {
    path: '/liked',
    component: () => import('@/views/LikedPosts.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router