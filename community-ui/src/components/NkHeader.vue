<template>
  <!-- 页头 -->
  <div class="nk-header">
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
          :default-active="activeIndex"
          router
          mode="horizontal"
          background-color="#343A40"
          text-color="#a5a8ad"
          active-text-color="#eef0f7"
          @select="handleSelect"
        >
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/letter" v-if="store.isLogin">消息</el-menu-item>
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
      <el-col :span="10">
        <!-- 搜索表单 -->
        <el-form :inline="true" :model="formSearch" class="search-form">
          <el-form-item>
            <el-input
              v-model="formSearch.detail"
              placeholder=" "
              style="margin-top: 10px"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              class="search-button"
              color="#343A40"
              style="color: white"
              @click="onSubmit"
              >搜索</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref, toRaw, watch } from "vue";
import userStore from "@/store/user";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const { path } = useRoute();
const store = userStore();
const router = useRouter();


console.log("path", path);
// onMounted(() => {
//   // const currentPath = window.location.hash.slice(1) || "/";
//   const currentPath = route.path;
//   console.log(path);

//   console.log("currentPath", currentPath);

//   activeIndex.value = currentPath;
// });

watch(() => route.path, () => {
  console.log("1", route.path);
  activeIndex.value = route.path;
})

const activeIndex = ref("/");
const handleSelect = () => {};

const formSearch = reactive({
  detail: undefined,
});

const onSubmit = () => {
  console.log("onSubmit");
};
</script>
<style scoped>
.nk-logo {
  text-align: right;
  padding-right: 20px;
  padding-top: 8px;
}
.search-form {
  text-align: left;
  margin-left: 35px;
}
.search-button {
  text-align: top;
  margin-top: 10px;
  border: 1px solid var(--el-border-color-base);
}
.nk-menu {
  height: 61px;
  width: 1080px;
}
/* 去掉导航栏下划线 */
/* .el-menu > .el-menu-item {
  border: transparent !important;
} */
</style>
