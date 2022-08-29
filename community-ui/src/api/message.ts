import request from "@/utils/request"
import { Page } from "@/types"

export function getMessage(query: Page) {
    return request({
        url: '/message',
        method: 'get',
        params: query
    })
}
