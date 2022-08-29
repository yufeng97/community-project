import request from "@/utils/request"
import { Page } from "@/types"

export function getHomeInfoList(query: Page){
    return request({
        url: '/post/list',
        method: 'get',
        params: query
    })
}

export function getPostDetail(id: string | number, query: Page) {
    return request({
        url: `/post/${id}`,
        method: 'get',
        params: query
    })
}