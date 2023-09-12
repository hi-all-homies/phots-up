import type { Author } from '@/types/Author'

export const avaUtils = {
    getInitials: (author?: Author, username?: string) => {
        let result: string | undefined
        if (author) {
            result = author.username.substring(0,2).toUpperCase()
        }
        else result = username?.substring(0,2).toUpperCase()
        return result
    },
    
    getBgColor: (author?: Author) => author?.avatarUrl ? undefined : 'success'
}