import { onMounted, onBeforeUnmount } from 'vue'

export function useHandleScroll(action: () => void, cleanUp: () => void) {

    const handleScroll = () => {
        const pageHeight = document.body.scrollHeight
        const scrollPoint = window.scrollY + window.innerHeight

        if (scrollPoint >= pageHeight){
            action()
        }
    }

    onMounted(() => window.addEventListener('scroll', handleScroll))
    onBeforeUnmount(() => {
        cleanUp()
        window.removeEventListener('scroll', handleScroll)
    })
}