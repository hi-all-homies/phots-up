import { useUserStore } from '@/store/user'
import { createRouter, createWebHistory } from 'vue-router'


const routes = [
  {
    path: '/',
    component: () => import('@/views/Home.vue')
  },

  {
    path: '/login',
    component: () => import('@/views/LoginPage.vue'),
    name: 'Login'
  },

  {
    path: '/signup',
    component: () => import('@/views/SignUp.vue'),
    name: 'Signup'
  },

  {
    path: '/profile/:username',
    component: () => import('@/views/AuthorProfile.vue')
  },

  {
    path: '/liked',
    component: () => import('@/views/LikedPosts.vue'),
    name: 'Liked'
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

router.beforeEach((to, from) => {
  const userStore = useUserStore()

  if ((to.name === 'Login' || to.name === 'Signup') && userStore.isAuthenticated){
    return '/'
  }

  else if (to.name === 'Liked' && !userStore.isAuthenticated){
    return '/login'
  }
})

export default router