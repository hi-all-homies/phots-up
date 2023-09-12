import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

export function useLoginRedirect() {

    const userStore = useUserStore()

    onMounted(() => {
        if (userStore.isAuthenticated){
            useRouter().push('/')
        }
    })
}