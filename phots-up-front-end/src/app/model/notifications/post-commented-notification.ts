import { Notification } from './notification';

export class PostCommentedNotification implements Notification{
    type: string;
    receiver: string;
    postId: string;
    content: string;
    author: string;

    getTitle(): string {
        return `${this.author} leave a comment:`;
    }

    getContent(): string {
        return this.content;
    }

    getPostId(): string {
        return this.postId;
    }
}