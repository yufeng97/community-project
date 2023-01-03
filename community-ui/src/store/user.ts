import { getToken, setToken, removeToken } from "@/utils/token";
import { login, getInfo, logout } from "@/api/login";
import { defineStore } from "pinia";
import { LoginBody, User } from "@/types";
import { ElMessage } from "element-plus";
import { router } from "@/router";

export default defineStore({
  id: "user",
  state: () => ({
    isLogin: false,
    token: getToken(),
    name: "",
    avatar: "",
    userMap: new Map<Number, User>(),
    // roles: [],
    // permissions: []
  }),
  getters: {
  },
  actions: {
    setToken(token: string) {
      this.token = token;
    },
    setName(name: string) {
      this.name = name;
    },
    setAvatar(avatar: string) {
      this.avatar = avatar;
    },
    changeState() {
      this.isLogin = !this.isLogin;
    },
    clearLoginStatus() {
      this.isLogin = false;
      this.setToken("");
      removeToken();
    },
    saveUser(id: number, user: User) {
      this.userMap.set(id, user);
    },
    loadUser(id: number) {
      return this.userMap.get(id);
    },
    // setRoles(roles: never[]) {
    //     this.roles = roles
    // },
    // setPermissions(permissions: never[]) {
    //     this.permissions = permissions
    // },

    // resolve(data) 触发 then((data)=>{})

    login(body: LoginBody) {
      return new Promise<void>((resolve, reject) => {
        login(body)
          .then((res) => {
            console.log(res);
            if (res.code !== 200) {
              resolve(res);
              return;
            }
            this.isLogin = true;
            this.setToken(res.data);
            setToken(res.data);
            this.getInfo();
            resolve(res);
          })
          .catch((error) => {
            console.log(error);
            reject(error);
          });
      });
    },
    getInfo() {
      return new Promise<void>((resolve, reject) => {
        getInfo()
          .then((res) => res.data)
          .then((res) => {
            const user = res.user;
            const avatar =
              user.avatar === ""
                ? "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                : user.avatar;
            this.setName(user.userName);
            this.setAvatar(avatar);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 退出系统
    logout() {
      return new Promise<void>((resolve, reject) => {
        logout(this.token)
          .then(() => {
            ElMessage({
              message: "登录退出。",
              type: "success",
              duration: 2000,
            });
            this.isLogin = false;
            this.setToken("");
            removeToken();
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    loginRequired() {
      return new Promise<void>((resolve, reject) => {
        router.push("/login");
        ElMessage({
          message: "请登录您的账号",
          type: "error",
          duration: 2000,
        });
        this.isLogin = false;
        this.setToken("");
        removeToken();
        resolve();
      });
    },
  },
});
