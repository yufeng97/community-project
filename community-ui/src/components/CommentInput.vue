<template>
  <div :class="`${className} comment-form`">
    <div class="avatar-box">
      <slot />
    </div>

    <div class="form-box">
      <div class="rich-input" :class="{ focus: focus || value }">
        <div class="grow-wrap" :data-replicated-value="value">
          <textarea
            ref="input"
            rows="1"
            :value="value"
            :placeholder="placeholder"
            @input="(e) => (value = (e.target as HTMLInputElement).value)"
            @focus="focus = true"
            @blur="handleBlur"
            @mousedown="closeEmojiSelector"
          />
        </div>
      </div>
      <div
        v-show="focus || value"
        class="option-box"
        @mousedown.prevent="closeEmojiSelector()"
      >
        <div
          class="emoji emoji-btn"
          @mousedown.prevent.stop="openEmojiSelector"
        >
          <div class="emoji-box">
            <div class="icon" />
            <span>表情</span>
          </div>
          <EmojiSelector
            v-show="showEmojiSelector"
            @choose="(v) => (value += v)"
          />
        </div>

        <slot name="submitBtn">
          <button
            class="submit-btn"
            :disabled="!value"
            @click.stop="handleSubmit"
          >
            评论
          </button>
        </slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  defineProps,
  computed,
  ref,
  nextTick,
  onMounted,
  onBeforeUnmount,
} from "vue";
import EmojiSelector from "./EmojiSelector.vue";
import userStore from "@/store/user";
import { useRouter } from "vue-router";

const router = useRouter();

const store = userStore();

const props = defineProps({
  placeholder: {
    type: String,
    default: "输入评论...",
  },
  id: {
    type: [String, Number],
    default: "comment-root",
  },
  comment: {
    type: Object,
    default: () => {},
  },
  parent: {
    type: Object,
    default: () => {},
  },
  postId: {
    type: String,
  },
  // uploadImg: {
  //   type: Function,
  //   default: null,
  // },
});

const focus = ref(false); // * 聚焦状态
const value = ref(""); // * 输入框值
// const imgSrc = ref(""); // * 粘贴的图片src
const showEmojiSelector = ref(false); // * 表情选择框状态
// const onUpload = ref(true);

// ref
const input = ref<HTMLInputElement | null>(null);
// const upload = ref<HTMLInputElement | null>(null);

// emit
const emit = defineEmits(["form-submit", "form-delete"]);

const isRoot = computed(() => {
  return props.id.toString() === "comment-root";
});

// 是否为回复中的表单
const isSub = computed(() => {
  return props.id.toString().split("-").length === 3;
});

const className = computed(() => {
  return isRoot.value
    ? "comment-root"
    : isSub.value
    ? "reply sub-reply"
    : "reply";
});

// * textarea blur 事件
const handleBlur = (e?: Event) => {
  showEmojiSelector.value = false;
  // if (onUpload.value) {
  //   nextTick(() => {
  //     onUpload.value = false;
  //   });
  //   return;
  // }
  // if (value.value || imgSrc.value) return;
  if (value.value) return;
  focus.value = false;
  if (!isRoot) {
    close();
  }
};

// * textarea paste 事件
const handlePaste = (e: ClipboardEvent) => {
  const file = e.clipboardData?.files[0];
  if (file) {
    // 只处理复制图片
    // beforeSetImg(file);
    e.preventDefault();
  }
};

// * 点击评论
const handleSubmit = () => {
  if (!value.value.trim()) return;
  console.log(props.id);
  console.log(value.value);
  console.log(props.comment);
  console.log(props.parent);
  console.log(store.isLogin);
  console.log(store.name);

  if (!store.isLogin) {
    router.push("/login");
  }

  // const user = (props.comment && props.comment.user) || null;
  // const data = {
  //   id: props.id,
  //   content: value.value,
  //   // imgSrc: imgSrc.value,
  //   reply: (isSub && JSON.parse(JSON.stringify(user))) || null,
  //   createAt: new Date().getTime(),
  //   likes: 0,
  //   callback: () => {
  //     isRoot ? reset() : close();
  //   },
  //   children: [],
  // };
  // if (!isSub) {
  //   data.children = [];
  // }
  // emit("form-submit", { newComment: data, parent: parent });
};

// * 重置组件状态
const reset = () => {
  value.value = "";
  // imgSrc.value = "";
  input.value?.blur();
};

// * 销毁组件
const close = () => {
  emit("form-delete", props.id);
};

// * 选择表情
const openEmojiSelector = () => {
  showEmojiSelector.value = !showEmojiSelector.value;
  if (document.activeElement === document.body) {
    input.value?.focus();
  }
  if (showEmojiSelector) {
    // 移动光标到末尾
    input.value!.selectionStart = input.value!.selectionEnd =
      value.value.length;
  }
};

// * 关闭选择表情组件
const closeEmojiSelector = () => {
  if (showEmojiSelector.value) {
    showEmojiSelector.value = false;
  }
};

onMounted(() => {
  const richInput = input.value!;
  !isRoot.value && richInput.focus();
  // richInput.addEventListener("paste", handlePaste);
});

onBeforeUnmount(() => {
  const richInput = input.value!;
  // richInput.removeEventListener("paste", handlePaste);
});

defineExpose({ focus });
</script>

<style lang="scss" scoped>
.comment-form {
  max-width: 100%;
  padding: 0.8rem 1.0664rem;
  display: flex;
  background-color: #fafbfc;
  border-radius: 3px;
  .avatar-box {
    flex: 0 0 auto;
    img {
      margin: 0 0.8rem 0 0;
    }
  }
  .form-box {
    flex: 1 1 auto;
    .rich-input {
      border-radius: 3px;
      border: 1px solid #f1f1f1;
      background-color: #fff;
      overflow: hidden;
      &.focus {
        border-color: #007fff;
      }
      .grow-wrap {
        display: grid;
        &::after {
          content: attr(data-replicated-value) " ";
          white-space: pre-wrap;
          visibility: hidden;
        }
        textarea {
          outline: none;
          border: none;
          resize: none;
          touch-action: none;
          overflow: hidden;
          &::placeholder {
            color: #c2c2c2;
          }
        }
        & > textarea,
        &::after {
          font: inherit;
          grid-area: 1 / 1 / 2 / 2;
          padding: 0.48rem 0.8rem;
          min-height: 1.04rem;
          line-height: 1.7;
          font-size: 0.8664rem;
          color: #17181a;
          box-sizing: border-box;
          word-break: break-all;
        }
      }
      .image-preview-box {
        display: inline-block;
        position: relative;
        margin: 0 0.8rem 0.4rem;
        .image {
          width: 5.3336rem;
          height: 5.3336rem;
          background-repeat: no-repeat;
          background-size: cover;
          background-position: 50%;
        }
        .clean-btn {
          position: absolute;
          top: 0.15rem;
          right: 0.2rem;
          cursor: pointer;
        }
      }
    }
    .option-box {
      margin-top: 0.52rem;
      display: flex;
      align-items: center;
      color: #027fff;
      font-size: 0.8664rem;
      .emoji {
        position: relative;
        .emoji-box {
          display: flex;
          align-items: center;
          cursor: pointer;
          .icon {
            width: 1.2rem;
            height: 1.2rem;
            background-repeat: no-repeat;
            background-size: cover;
            background-image: url("data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMiIgaGVpZ2h0PSIyMiIgdmlld0JveD0iMCAwIDIyIDIyIj4KICAgIDxnIGZpbGw9Im5vbmUiIGZpbGwtcnVsZT0iZXZlbm9kZCI+CiAgICAgICAgPHBhdGggZD0iTTEgMWgyMHYyMEgxeiIvPgogICAgICAgIDxwYXRoIGZpbGw9IiMwMjdGRkYiIGZpbGwtcnVsZT0ibm9uemVybyIgZD0iTTExIDE4LjQzOGE3LjQzOCA3LjQzOCAwIDEgMCAwLTE0Ljg3NiA3LjQzOCA3LjQzOCAwIDAgMCAwIDE0Ljg3NnptMCAxLjA2MmE4LjUgOC41IDAgMSAxIDAtMTcgOC41IDguNSAwIDAgMSAwIDE3ek03LjgxMiA5LjkzN2ExLjA2MiAxLjA2MiAwIDEgMCAwLTIuMTI0IDEuMDYyIDEuMDYyIDAgMCAwIDAgMi4xMjV6bTYuMzc1IDBhMS4wNjMgMS4wNjMgMCAxIDAgMC0yLjEyNSAxLjA2MyAxLjA2MyAwIDAgMCAwIDIuMTI1ek0xMSAxNi4yMzJhMy4yNyAzLjI3IDAgMCAwIDMuMjctMy4yN0g3LjczYTMuMjcgMy4yNyAwIDAgMCAzLjI3IDMuMjd6Ii8+CiAgICA8L2c+Cjwvc3ZnPgo=");
          }
          &:hover {
            opacity: 0.8;
          }
        }
      }
      .image-btn {
        flex: 0 0 auto;
        display: flex;
        align-items: center;
        margin-left: 20px;
        cursor: pointer;
        .icon {
          margin-right: 0.2664rem;
          width: 1.2rem;
          height: 1.2rem;
        }
        &:hover {
          opacity: 0.8;
        }
      }
      .upload-file {
        display: none;
      }
      .submit-btn {
        flex: 0 0 auto;
        margin-left: auto;
        padding: 0.4rem 1.04rem;
        font-size: 1rem;
        color: #fff;
        background-color: #027fff;
        border-radius: 2px;
        outline: none;
        border: none;
        cursor: pointer;
        transition: all 0.3s;
        &:hover {
          background-color: #0371df;
        }
        &:disabled {
          opacity: 0.4;
          cursor: default;
        }
      }
    }
  }
  &.reply {
    margin-top: 0.8664rem;
    padding: 0.8rem;
    &.sub-reply {
      background-color: #fff;
      border: 1px solid #f1f1f2;
    }
    .avatar-box {
      display: none;
    }
  }
}
</style>
