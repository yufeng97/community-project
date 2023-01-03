<template>
  <div class="chat-box" :class="{active: isActive}">
    <el-avatar :size="45" :src="source.user.avatar" />

    <div class="username-message-box">
      <span class="username">{{ source.user.username }}</span>
      <span class="message">{{ source.lastMessage }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ChatRoom } from "@/types";
import { PropType } from "vue";
const props = defineProps({
  source: {
    type: Object as PropType<ChatRoom>,
    required: true,
  },
  isActive: {
    type: Boolean,
    required: false,
    default: false,
  }
});
</script>

<style lang="scss" scoped>
.chat-box {
  display: flex;
  padding: 0.5rem 0.3rem;
  cursor: pointer;
  &:hover{
    background-color: grey;
  }
  &.active {
    background-color: gray;
  }
  .username-message-box {
    margin-left: 2px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items:flex-start;
    .username{
      font-size: large;
    }
  }
}
</style>
