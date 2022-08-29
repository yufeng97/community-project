<template>
  <comment-item
    v-for="(child, j) in replies"
    :id="`${id}-${j}`"
    :key="`${id}-${j}`"
    :ref="`${id}-${j}`"
    :comment="child"
    :user="child.author"
    :parent="parent"
  />
  <el-pagination
    background
    layout="prev, pager, next"
    hide-on-single-page
    :pageSize="pageSize"
    v-model:currentPage="pageNum"
    :total="parent?.replyCount"
    @current-change="handleCurrentChange(pageNum, commentId)"
  />
</template>

<script setup lang="ts">
import { getCommentReplies } from "@/api/comment";
import { Reply, Comment } from "@/types";
import { PropType, ref } from "vue";
import CommentItem from "./CommentItem.vue";

const props = defineProps({
  id: {
    type: [String, Number],
    required: true,
  },
  commentId: {
    type: [String, Number],
    required: true,
  },
  replies: {
    type: Object as PropType<Reply[]>,
    required: true,
  },
  parent: {
    type: Object as PropType<Comment>,
    required: false,
  },
});

const pageNum = ref(1);
const pageSize: number = 5;
const replies = ref(props.replies);

const handleCurrentChange = (
  currentPage: number,
  commentId: string | number
) => {
  getCommentReplies(commentId, { pageNum: currentPage })
    .then((res) => res.data)
    .then((res) => {
      console.log(res);
      replies.value = res.rows;
    });
};
</script>

<style scoped></style>
