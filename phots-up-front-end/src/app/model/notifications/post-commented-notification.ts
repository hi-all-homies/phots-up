import { Notification } from './notification';
import { Comment } from '../comment';

export class PostCommentedNotification implements Notification{
    type: string;
    receiver: string;
    comment: Comment;

    getTitle(): string {
        return `${this.comment.author.username} leave a comment:`
    }

    getContent(): string {
        return this.comment.content;
    }

    getPostId(): string {
        let id = this.comment.post.id;
        return id.toString();
    }
}