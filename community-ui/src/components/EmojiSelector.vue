<template>
  <div class="emoji-selector" @mousedown.prevent @mousedown.stop>
    <div class="triangle" />
    <div class="emoji-content">
      <div class="category">
        <div
          v-for="(item, i) in currentEmojis"
          :key="`emoji-${i}`"
          class="item"
          @click="$emit('choose', item)"
        >
          {{ item }}
        </div>
      </div>
    </div>
    <div class="next-page">
      <div
        v-for="cat in Object.keys(emojis)"
        :key="cat"
        :class="{ active: currentCat === cat }"
        @click="currentCat = cat"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
const currentCat = "FrequentlyUsed";
const emojis = {
  FrequentlyUsed: [
    "ð",
    "ð",
    "ð",
    "ðģ",
    "ð",
    "ð",
    "ð",
    "ð",
    "ð",
    "ð",
    "ð ",
    "ð",
    "ðĨ",
    "ð­",
    "ð",
    "ðĄ",
    "âĪ",
    "ð",
    "ðĢ",
    "ð",
    "ð",
    "ð·",
    "ð",
    "ð",
    "ð",
    "ðĒ",
    "ð",
    "ðĻ",
    "ð°",
    "ðē",
    "ð",
    "ðą",
    "ðŠ",
    "ð",
    "ð",
    "ð",
    "ðŧ",
    "ð",
    "ð§",
    "ðĶ",
    "ðĐ",
    "ðĻ",
    "ðķ",
    "ðą",
    "ð",
    "â",
    "â",
    "ðŠ",
    "ð",
    "ð",
    "ð",
    "ð",
    "ð",
    "ðĐ",
  ],
  Symbols0: [
    "ðĪ",
    "ð",
    "ðĪ",
    "ðĐâðŧ",
    "ðĻâðŧ",
    "ð",
    "ð­",
    "ðĻ",
    "ðĪŠ",
    "ð",
    "ðĪ",
    "ðĩ",
    "ð",
    "ðĪŽ",
    "ð",
    "ðđ",
    "ð",
    "ðĻðģ",
    "ðŪ",
    "ð",
    "â",
    "ð",
    "ðĨ",
    "ð",
    "ð",
    "ð―",
    "ðĪ",
    "â",
    "ðĪ",
    "ðģïļâð",
    "ðĐ",
    "ðĪ",
    "ÂŪ",
    "ÂĐ",
    "ðŊ",
    "âĒ",
    "ðŧ",
    "ð",
    "ð",
    "â",
    "âĻ",
    "ð",
    "ðĪģ",
    "ð",
    "ðĢ",
    "ðĻ",
    "ð§",
    "ðļ",
    "ðĪ",
    "ðļ",
    "ð",
    "â―",
    "ðŪ",
    "ð",
  ],
  Symbols1: [
    "ð",
    "ðĶ",
    "ð",
    "ð",
    "â",
    "ð",
    "ð",
    "âĄ",
    "â",
    "â",
    "ð",
    "ðŧ",
    "ð",
    "ð",
    "ð",
    "ð",
    "ðđ",
    "â",
    "ð",
    "ð",
    "ðš",
    "ð",
    "ðą",
    "ð ",
    "ð",
    "ð",
    "â―",
    "ðĢ",
    "ð",
    "ð",
    "ðĪŪ",
    "ð",
    "ðŋ",
  ],
};

const currentEmojis = computed(() => {
  return emojis[currentCat];
});
</script>

<style lang="scss" scoped>
.emoji-selector {
  padding: 0.8rem;
  position: absolute;
  top: 2.24rem;
  z-index: 1;
  bottom: 0;
  width: 19rem;
  height: 14rem;
  border-radius: 2px;
  background-color: #fff;
  box-shadow: 0 5px 18px 0 rgba(0, 0, 0, 0.16);
  box-sizing: content-box;
  .triangle {
    position: absolute;
    top: -0.56rem;
    left: 14%;
    width: 0;
    height: 0;
    transform: translate(-50%, -50%);
    border: 0.64rem solid transparent;
    border-bottom-color: #fff;
  }
  .emoji-content {
    height: 100%;
    overflow: auto;
    margin-bottom: 10px;
    .category {
      max-width: 19rem;
      max-height: 13rem;
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      justify-content: flex-start;
      overflow: hidden;
      .item {
        width: calc(19rem / 9);
        height: calc(13rem / 6);
        font-size: 1.25rem;
        text-align: center;
        line-height: calc(10rem / 6);
        cursor: pointer;
        &:hover {
          font-size: 1.6rem;
        }
      }
    }
  }
  .next-page {
    list-style: none;
    position: absolute;
    display: flex;
    justify-content: center;
    align-items: center;
    bottom: 10px;
    left: 50%;
    transform: translate(-50%, -50%);
    div {
      list-style: none;
      margin: 0 5px;
      width: 6px;
      height: 6px;
      border-radius: 100%;
      background-color: #f0f0f0;
      cursor: pointer;
      &.active {
        cursor: default;
        background-color: #d8d8d8;
      }
    }
  }
}
</style>
