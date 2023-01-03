<template>
  <main>
    <div class="fluid" />
    <div class="fixed">
      <router-view />
    </div>
    <div class="fluid" />
  </main>
</template>

<script setup lang="ts"></script>

<style lang="scss" scoped>
main {
  background-color: #eee;
  color: var(--el-text-color-primary);
  text-align: left;
  overflow: auto;
  flex: auto;
  // line-height: 20px;
  // min-height: 760px;
  display: -webkit-flex;
  display: -moz-flex;
  display: -o-flex;
  display: -ms-flex;
  .fluid {
    flex: 1 1;
    // max-width: 15%;
  }
  .fixed {
    flex: 1 1 100vh;
    // overflow: auto;
  }
}
</style>
