import { getStorage, removeStorage, setStorage } from "@/store/utils"


const TokenKey = 'Coummunity-Token'

export function getToken(): string {
    return getStorage(TokenKey)
}

export function setToken(token: any): void {
    return setStorage(TokenKey, token)
}

export function removeToken(): void {
    return removeStorage(TokenKey)
}