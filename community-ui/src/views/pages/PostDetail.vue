<template>
  <!-- 帖子详情 -->
  <nk-post :post-detail="postDetail" />

  <el-card class="box-card">
    <!-- 回帖数量 -->

    <div class="comment-count-box">
      <span> {{ postDetail?.commentCount }}条回帖 </span>
      <el-button type="primary" size="default" @click="handleScrollToInput()"
        >回&nbsp;&nbsp;帖</el-button
      >
    </div>

    <nk-comment
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
  <el-card class="box-card" id="comment-input">
    <comment-input
      ref="input"
      placeholder="在这里畅所欲言你的看法吧"
      :post-id="postId"
      @commentSubmit="handleCommentSubmit"
    />
  </el-card>
</template>

<script setup lang="ts">
import NkPost from "@/components/NkPost.vue";
import NkComment from "@/components/NkComment.vue";
import CommentInput from "@/components/CommentInput.vue";
import { computed, onMounted, reactive, ref } from "vue";
import { getPostDetail } from "@/api/home";
import { getPostComments } from "@/api/comment";
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

const handleCommentSubmit = () => {
  getPost();
  getComments(page.pageNum);
};

const getPost = () => {
  getPostDetail(postId.value, page).then((res) => {
    postDetail.value = res.data;
    // Object.assign(res.data, )
    commentCount.value = res.data.commentCount;
    console.log(postDetail.value);
  });
};

const getComments = (pageNum: number = 1) => {
  console.log(postId);
  console.log(page);
  
  getPostComments(
    postId.value,
    //  { pageNum: pageNum, pageSize, orderBy }
    page
  ).then((res) => {
    console.log(res);
    comments.value = res.data.rows;
    router.replace({ path: route.path, query: { page: pageNum } });
  });
};

getPost();
getComments(page.pageNum);
</script>

<style scoped>
.comment-count-box {
  display: flex;
  justify-content: space-between;
}
</style>
