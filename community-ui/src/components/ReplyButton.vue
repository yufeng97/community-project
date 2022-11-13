<template>
  <div class="comment-action action" @mousedown.prevent="">
    <svg aria-hidden="true" viewBox="0 0 20 20" class="icon comment-icon">
      <g fill="none" fill-rule="evenodd">
        <path d="M0 0h20v20H0z" />
        <path
          stroke="#8A93A0"
          stroke-linejoin="round"
          d="M10 17c-4.142 0-7.5-2.91-7.5-6.5S5.858 4 10 4c4.142 0 7.5 2.91 7.5 6.5 0 1.416-.522 2.726-1.41 3.794-.129.156.41 3.206.41 3.206l-3.265-1.134c-.998.369-2.077.634-3.235.634z"
        />
      </g>
    </svg>
    <span class="action-title">{{
      replyCount === 0 ? buttonText : replyCount
    }}</span>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  replyCount: {
    type: Number,
    default: 0,
  },
  buttonText: {
    type: String,
    default: "回复",
  },
  size: {
    type: [Number, String],
    default: "16.5px",
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
