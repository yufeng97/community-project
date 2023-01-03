import request from "@/utils/request"


export function getUserById(id: number) {
    return request({
        url: `/user/${id}`,
    })
}

export function getUsersByIds(ids: number[]) {
    return request({
        url: '/user',
        params: {ids: ids}
    })
}

// 获取用户详细信息
export function getInfo() {
    return request({
        url: '/user/me',
        method: 'get'
    })
}

