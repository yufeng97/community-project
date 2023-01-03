<template>
  <section>
    <div class="left">

      <!-- <ChatItem
        v-for="chatItem in chatRoomList"
        :key="chatItem.id"
        :chat="chatItem"
        :is-active="chatItem.id === activeChatRoomIndex"
        @click="handleClick(chatItem)"
      /> -->
      <ConversationListPanel></ConversationListPanel>
    </div>

    <div class="right">
      <!-- <ChatBox :container="(right as HTMLElement)" ref="chatBox" /> -->
      <!-- <div class="message-box-container" ref="container">
        <vue-eternal-loading
          :load="infiniteHandler"
          position="top"
          :container="(container as HTMLElement)"
        >
          <template #loading>
            <div class="my-loading">Trying to load content...</div>
          </template>

          <template #no-more>
            <div class="my-no-more">There is no more content.</div>
          </template>

          <template #no-results>
            <div class="my-no-results">It's empty here.</div>
          </template>

          <template #error>
            <div class="my-error">Oops. We've got an error.</div>
          </template>
        </vue-eternal-loading>

        <div class="message-box" v-for="item in messageList" :key="item.id">
          <MessageRow :message="item" />
        </div>
      </div> -->


    </div>
  </section>
</template>

<script setup lang="ts">
import { Ref, ref, reactive, computed } from "vue";
import { getChatList, getMessages } from "@/api/message";
import { ChatRoom, MessageRaw, Page } from "@/types";
import ChatItem from "@/components/ChatItem.vue";
import ChatBox from "@/components/ChatBox.vue";
import { getUsersByIds } from "@/api/user";
import { VueEternalLoading, LoadAction } from "@ts-pro/vue-eternal-loading";
import MessageRow from "@/components/MessageRow.vue";
import ConversationListPanel from "@/components/conversationList/ConversationListPanel.vue";


// const count = ref(16);
const loading = ref(false);
// const noMore = computed(() => count.value >= 20);
// const disabled = computed(() => loading.value || noMore.value);
const disabled = ref(false);

const container = ref<HTMLElement | null>(null);

const chatRoomList = ref<ChatRoom[]>([]);
const chatRoomMap = ref<Map<Number, MessageRaw[]>>(
  new Map<Number, MessageRaw[]>()
);
const activeChatRoomIndex = ref(0);
const activeChatRoom = ref<ChatRoom | null>(null);
const activeTalkerId = ref(0);

const messageList = ref<MessageRaw[]>([]);

const load = () => {
  disabled.value = true;
  loading.value = true;
  // setTimeout(() => {
  //   count.value += 2;
  //   loading.value = false;
  // }, 2000);
  let chatLen = chatRoomList.value.length;
  let updateTime = chatRoomList.value.at(chatLen - 1)?.updateTime;
  let time = updateTime ? new Date(updateTime).getTime() : undefined;
  // let time = Date.now();
  getChatList(time).then((res) => {
    chatRoomList.value.push(...res.data.list);
    activeChatRoomIndex.value = chatRoomList.value[0].id;
    activeChatRoom.value = chatRoomList.value[0];
    disabled.value = !res.data.hasMore;
    loading.value = false;
  });
};


const handleClick = async (chatItem: ChatRoom) => {
  console.log(chatItem);

  activeChatRoomIndex.value = chatItem.id;
  activeTalkerId.value = chatItem.user.id as number;
  let talkerId = activeTalkerId.value;
  // if (chatBox.value) {
  //   chatBox.value.handleChange(key);
  // }
  const rawMessages = await fetchMessages(chatItem.user.id as number);
  if (!chatRoomMap.value.has(talkerId)) {
    chatRoomMap.value.set(talkerId, rawMessages);
  } else {
    chatRoomMap.value.get(talkerId)?.push(...rawMessages);
  }
  messageList.value = chatRoomMap.value.get(talkerId)!;
};

const infiniteHandler = async ({ loaded }: LoadAction) => {
  let talkerId = activeTalkerId.value;
  const rawMessages = await fetchMessages(activeTalkerId.value);
  if (!chatRoomMap.value.has(talkerId)) {
    chatRoomMap.value.set(talkerId, rawMessages);
  } else {
    chatRoomMap.value.get(talkerId)?.push(...rawMessages);
  }
  messageList.value = chatRoomMap.value.get(talkerId)!;
  loaded(rawMessages.length, 10);
};

const fetchMessages = async (talkerId: number) => {
  const rawMessages: MessageRaw[] = await getMessages(
    talkerId,
    Date.now()
  ).then((res) => {
    console.log("fetch message", res);
    return res.data.list;
  });
  return rawMessages;
};

getUsersByIds([111, 112]).then((res) => console.log(res));

const handleCurrentChange = () => {
  // getMessage({
  //   pageNum: page.pageNum,
  //   pageSize: page.pageSize,
  // }).then((res) => {
  //   totalUnreadCount.value = res.data;
  //   total.value = res.total;
  //   data.value = res.rows;
  //   loading.value = false;
  // });
};

// init
// loading.value = true;
handleCurrentChange();
</script>

<style lang="scss" scoped>
section {
  display: flex;
  height: calc(100vh - 58px - 148px);
  .left {
    height: 100%;
    text-align: center;
    width: 30%;
    .list {
      height: 100%;
      overflow-y: auto;
      display: flex;
      flex-direction: column;
      background-color: whitesmoke;
    }
  }
  .right {
    // height: 100%;
    overflow-y: auto;
    // display: flex;
    flex: 1 0;
    .message-box {
      background-color: white;
      // display: flex;
      flex: 1 1;
      // width: 100%;
    }
  }
}
.el-pagination {
  margin-top: 30px;
  text-align: center;
}
</style>
