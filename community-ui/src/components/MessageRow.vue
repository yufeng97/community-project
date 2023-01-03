<template>
  <div class="message">
    <div class="message-time">
      <time>{{message.createTime}}</time>
    </div>

    <div class="user-avatar">
      <img
        src="https://tva4.sinaimg.cn/crop.0.0.750.750.180/75f2b996jw8f6zkdm7qp7j20ku0kudgr.jpg"
        alt="{{username}}"
      />
    </div>
    <h4 class="message-heading">
      <span>{{username}}</span>
    </h4>
    <div class="message-content">
      {{message.content}}
    </div>
  </div>
</template>

<script setup lang="ts">
import { PropType, ref } from "vue";
import { Message, MessageRaw, User } from "@/types";
const props = defineProps({
  message: {
    required: true,
    type: Object as PropType<MessageRaw>,
  },
});

const username = ref(props.message.senderId);
</script>

<style scoped>
h4 {
  font-weight: 500;
}
.message {
  position: relative;
  padding: 0.8rem 5.5rem;
  font-size: 1rem;
}
.message-time {
  display: flex;
  justify-content: center;
  padding-bottom: 0.2rem;
}

.user-avatar {
  position: absolute;
  width: 4rem;
  height: 4rem;
  left: calc((5.5rem - 4rem) / 2 - 5px);
  overflow: hidden;
  background: #e6e6e6; /* 背景色兜底，防止头像“走光” */
  border-radius: 50%;
}

.user-avatar > img {
  display: block;
  max-width: 100%;
  margin: auto;
}

.message-heading {
  margin: 0;
  margin-bottom: 0.4rem;
  font-size: inherit;
  color: #ababab;
}

.message-heading > span {
  margin-right: 0.5em;
}

.message-content {
  position: relative;
  display: inline-block;
  min-height: 1em;
  padding: 0.5em 1em;
  line-height: 1.6;
  background: #f2f4f7;
  border-radius: 10px;
  border-top-left-radius: 0;
}

.message-content::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: calc(5.5rem - 4rem);
  height: calc(5.5rem - 4rem);
  transform: translateX(-100%);

  /* 弧形箭头借鉴了 @guisturdy 的代码 */
  box-shadow: 0.5rem 0 0 #f2f4f7;
  border-radius: 50% 50% 0 0;
}

.message.oneself {
  direction: rtl;
}

.oneself > .user-avatar {
  left: auto;
  right: calc((5.5rem - 4rem) / 2 - 5px);
}

.oneself > .message-heading > span {
  float: right;
  margin-right: 0;
  margin-left: 0.5em;
}

.oneself > .message-content {
  direction: ltr;
  color: #fff;
  background: #00bdfe;
  border-radius: 10px;
  border-top-right-radius: 0;
}

.oneself > .message-content::before {
  left: auto;
  right: 0;
  transform: translateX(1rem);
  box-shadow: inset 0.5rem 0 0 #00bdfe;
}
</style>
