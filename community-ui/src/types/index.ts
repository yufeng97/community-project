export interface User {
    id: number | string,
    username: string,
    type: number,
    avatar: string,
    createTime?: string,
}


export interface DiscussPost {
    id: number | string,
    userId: number,
    title: string,
    content: string,
    type: number,
    createTime: string,
    commentCount: number,
    score: number,
    likeCount?: number,
}

export interface PostInfo {
    id: number | string,
    title: string,
    createTime: string,
    commentCount: number,
    score: number,
    author: User,
    likeCount?: number,
    type?: number,
}

export interface PostDetail extends PostInfo {
    content: string,
}

export interface Reply {
    id: string | number,
    content: string,
    createTime: string,
    likesCount: number,
    liked: boolean,
    author: User,
    target: User | null,
}

export interface Comment {
    id: string | number,
    content: string,
    createTime: string,
    likesCount: number,
    liked: boolean,
    author: User,
    replyCount: number,
    replies: [Reply],
}


export interface HomeDataTableBody {
    info: PostInfo,
}

export interface RegisterBody {

    username: string,
    password: string,
    email: string

}

export interface LoginBody {

    username: string,
    password: string,
    code: string
    uuid: string

}

export interface Page {

    pageNum:number
    pageSize?:number
    orderBy?:string,
    // isAsc:string

}

