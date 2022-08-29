<template>
  <el-tabs
    type="card"
    v-model="activateLabel"
    class="nk-tabs"
    @tab-click="handleClick"
    v-loading="loading"
  >
    <el-tab-pane v-for="entry in tabLabel.entries()" :label="entry[0]" :name="entry[1]">
      asd
    </el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">import { Ref, ref, watch } from 'vue';
import type { TabsPaneContext } from 'element-plus'
import { useRoute, useRouter } from 'vue-router';

const tabLabel = ref<Map<String, String>>(new Map([
  ["个人信息", "profile"],
  ["我的帖子", "my-post"],
  ["我的回复", "my-reply"]
]))

console.log(tabLabel.value);


const activateLabel = ref("my-post")


const loading = ref(false);

const route = useRoute();

const router = useRouter()

watch(() => route.path, () => {
  activateLabel.value = route.path;
})

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
  // route.path = tab.index as string
  console.log(tab);
  
  router.push("/user/" + tab.props.name)
};
</script>

<style scoped>

</style>