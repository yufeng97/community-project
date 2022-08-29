export interface User {
    id: string | number,
    username: string,
    avatar: string,
}

export interface Comment {
    id: string | number,
    content: string,
    author: User,
    createTime: string,
    likesCount: number,
    liked: boolean,
    replyTarget: User,
    replies?: [Comment]
}

