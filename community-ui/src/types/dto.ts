export interface CommentDTO {
    postId: string | number,
    content: string,
    targetId?: string | number,
    commentId?: string | number,
}

export interface ReplyDTO {
    commentId: string,
    content: string,
    toId: string,
}

export interface PostDTO {
    title: string,
    content: string,
}