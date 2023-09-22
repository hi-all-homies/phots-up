import { type Author } from "./Author";

export interface Notification {
    type: 'POST_LIKED' | 'USER_SUBSCRIBED' | 'USER_UNSUBSCRIBED' | 'NEW_MESSAGE',
    receiver: Author,
    causer: Author,
    reason: string
}