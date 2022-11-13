<template>
  <el-tabs
    type="card"
    class="nk-tabs"
    @tab-click="handleClick"
    v-loading="loading"
    v-if="!loading"
  >
    <el-tab-pane v-for="label in tabLabel" :label="label">
      <div v-for="item in postInfos">
        <nk-home-card :post-info="item" />
        <el-divider style="margin: 12px 0;"/>
      </div>

      <el-pagination
        background
        layout="prev, pager, next"
        v-model:currentPage="page.pageNum"
        :page-size="pageSize"
        :total="totalRows"
        @current-change="handleCurrentChange"
      />
    </el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
import { reactive, ref, Ref, PropType } from "vue";
import { Page, PostInfo } from "@/types";
import NkHomeCard from "@/components/NkHomeCard.vue";
import { getHomeInfoList } from "@/api/home";
import { useRoute, useRouter } from "vue-router";

//初始化
const postInfos = ref<PostInfo[]>([]);

const tabLabel: Ref<string[]> = ref(["最新", "最热"]);
const map = new Map([
  ["最新", "createTime"],
  ["最热", "commentCount"],
]);

const pageNum = ref(1);
const pageSize: number = 7;
const orderBy: string = "createTime";
const isAsc: string = "desc";
const totalRows = ref(0);
const loading = ref(true);
const page = reactive({
  pageNum,
  pageSize,
  orderBy,
  // isAsc
});

const route = useRoute()
const router = useRouter()

const handleClick = (tab: any, event: unknown) => {
  page.orderBy = map.get(tab.props.label) || "createTime";
  handleCurrentChange();
};
const handleCurrentChange = (currentPage: number = 1) => {
  getHomeInfoList({
    pageNum: page.pageNum,
    pageSize: page.pageSize,
    // isAsc: page.isAsc,
    orderBy: page.orderBy,
  }).then((res) => {
    totalRows.value = res.data.total;
    postInfos.value = res.data.rows;

    router.replace({ path: route.path, query: { page: currentPage } });

    loading.value = false;
  });
};

//初始化
handleCurrentChange();
</script>

<style scoped></style>
