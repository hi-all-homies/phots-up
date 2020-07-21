import { Notification } from './notification';

export class PostLikedNotification implements Notification{
    type: string;
    receiver: string;
    whoLiked: string;
    postId: string;
    postContent: string

    getTitle(): string {
        return `${this.whoLiked} just liked your post:`;
    }

    getContent(): string {
        return `"${this.postContent}"`;    
    }

    getPostId(): string {
        return this.postId;
    }
}