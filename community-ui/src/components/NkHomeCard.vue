<template>
  <!-- 文章卡片 -->
  <div class="nk-home-card">
    <router-link :to="``">
      <el-avatar
        :size="50"
        :src="postInfo.author.avatar"
        src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
      />
    </router-link>

    <div class="meta-box">
      <router-link :to="`/post/${postInfo.id}`"
        ><span class="post-title">{{ postInfo.title }}</span></router-link
      >

      <div class="post-meta">
        <span>
          <router-link :to="`/user/${postInfo.author.id}`"
            ><span>{{ postInfo.author.username }}</span>
          </router-link>
          &nbsp;&nbsp;发布于&nbsp;&nbsp;
          <time>{{ postInfo.createTime }}</time>
        </span>
        <div>
          <span>
            点赞&nbsp;&nbsp;{{ Math.floor(postInfo.score) }}&nbsp;&nbsp;
          </span>
          |
          <span>&nbsp;&nbsp;回帖&nbsp;&nbsp; {{ postInfo.commentCount }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { PropType } from "vue";
import type { PostInfo } from "@/types";
import { dataFormat } from "@/utils/dataFormat";
defineProps({
  postInfo: {
    type: Object as PropType<PostInfo>,
    required: true,
  },
});
</script>

<style lang="scss" scoped>
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
/* .nk-home-card {
    border-bottom: 1px solid var(--el-border-color-base);
} */

.nk-home-card {
  display: flex;
  justify-content: start;
  align-items: center;

  .meta-box {
    flex: 1 1 auto;
    padding-left: 6px;
    .post-meta {
      display: flex;
      justify-content: space-between;
      font-size: var(--el-font-size-extra-small);
      color: rgb(98, 100, 102);
    }
  }
}
</style>
