import request from "@/utils/request"
import { Page } from "@/types"

export function getMessages(talkerId: number, lastUpdateTime?: number) {
    return request({
        url: `/message/${talkerId}`,
        method: 'get',
        params: {lastUpdateTime: lastUpdateTime},
    })
}

export function getChatList(lastUpdateTime?: number) {
    return request({
        url: '/message/list',
        method: 'get',
        params: {lastUpdateTime: lastUpdateTime},
    })
}
