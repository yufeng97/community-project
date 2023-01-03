import ScrollableWrap from "@/components/ScrollableWrap.vue";
import NonScrollableWrap from "@/components/NonScrollableWrap.vue";
import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";

// const routes: Array<RouteRecordRaw> = [
//     {
//         path: '/home',
//         name: 'home',
//         component: () => import('@/views/pages/Home.vue'),
//     },
//     {
//         path: '/letter',
//         name: 'letter',
//         component: () => import('@/views/pages/Letter.vue'),
//     },
//     {
//         path: '/message',
//         name: 'message',
//         component: () => import('@/views/pages/Message.vue'),
//     },
//     {
//         path: '/register',
//         name: 'register',
//         component: () => import('@/views/pages/Register.vue'),
//     },
//     {
//         path: '/login',
//         name: 'login',
//         component: () => import('@/views/pages/Login.vue'),
//     },
//     {
//         path: '/tip/id/:id',
//         name: 'tip',
//         component: () => import('@/views/pages/Activation.vue'),
//     },
//     {
//         path: '/activation/id/:id/activationCode/:activationCode',
//         name: 'activation',
//         component: () => import('@/views/pages/Activation.vue'),
//     },
//     {
//         path: '/post/:id',
//         name: 'post detail',
//         component: () => import('@/views/pages/PostDetail.vue'),
//     },
//     {
//         path: '/user/:id',
//         name: 'profile',
//         component: () => import('@/views/pages/Profile.vue'),
//     },
//     {
//         path: '/user/my-post',
//         name: 'MyPost',
//         component: () => import('@/views/pages/MyPost.vue'),
//     },
//     {
//         path: '/user/my-reply',
//         name: 'MyReply',
//         component: () => import('@/views/pages/MyReply.vue'),
//     },
//     {
//         path: '/',
//         redirect: '/home'
//     },
// ]

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: ScrollableWrap,
    children: [
      {
        path: "/home",
        name: "home",
        component: () => import("@/views/pages/Home.vue"),
      },
      {
        path: "/letter",
        name: "letter",
        component: () => import("@/views/pages/Letter.vue"),
      },
      {
        path: "/tip/id/:id",
        name: "tip",
        component: () => import("@/views/pages/Activation.vue"),
      },
      {
        path: "/activation/id/:id/activationCode/:activationCode",
        name: "activation",
        component: () => import("@/views/pages/Activation.vue"),
      },
      {
        path: "/post/:id",
        name: "post detail",
        component: () => import("@/views/pages/PostDetail.vue"),
      },
      {
        path: "/user/:id",
        name: "profile",
        component: () => import("@/views/pages/Profile.vue"),
      },
      {
        path: "/user/my-post",
        name: "MyPost",
        component: () => import("@/views/pages/MyPost.vue"),
      },
      {
        path: "/user/my-reply",
        name: "MyReply",
        component: () => import("@/views/pages/MyReply.vue"),
      },
      {
        path: "/",
        redirect: "home",
        strict: true,
      },
    ],
  },
  {
    path: "/",
    component: NonScrollableWrap,
    children: [
      {
        path: "/message",
        name: "message",
        component: () => import("@/views/pages/MessagePage.vue"),
      },
      {
        path: "/register",
        name: "register",
        component: () => import("@/views/pages/Register.vue"),
      },
      {
        path: "/login",
        name: "login",
        component: () => import("@/views/pages/Login.vue"),
      },
    ],
  },
];

const router = createRouter({
  strict: true,
  history: createWebHashHistory(),
  routes,
});
router.beforeEach((to, from, next) => {
  console.log("to", to);
  console.log("from", from);

  next();
});

export { router };
