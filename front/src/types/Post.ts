import { Author } from "./Author";
import { Comment } from "./Comment";

export interface Post {
    id: number,
    content: string,
    imageUrl: string,
    created: string,
    author: Author,
    likes: Author[],
    comments: Comment[]
}