<template>
  <div class="unscrollable">
    <Main />
  </div>
  <Footer />
</template>

<script setup lang="ts">
import Footer from "./Footer.vue";
import Main from "./Main.vue";
</script>

<style scoped>
main {
  height: calc(100vh - 58px - 148px);
}
</style>
