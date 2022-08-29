import request from "@/utils/request"
import { Page } from "@/types"

export function getPostComments(postId: string | number, page: Page) {
    return request({
        url: `/comment/${postId}/list`,
        method: 'get',
        params: page,
    })
}

export function getCommentReplies(commentId: string | number, page: Page) {
    return request({
        url: `/comment/${commentId}/reply/list`,
        method: 'get',
        params: page,
    })
}