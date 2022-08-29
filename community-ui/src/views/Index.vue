<template>
  <!-- Element Plus 布局容器 -->
  <el-container>
    <!-- 头部 -->
    <el-header height="60px">
      <nk-header></nk-header>
    </el-header>
    <!-- 页面主内容 交给路由转发 -->
    <el-main>
      <div class="nk-letter nk-main-repair">
        <el-row>
          <el-col :span="6"></el-col>
          <el-col :span="12">
            <router-view></router-view>
          </el-col>
          <el-col :span="6"></el-col>
        </el-row>
      </div>
    </el-main>
    <!-- 尾部 -->
    <el-footer height="179px">
      <nk-footer></nk-footer>
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
import NkFooter from "../components/NkFooter.vue";
import NkHeader from "../components/NkHeader.vue";
</script>

<style>
.el-header {
  background-color: #343a40;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 60px;

  /* 固定在顶部 */
  position: fixed;
  z-index: 10;
  top: 0;
  width: 100%;
}
.el-footer {
  background-color: #343a40;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 22px;
  /* 跟随在底部 */
  position: relative;
  bottom: 0;
  width: 100%;
  min-width: 1080px;
}
.el-main {
  background-color: #eee;
  color: var(--el-text-color-primary);
  text-align: left;
  line-height: 40px;
  min-height: 761px;
}
/* body > .el-container {
} */
</style>
