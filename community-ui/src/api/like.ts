import request from "@/utils/request"

export function postLike(id: string | number) {
    return request({
        url: `/like/post/${id}`,
        method: "post",
    })
}

export function commentLike(id: string | number) {
    return request({
        url: `/like/comment/${id}`,
        method: "post",
    })
}