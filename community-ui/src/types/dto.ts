export interface CommentDTO {
    postId: string,
    content: string,
}

export interface ReplyDTO {
    commentId: string,
    content: string,
}

export interface PostDTO {
    title: string,
    content: string,
}