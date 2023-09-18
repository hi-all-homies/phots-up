import { Client, Message } from "@stomp/stompjs"
import SockJS  from 'sockjs-client/dist/sockjs.min.js';


const wsBaseUrl = (): string =>
    import.meta.env.DEV ? 'http://localhost:8080/ws' : '/ws'

const client: Client = new Client({
    webSocketFactory: () => new SockJS(wsBaseUrl()),
    reconnectDelay: 5000,
    //debug: msg => console.log(msg)
})

const subscribers: Subscriber[] = []

export const ws = {

    connect: (username?: string) => {

        client.onConnect = (frame) => {
            console.log(frame)
            const onMessage = (message: Message) =>
                subscribers.forEach(sub => sub.handleMessage(message))

            client.subscribe(`/user/${username}/que/events`, onMessage)
        }

        client.onStompError = () => client.deactivate()

        client.activate()
    },

    // TODO: 
    sendMessage: () => {},

    disconnect: () => client.deactivate(),

    addSubscriber: (sub: Subscriber) => subscribers.push(sub),

    removeSubscriber: (sub: Subscriber) => {
        let index = subscribers.findIndex(s => s === sub)
        if (index > -1){
            subscribers.splice(index, 1)
        }
    }
}


export declare interface Subscriber {
    handleMessage: (message: Message) => void
}