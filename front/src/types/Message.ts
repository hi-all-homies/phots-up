export interface Message {
    id: number | null,
    chatIdentity: string,
    created: string | null,
    sender: string,
    receiver: string,
    payload: string
}