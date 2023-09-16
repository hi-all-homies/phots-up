import type { Author } from '@/types/Author'
import { ref } from 'vue'


function upCase(name: string): string {
    return name.substring(0,2).toUpperCase()
}

function getInitials(author?: Author, username?: string): string {
    let result: string
        if (author) {
            result = upCase(author.username)
        }
        else result = username ? upCase(username) : ''
        return result
}

const colors: string[] = ['success', 'info', 'warning', 'error', 'primary', 'secondary']

function getBgColor(author?: Author): string {
    if (author?.avatarUrl) return ''
        else {
            let ind = Math.floor(Math.random() * (5 - 0 + 1)) + 0;
            return colors[ind]
        }
}

export function useAvatarUtils(author?: Author, username?: string){
    
    const initials = ref('')
    const bgColor = ref('')

    initials.value = getInitials(author, username)
    bgColor.value = getBgColor(author)

    return { initials, bgColor }
}