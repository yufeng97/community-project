<template>
  <!-- 回帖列表 -->

  <!-- 单条评论 -->
  <comment-item
    v-for="(comment, i) in comments"
    :id="`comment-${i}`"
    :key="`comment-${i}`"
    :ref="`comment-${i}`"
    :level="i + 1"
    :user="comment.author"
    :comment="comment"
  >
    <!-- 单条评论下的回复列表 -->
    <template #subList="{ parentId }">
      <div class="sub-comment-list" v-if="comment.replies.length > 0">
        <reply-list
          :id="parentId"
          :comment-id="comment.id"
          :replies="comment.replies"
          :parent="comment"
        />
      </div>
    </template>
  </comment-item>
</template>

<script setup lang="ts">
import { getPostComments, getCommentReplies } from "@/api/comment";
import { Comment } from "@/types";
import { onMounted, PropType, reactive, ref } from "vue";

import CommentItem from "./CommentItem.vue";
import ReplyList from "./ReplyList.vue";

const props = defineProps({
  postId: {
    type: [String, Number],
    required: true,
  },
  comments: {
    type: Object as PropType<Comment[]>,
    required: true,
  },
});

const pageNum = ref(1);
const pageSize: number = 5;
const isShowInput = ref(false);

const handleCurrentChange = (
  currentPage: number,
  commentId: string | number,
  index: number
) => {
  getCommentReply(commentId, currentPage, index);
};


const addCommentClick = () => {
  isShowInput.value = !isShowInput.value;
};

const getCommentReply = (
  commentId: string | number,
  page: number = 1,
  index: number
) => {
  getCommentReplies(commentId, { pageNum: page })
    .then((res) => res.data)
    .then((res) => {
      console.log(res);
      props.comments[index].replies = res.rows;
    });
};
</script>

<style lang="scss" scoped>
.align-right {
  text-align: right;
}

a:link {
  text-decoration: none;
}

a:visited {
  text-decoration: none;
}

a:hover {
  text-decoration: none;
  color: #000000;
}

a:active {
  text-decoration: none;
  color: #000000;
}

.nk-avatar {
  text-align: center;
  margin-top: 25px;
}

.disscuss-post-info {
  width: 100%;
  text-align: left;
  margin-left: 20px;
  margin-top: 5px;
  margin-bottom: 5px;
}

.re-info {
  text-align: right;
  margin-right: 0px;
  margin-top: 48px;
}

.comment-info {
  text-align: right;
  border-radius: 4px;
  border: 1px solid var(--el-border-color-base);
  padding: 5px 10px;
  background-color: gray;
  color: white;
}

.input-size {
  width: 100%;
  height: 20px;
}

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

.action-title {
  margin-left: 0.2rem;
  font-size: 0.8rem;
}

.icon {
  min-width: 16.5px;
  min-height: 16.5px;
  width: 10px;
  height: 10px;
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

    .action {
      display: flex;
      align-items: center;
      margin-left: 0.4rem;
      cursor: pointer;

      &:hover {
        opacity: 0.8;
      }

      &.active {
        color: #37c700;
      }

      .icon {
        min-width: 16.5px;
        min-height: 16.5px;
        width: 10px;
        height: 10px;
      }

      .action-title {
        margin-left: 0.2rem;
        font-size: 0.8rem;
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
