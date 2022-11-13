<template>
  <!-- 文章卡片 -->
  <div class="nk-home-card">
    <div class="user-info-box">
      <router-link :to="``">
        <el-avatar :size="40" :src="postInfo.author.avatar" />
      </router-link>

      <div class="user-time-box">
        <span
          ><router-link :to="`/user/${postInfo.author.id}`"
            >{{ postInfo.author.username }}
          </router-link>
        </span>
        <time>{{ formatTime(postInfo.createTime) }}</time>
      </div>
    </div>

    <div class="post-info-box">
      <div class="post-title">
        <router-link :to="`/post/${postInfo.id}`">
          <span>{{ postInfo.title }}</span>
        </router-link>
      </div>
      <div class="post-brief">
        <router-link :to="`/post/${postInfo.id}`">{{
          postInfo.brief
        }}</router-link>
      </div>
    </div>

    <div class="post-action-box">
      <like-button
        :id="postInfo.id"
        :liked="postInfo.liked"
        :likes-count="postInfo.likeCount"
        :size="'20px'"
      />
      <reply-button :reply-count="postInfo.commentCount" :size="'20px'" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { PropType } from "vue";
import type { PostInfo } from "@/types";
import { formatTime } from "@/utils/dataFormat";
import LikeButton from "@/components/LikeButton.vue";
import ReplyButton from "@/components/ReplyButton.vue";

defineProps({
  postInfo: {
    type: Object as PropType<PostInfo>,
    required: true,
  },
});
</script>

<style lang="scss" scoped>
a {
  text-decoration: none;
  color: black;
}

.nk-home-card {
  display: flex;
  flex-direction: column;
  .user-info-box {
    display: flex;
    .user-time-box {
      margin: 2px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      time {
        font-size: var(--el-font-size-extra-small);
        color: rgb(98, 100, 102);
      }
    }
  }
  .post-title {
    font-size: larger;
    font-weight: bold;
    margin: 1ch 0;
  }
  .post-brief {
    font-size: medium;
    margin: 1ch 0;
  }
  .post-action-box {
    display: flex;
    flex: 0 0 auto;
    flex-direction: row;
    align-content: flex-start;
    justify-content: space-between;
    max-width: 7.04rem;
  }
}
</style>
