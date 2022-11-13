<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header" v-if="postDetail">
        <div class="title-box">
          <div class="post-title">
            <img
              src="http://static.nowcoder.com/images/img/icons/ico-discuss.png"
            />
            <span>{{ postDetail.title }}</span>
          </div>
          <el-button-group>
            <el-button type="danger">置顶</el-button>
            <el-button type="danger">加精</el-button>
            <el-button type="danger">删除</el-button>
          </el-button-group>
        </div>

        <div class="nk-post-card">
          <router-link :to="`/user/${postDetail.author.id}`">
            <el-avatar :size="50" :src="postDetail.author.avatar" />
          </router-link>

          <div class="post-meta-box">
            <router-link :to="`/user/${postDetail.author.id}`">
              <span class="post-author">
                {{ postDetail.author.username }}
              </span>
            </router-link>

            <!-- 评论或回复时间及操作 -->
            <div class="reply-stat">
              <time
                :title="formatTime(postDetail.createTime, true)"
                :datetime="postDetail.createTime"
                >{{ formatTime(postDetail.createTime) }}</time
              >
              <div class="action-box">
                <like-button
                  :id="postDetail.id"
                  :liked="postDetail.liked"
                  :likes-count="postDetail.likeCount"
                  @like-click="handlePostLikeClick"
                />

                <reply-button
                  :reply-count="postDetail.commentCount"
                  @mousedown.prevent=""
                  @click="handleScrollToInput"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
    <!-- 正文 -->
    <div class="text item" v-if="postDetail">
      {{ postDetail.content }}
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { reactive, ref, Ref, PropType } from "vue";
import { PostInfo, Page, User, PostDetail } from "@/types";
import likeButton from "@/components/LikeButton.vue";
import { postLike } from "@/api/like";
import { formatTime } from "@/utils/dataFormat";
import ReplyButton from "./ReplyButton.vue";

const props = defineProps({
  postDetail: {
    type: Object as PropType<PostDetail>,
    required: true,
  },
});

const handlePostLikeClick = (liked: boolean) => {
  console.log("post id", props.postDetail.id, "like status: ", liked);
  postLike(props.postDetail.id);
};

const handleScrollToInput = () => {
  const commentInput = document.querySelector("#comment-input");
  console.log(commentInput);

  commentInput?.scrollIntoView({
    behavior: "smooth",
  });
};
</script>

<style lang="scss" scoped>
.title-box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .post-title {
    display: flex;
    align-items: center;
  }
}
.nk-post-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .post-meta-box {
    flex-grow: 1;
    padding-left: 6px;
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
        width: 0.8rem;
        height: 0.8rem;
      }
      .action-title {
        margin-left: 0.2rem;
        font-size: 0.8rem;
      }
    }
  }
}
</style>
