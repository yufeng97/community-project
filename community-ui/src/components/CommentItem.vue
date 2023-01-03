<template>
  <div class="comment-item" :class="{ 'sub-comment-item': isSubComment }">
    <div class="comment">
      <!-- 评论或回复人头像 -->
      <router-link :to="`/user/${comment.author.id}`">
        <el-avatar
          class="avatar"
          :src="comment.author.avatar || ''"
          @error="(e) => (e.target as HTMLElement).classList.add('error')"
        />
      </router-link>

      <div class="content-box">
        <!-- 评论或回复人具体信息 -->
        <div class="meta-box">
          <router-link :to="`/user/${comment.author.id}`">
            <slot name="userMeta">
              <div class="user-popover-box">
                <span v-if="comment.author">{{
                  comment.author.username +
                  (comment.author.isAuthor === true ? "（作者）" : "")
                }}</span>
              </div>
            </slot>
          </router-link>

          <span class="comment-info" v-if="!isSubComment"> {{ level }} # </span>
        </div>

        <!-- 评论或回复内容 -->
        <div class="content">
          <span v-if="parent" class="reply"
            >回复
            <span class="reply-target">
              <router-link
                v-if="(comment as Reply).target "
                :to="`/user/${(comment as Reply).target?.id}`"
              >
                {{ (comment as Reply).target?.username }}
              </router-link>

              <router-link v-else :to="`/user/${parent.author.id}`">
                {{ parent.author.username }}
              </router-link>
              ：
            </span>
          </span>
          {{ comment.content }}
        </div>

        <!-- 评论或回复时间及操作 -->
        <div class="reply-stat">
          <time
            :title="formatTime(comment.createTime, true)"
            :datetime="comment.createTime"
            >{{ formatTime(comment.createTime) }}</time
          >
          <div
            v-if="user.isAuthor === true"
            class="delete"
            @click.stop="$emit('comment-delete', { id, comment, parent })"
          >
            <span>·</span>删除
          </div>
          <div class="action-box">
            <like-button
              :id="id"
              :liked="comment.liked"
              :likes-count="comment.likesCount"
              @like-click="handleCommentLikeClick"
            />
            <reply-button
              :reply-count="comment.replyCount"
              @click="replyButtonClick"
            />
          </div>
        </div>

        <comment-input
          v-if="isShowInput"
          :id="id"
          :placeholder="`回复${user && user.username}...`"
          @reply-submit="$emit('update-replies')"
          @comment-submit="handleCommentSubmit"
        />

        <!-- 评论表单组件 -->
        <slot :id="id" />

        <!-- 回复列表 -->
        <slot name="subList" :parentId="id" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { addComment } from "@/api/comment";
import { commentLike } from "@/api/like";
import { Comment, Reply, User } from "@/types";
import { computed, ref, PropType } from "vue";
import CommentInput from "./CommentInput.vue";
import LikeButton from "./LikeButton.vue";
import ReplyButton from "./ReplyButton.vue";

const props = defineProps({
  id: {
    type: [String, Number],
    required: true,
  },
  postId: {
    type: [String, Number],
    required: true,
  },
  comment: {
    type: Object as PropType<Comment>,
    default: () => {},
    required: true,
  },
  parent: {
    type: Object as PropType<Comment>,
    default: () => {},
  },
  user: {
    type: Object as PropType<User>,
    default: () => {},
  },
  level: {
    type: Number,
    default: 1,
  },
});

const emit = defineEmits(["update-replies"]);

const handleCommentSubmit = (value: string) => {
  console.log("handleCommentSubmit", value, "id", props.id);
  const commentId = props.parent ? props.parent.id : props.comment.id;
  addComment({
    content: value,
    postId: props.postId,
    commentId: commentId,
    targetId: props.comment.author?.id,
  });
};

const handleCommentLikeClick = (liked: boolean) => {
  console.log("comment id", props.id, "like status: ", liked);
  commentLike(props.id);
};

const isShowInput = ref(false);

const replyButtonClick = () => {
  isShowInput.value = !isShowInput.value;
};

const isSubComment = computed(() => {
  return props.parent != undefined || props.parent != null;
});

const formatTime = (time: string, local = false) => {
  const d = new Date(time);
  if (local) {
    return d.toString();
  }
  const now = Date.now();
  const diff = (now - d.getTime()) / 1000;
  switch (true) {
    case diff < 30:
      return "刚刚";
    case diff < 3600:
      return Math.ceil(diff / 60) + "分钟前";
    case diff < 3600 * 24:
      return Math.ceil(diff / 3600) + "小时前";
    case diff < 3600 * 24 * 30:
      return Math.floor(diff / 3600 / 24) + "天前";
    case diff < 3600 * 24 * 365:
      return Math.floor(diff / 3600 / 24 / 30) + "月前";
    default:
      return Math.floor(diff / 3600 / 24 / 365) + "年前";
  }
};
</script>

<style lang="scss" scoped>
.comment-info {
  border-radius: 4px;
  border: 1px solid var(--el-border-color-base);
  padding: 5px 10px;
  background-color: gray;
  color: white;
}

.comment-item {
  margin-bottom: 1.0664rem;
  &:not(:last-child) {
    .content-box {
      border-bottom: 1px solid #f1f1f1;
    }
  }
  &:hover {
    .comment .reply-stat .delete {
      visibility: visible;
    }
  }
  .comment {
    display: flex;
    .content-box {
      margin-left: 0.6664rem;
      flex: 1 1 auto;
      &.focus {
        padding-bottom: 0.4rem;
      }
      .meta-box {
        display: flex;
        align-items: center;
        font-size: 0.8664rem;
        line-height: 1.25;
        white-space: nowrap;
        justify-content: space-between;
        .user-popover-box {
          cursor: pointer;
        }
      }
      .content {
        margin-top: 0.44rem;
        font-size: 0.8664rem;
        line-height: 1.4664rem;
        white-space: pre-line;
        word-break: break-all;
        color: #505050;
        overflow: hidden;
        .img-box {
          margin-top: 0.5rem;
          img {
            max-width: 100%;
            max-height: 20rem;
            object-fit: cover;
          }
        }
        .reply {
          vertical-align: top;
        }
        .reply-target {
          cursor: pointer;
          color: #406599;
        }
      }
    }
    .reply-stat {
      display: flex;
      margin-top: 7px;
      font-weight: 400;
      time,
      .delete {
        font-size: 0.8664rem;
        color: #8a9aa9;
      }
      .delete {
        visibility: hidden;
        cursor: pointer;
        span {
          margin: 0 0.2rem;
        }
      }
      .action-box {
        flex: 0 0 auto;
        display: flex;
        justify-content: space-between;
        margin-left: auto;
        min-width: 7.04rem;
        color: #8a93a0;
        user-select: none;
      }
    }
  }
}
.sub-comment-list {
  margin: 0.8rem 0;
  padding: 0 0 0 0.8rem;
  background-color: #fafbfc;
  border-radius: 3px;
  .comment-item {
    margin-bottom: 0;
    &:last-child .content-box {
      border-bottom: none;
    }
    .comment {
      position: relative;
      padding: 0.8rem 0 0;
      .content-box {
        margin-right: 0.8rem;
        padding-bottom: 0.8rem;
      }
    }
  }
}
</style>
