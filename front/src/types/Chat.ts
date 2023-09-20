import type { Author } from "./Author";

export interface Chat {
    id: number,
    chatIdentity: string,
    sender: Author,
    receiver: Author
}