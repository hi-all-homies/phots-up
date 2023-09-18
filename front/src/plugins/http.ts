import axios from "axios";

function getBaseUrl(): string {
    return import.meta.env.DEV ? 'http://localhost:8080/' : '/'
}

export const http = axios.create({
    baseURL: getBaseUrl(),
    withCredentials: true,
    headers: {
        common: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    },
    timeout: 10000,
    timeoutErrorMessage: 'failed get a response'
})