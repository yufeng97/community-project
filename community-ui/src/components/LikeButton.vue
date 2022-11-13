<template>
  <div
    class="like-action action"
    :class="{ active: liked }"
    @click.stop="handleLikeClick"
  >
    <svg aria-hidden="true" viewBox="0 0 20 20" class="icon like-icon">
      <g fill="none" fill-rule="evenodd">
        <path d="M0 0h20v20H0z" />
        <path
          :stroke="liked ? '#37C700' : '#8A93A0'"
          stroke-linejoin="round"
          :fill="liked ? '#37c700' : 'none'"
          d="M4.58 8.25V17h-1.4C2.53 17 2 16.382 2 15.624V9.735c0-.79.552-1.485 1.18-1.485h1.4zM11.322 2c1.011.019 1.614.833 1.823 1.235.382.735.392 1.946.13 2.724-.236.704-.785 1.629-.785 1.629h4.11c.434 0 .838.206 1.107.563.273.365.363.84.24 1.272l-1.86 6.513A1.425 1.425 0 0 1 14.724 17H6.645V7.898C8.502 7.51 9.643 4.59 9.852 3.249A1.47 1.47 0 0 1 11.322 2z"
        />
      </g>
    </svg>
    <span v-show="likesCount" class="action-title">{{ likesCount }}</span>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, ref } from "vue";
import userStore from "@/store/user";
import { useRouter } from "vue-router";

const router = useRouter();
const store = userStore();

const props = defineProps({
  id: {
    type: [String, Number],
    required: true,
  },
  liked: {
    type: Boolean,
    default: false,
  },
  likesCount: {
    type: Number,
    // required: true,
    default: 0,
  },
  size: {
    type: [Number, String],
    default: "16.5px",
  }
});

const emit = defineEmits(["like-click"]);

const liked = ref(props.liked);
const likesCount = ref(props.likesCount);

let timer: NodeJS.Timeout | null = null;

const triggerLikeClick = () => {
  if (liked.value) {
    likesCount.value--;
  } else {
    likesCount.value++;
  }
  liked.value = !liked.value;
};

const handleLikeClick = () => {
  if (!store.isLogin) {
    // router.push("/login");
    clearTimeout(Number(timer));
    timer = null;
    store.loginRequired();
    return;
  }
  triggerLikeClick();
  if (timer) {
    clearTimeout(timer);
  }
  timer = setTimeout(() => {
    timer = null;
    emit("like-click", liked.value);
  }, 3000);
};

onBeforeUnmount(() => {
  if (timer) {
    emit("like-click", liked.value);
    clearTimeout(Number(timer));
    timer = null;
  }
});
</script>

<style lang="scss" scoped>
.action {
  display: flex;
  align-items: center;
  margin-left: 0.4rem;
  cursor: pointer;
  &:hover {
    opacity: 0.8;
  }
  &.active {
    color: #37c700;
  }
  .icon {
    min-width: v-bind(size);
    min-height: v-bind(size);
    width: 0.8rem;
    height: 0.8rem;
  }
  .action-title {
    margin-left: 0.2rem;
    font-size: 0.8rem;
  }
}
</style>
