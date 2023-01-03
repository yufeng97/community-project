<template>
  <!-- 帖子详情 -->
  <nk-post :post-detail="postDetail" v-if="postDetail" />

  <el-card>
    <!-- 回帖数量 -->
    <div class="comment-count-box">
      <span> {{ postDetail?.commentCount }}条回帖 </span>
      <el-button type="primary" size="default" @click="handleScrollToInput"
        >回&nbsp;&nbsp;帖</el-button
      >
    </div>

    <comment-list
      :post-id="postId"
      :comments="comments"
      v-if="comments.length > 0"
    />

    <el-pagination
      background
      layout="prev, pager, next"
      hide-on-single-page
      v-model:currentPage="page.pageNum"
      :pageSize="page.pageSize"
      :total="commentCount"
      @current-change="handleCurrentChange"
    />
  </el-card>

  <!-- 回帖输入 -->
  <div id="comment-input">
    <comment-input
      ref="input"
      placeholder="在这里畅所欲言你的看法吧"
      @comment-submit="handleCommentSubmit"
    />
  </div>
</template>

<script setup lang="ts">
import NkPost from "@/components/NkPost.vue";
import CommentList from "@/components/CommentList.vue";
import CommentInput from "@/components/CommentInput.vue";
import { computed, onBeforeMount, onMounted, reactive, ref } from "vue";
import { getPostDetail } from "@/api/home";
import { addComment, getPostComments } from "@/api/comment";
import { useRoute, useRouter } from "vue-router";
import { PostDetail, User, Comment } from "@/types";

const route = useRoute();
const router = useRouter();

const pageNum: number = 1;
const pageSize: number = 7;
const orderBy: string = "createTime";
const page = reactive({
  pageNum,
  pageSize,
  orderBy,
  // isAsc
});

if (route.query.page) {
  page.pageNum = Number.parseInt(route.query.page as string);
}

const comments = ref<Comment[]>([]);

const commentCount = ref(0);

const postId = computed(() => {
  return route.params.id as string;
});

const postDetail = ref<PostDetail>();

const handleScrollToInput = () => {
  const commentInput = document.querySelector("#comment-input");
  commentInput?.scrollIntoView({
    behavior: "smooth",
  });
  const input = ref<InstanceType<typeof CommentInput>>();
  console.log("input focus?", input.value?.focus);
};

const handleCurrentChange = (currentPage: number) => {
  getComments(currentPage);
  router.replace({ path: route.path, query: { page: currentPage } });
};

const handleCommentSubmit = (value: string) => {
  addComment({
    content: value,
    postId: postId.value,
  }).then(() => {
    getPost();
    getComments(page.pageNum);
  });
};

const getPost = () => {
  getPostDetail(postId.value, page).then((res) => {
    postDetail.value = res.data;
    // Object.assign(res.data, )
    commentCount.value = res.data.commentCount;
  });
};

const getComments = (pageNum: number = 1) => {
  getPostComments(
    postId.value,
    //  { pageNum: pageNum, pageSize, orderBy }
    page
  ).then((res) => {
    comments.value = res.data.rows;
    router.replace({ path: route.path, query: { page: pageNum } });
  });
};

onMounted(() => {
  getPost();
  getComments(page.pageNum);
});
</script>

<style scoped></style>
