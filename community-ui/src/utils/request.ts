import axios from "axios";
import errorCode from "@/utils/errorCode";
// import { getToken } from "@/utils/cookies";
import { getToken } from "@/utils/token";
import { tansParams } from "@/utils/tansParams";
import { ElMessage } from "element-plus";
import { router } from "@/router";
axios.defaults.headers.common["Content-Type"] =
  "application/json;charset=utf-8";

// 创建axios
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // 超时
  timeout: 1000,
});

// 添加请求拦截器
service.interceptors.request.use(
  (config) => {
    if (!config?.headers) {
      throw new Error(
        `Expected 'config' and 'config.headers' not to be undefined`
      );
    }

    if (getToken()) {
      // config.headers["Authorization"] = "Bearer " + getToken();
      config.headers["Authorization"] = getToken();
    }

    //get请求映射params参数
    if (config.method === "get" && config.params) {
      var url = config.url + "?" + tansParams(config.params);
      url = url.slice(0, -1);
      config.params = {};
      config.url = url;
    }

    return config;
  },
  (error) => {
    console.log(error);
    return Promise.reject(error);
  }
);

// 添加响应拦截器
service.interceptors.response.use(
  (res) => {
    // 未设置状态码则默认成功状态

    const code: number = res.data.code || 200;
    const status: number = res.status;
    // 获取错误信息
    const msg: string =
      errorCode.get(code) || res.data.message || errorCode.get(0);

    // 二进制数据则直接返回
    if (
      res.request.responseType === "blob" ||
      res.request.responseType === "arraybuffer"
    ) {
      return res.data;
    }
    if (status === 401) {
      return Promise.reject("无效的会话，或者会话已过期，请重新登录。");
    } else if (status === 403) {
      return Promise.reject("权限不足");
    } else if (status === 500) {
      return Promise.reject(new Error(msg));
    } else if (status !== 200) {
      return Promise.reject("error");
    }

    if (status === 200 || code === 200) {
      return res.data;
    }
  },
  (error) => {
    console.log("response error", error);
    const status: number = error.response.status;
    let { message } = error;
    if (status === 401) {
      ElMessage({
        message: "无效的会话，或者会话已过期，请重新登录。",
        type: "warning",
        duration: 2000,
      });
      sessionStorage.clear(); //清除缓存
      localStorage.clear(); //清除缓存
      return Promise.reject("无效的会话，或者会话已过期，请重新登录。");
    }

    if (message == "Network Error") {
      message = "后端接口连接异常";
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    } else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    return Promise.reject(error);
  }
);

export default service;
