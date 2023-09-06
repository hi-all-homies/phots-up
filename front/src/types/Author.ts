export interface Author {
    id: number,
    username: string,
    avatarUrl: string,
    subscriptions: Author[],
    subscribers: Author[]
}