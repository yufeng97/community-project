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
            <el-avatar
              :size="50"
              :src="postDetail.author.avatar"
              src="http://static.nowcoder.com/images/img/icons/ico-discuss.png"
            />
          </router-link>

          <div class="post-meta-box">
            <router-link :to="`/user/${postDetail.author.id}`">
              <span class="post-author">
                {{ postDetail.author.username }}
              </span>
            </router-link>

            <div class="post-meta">
              <span>发布于&nbsp;{{ postDetail.createTime }}</span>
              <span>
                <span
                  >点赞&nbsp;&nbsp;{{ Math.floor(postDetail.likeCount!) }}</span
                >
                <span>&nbsp;&nbsp;回帖&nbsp;&nbsp;</span>
              </span>
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

defineProps({
  postDetail: {
    type: Object as PropType<PostDetail|undefined>,
    required: true,
  },
});
</script>

<style lang="scss" scoped>
.title-box {
  display: flex;
  justify-content: space-between;
}
.nk-post-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .post-meta-box {
    flex-grow: 1;
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
