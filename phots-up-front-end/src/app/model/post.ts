import { User } from './user';
import { Comment } from './comment';

export class Post{
    id:number;
    content: string;
    imageKey: string;
    image: string;
    author: User;
    likes: User[];
    comments: Comment[];
    created: string;
}