export interface Notification {
    type: string;
    receiver: string;

    getTitle(): string;
    getContent(): string;
    getPostId(): string;
}