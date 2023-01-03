<template>
  <div class="scrollable">
    <Main />
    <Footer />
  </div>
</template>

<script setup lang="ts">
import Footer from "./Footer.vue";
import Main from "./Main.vue";
</script>

<style lang="scss" scoped>
.scrollable {
  display: flex;
  flex-direction: column;
}
</style>
