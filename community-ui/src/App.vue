<template>
  <Index></Index>
</template>
<script setup lang="ts">
import { onBeforeMount } from "vue";
import Index from "./views/Index.vue";
import userStore from "@/store/user";

const store = userStore();

const getAccess = () => {
  if (store.isLogin) {
    store.getInfo()
    .catch((err) => {
      store.clearLoginStatus();
    });
  }
};

onBeforeMount(() => {
  getAccess();
});
</script>
<style></style>
