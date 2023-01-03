<template>
  <vue-eternal-loading
    :load="infiniteHandler"
    position="top"
    :container="container"
  >
    <div slot="no-more">No more Data</div>
    <div slot="no-results">No results Data</div>
  </vue-eternal-loading>

  <div class="chat-box" v-for="i in count" :key="i">
    <!-- <MessageRow /> -->
    <div>count: {{ i }}</div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, PropType, ref } from "vue";
import MessageRow from "./MessageRow.vue";
import { VueEternalLoading, LoadAction } from "@ts-pro/vue-eternal-loading";
import axios from "axios";
import { getUsersByIds } from "@/api/user";
import { getMessages } from "@/api/message";
import { MessageRaw } from "@/types";
import { useRoute, useRouter } from "vue-router";

const props = defineProps({
  talkerId: {
    required: false,
    type: Number,
    default: 2,
  },
  container: {
    required: false,
    type: Object as PropType<HTMLElement | null>,
    default: null,
  },
});

const talkerId = ref(props.talkerId);
const api = "//hn.algolia.com/api/v1/search_by_date?tags=story";
const list = ref([]);

const page = ref(1);
const count = ref(10);
const loading = ref(false);
const route = useRoute();
const router = useRouter();

const handleChange = (id: number) => {
  talkerId.value = id;
  console.log("talkerId changed", talkerId.value);
  fetchMessages(talkerId.value);
}
defineExpose({handleChange})

const infiniteHandler = async ({ loaded }: LoadAction) => {
  // setTimeout(() => {
  //   count.value += 2;
  //   loading.value = false;
  //   list.value.push(...list.value);
  // }, 2000);
  // const res = await axios
  //   .get(api, {
  //     params: {
  //       page: page.value,
  //     },
  //   })
  //   .then(({ data }) => {
  //     console.log("api data", data);
  //     if (data.hits.length) {
  //       return data.hits;
  //     } else {
  //       // $state.complete();
  //       return [];
  //     }
  //   });
  //   list.value.push(...res);
  //   page.value += 1;
  //   loaded(res.length, 10);
};

const fetchMessages = async (talkerId: number) => {
  const rawMessages: MessageRaw[] = await getMessages(Date.now()).then(
    (res) => {
      console.log(res);
      return res.data;
    }
  );
  console.log(rawMessages);
};
getUsersByIds([111, 112]).then((res) => console.log(res));

// onMounted(() => {
//   router.replace(`${route.path}/${props.talkerId}`);
// });
</script>

<style lang="scss" scoped>
.chat-box {
  background-color: white;
  // display: flex;
  flex: 1 1;
  // width: 100%;
}
</style>
