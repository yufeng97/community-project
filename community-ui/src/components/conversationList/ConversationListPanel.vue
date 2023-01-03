<template>
  <section>
    <virtual-list
      :data-key="'id'"
      :data-sources="chatRoomList"
      :data-component="ChatItem"
      :page-mode="true"
    >
      <div slot="footer" class="loading-spinner text-center">加载中...</div>
    </virtual-list>
  </section>
</template>

<script setup lang="ts">
import { getChatList } from "@/api/message";
import { ChatRoom } from "@/types";
import { onMounted, ref } from "vue";
import ChatItem from "@/components/ChatItem.vue";

const chatItem = ChatItem;
const list = ref<Number[]>([]);
const chatRoomList = ref<ChatRoom[]>([]);

onMounted(() => {
  for (let i = 0; i < 1000; i++) {
    list.value.push(i);
  }

  let time = Date.now();
  getChatList(time).then((res) => {
    console.log("res", res.data);
    
    chatRoomList.value.push(...res.data.list);
  });
});
</script>

<style scoped></style>
