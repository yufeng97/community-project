<template>
  <!-- 页头 -->
  <header class="nk-header">
    <el-row>
      <!-- Logo -->
      <el-col :span="8" class="nk-logo">
        <el-image
          style="width: 147px; height: 42px"
          src="http://static.nowcoder.com/images/res/logo/logo-v3.png"
        />
      </el-col>
      <!-- 导航栏 -->
      <el-col :span="6">
        <el-menu
          class="nk-menu"
          router
          mode="horizontal"
          background-color="#343A40"
          text-color="#a5a8ad"
          active-text-color="#eef0f7"
          :default-active="activeIndex"
          @select="handleSelect"
        >
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/message" v-if="store.isLogin"
            >消息</el-menu-item
          >
          <el-menu-item index="/register" v-if="!store.isLogin"
            >注册</el-menu-item
          >
          <el-menu-item index="/login" v-if="!store.isLogin">登录</el-menu-item>
          <el-sub-menu index="/#" v-if="store.isLogin">
            <template #title>
              <el-avatar :size="35" :src="store.avatar"></el-avatar>
            </template>
            <el-menu-item index="/profile">个人主页</el-menu-item>
            <el-menu-item index="/#">账号设置</el-menu-item>
            <el-menu-item index="/" @click="store.logout"
              >退出登录</el-menu-item
            >
          </el-sub-menu>
        </el-menu>
      </el-col>

      <!-- 搜索表单 -->
      <el-form class="search-form" :inline="true" :model="formSearch">
        <el-input v-model="formSearch.detail" placeholder=" "></el-input>
        <el-button class="search-button" color="#343A40" @click="onSubmit"
          >搜索</el-button
        >
      </el-form>
    </el-row>
  </header>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref, toRaw, watch } from "vue";
import userStore from "@/store/user";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const { path } = useRoute();
const store = userStore();
const router = useRouter();

const activeIndex = ref("/");
const handleSelect = () => {};

const formSearch = reactive({
  detail: undefined,
});

console.log("path", path);

watch(
  () => route.path,
  () => {
    console.log("1", route.path);
    activeIndex.value = route.path;
  }
);

const onSubmit = () => {
  console.log("onSubmit");
};
</script>

<style lang="scss">
header {
  background-color: #343a40;
  color: var(--el-text-color-primary);
  // text-align: center;
  height: 58px;
  // flex: 1 1 auto;
  position: sticky;
  z-index: 999;
  top: 0;
}
.nk-logo {
  text-align: right;
  padding-right: 20px;
  display: flex;
  flex-direction: row-reverse;
  align-items: center;
}

.nk-menu {
  // height: 100%;
  // width: 1080px;
}

.search-form {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}

.el-menu--horizontal {
  border-bottom: none;
}
</style>
