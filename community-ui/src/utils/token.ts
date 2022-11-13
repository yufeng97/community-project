import { getStorage, removeStorage, setStorage } from "@/store/utils"


const TokenKey = 'Coummunity-Token'

export function getToken() {
    return getStorage(TokenKey)
}

export function setToken(token: any) {
    return setStorage(TokenKey, token)
}

export function removeToken() {
    return removeStorage(TokenKey)
}