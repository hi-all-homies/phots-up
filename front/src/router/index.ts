import { useUserStore } from '@/store/user'
import { createRouter, createWebHistory } from 'vue-router'


const routes = [
  {
    path: '/',
    component: () => import('@/views/Home.vue'),
    name: 'Home'
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
  },

  {
    path: '/messages',
    component: () => import('@/views/UserChats.vue'),
    name: 'Messages'
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})


const isLoggedIn = async () => {
  const userStore = useUserStore()
  
  return userStore.isAuthenticated ? true :
    await userStore.getUser()
      .then(() => true)
      .catch(() => false)
}

const pathSet: Set<string> = new Set(['Login', 'Signup', 'Liked', 'Messages'])

router.beforeEach(async (to, from) => {
  const name = to.name?.toString()
  if (name && pathSet.has(name)){
    
    let canAccess = await isLoggedIn()

    if ((to.name === 'Login' || to.name === 'Signup') && canAccess)
      return '/'

    else if ((to.name === 'Liked' || to.name === 'Messages') && !canAccess)
      return '/login'

  }
})

export default router