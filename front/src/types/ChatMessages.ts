import type { Chat } from "./Chat";
import type { Message } from "./Message";

export interface ChatMessages {
    chat: Chat,
    messages: Message[]
}