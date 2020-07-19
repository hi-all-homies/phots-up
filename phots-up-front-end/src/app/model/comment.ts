import { User } from './user';
import { Post } from './post';

export class Comment{
    id: number;
    content: string;
    author: User;
    post: Post
}