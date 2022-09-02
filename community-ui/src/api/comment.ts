import request from "@/utils/request"
import { Page } from "@/types"
import { CommentDTO, ReplyDTO } from "@/types/dto"

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

export function addComment(data: CommentDTO) {
    return request({
        url: `/comment`,
        method: 'post',
        data: data,
    })
}

export function addReply(data: ReplyDTO) {
    return request({
        url: `/comment/reply`,
        method: 'post',
        data: data,
    })
}
