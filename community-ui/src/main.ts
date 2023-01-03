import { createApp, toRaw } from 'vue'
import App from '@/App.vue'
import { router } from '@/router'
import ElementPlus from 'element-plus'
import { createPinia, PiniaPluginContext } from "pinia"
import { setStorage, getStorage } from './store/utils'
import 'element-plus/dist/index.css'
import '@/assets/css/global.css'
// import VueVirtualScroller from 'vue-virtual-scroller'
// import 'vue-virtual-scroller/dist/vue-virtual-scroller.css'
// 全局注册 虚拟滚动的组件

import VirtualList from 'vue3-virtual-scroll-list'





// import UndrawUi from 'undraw-ui'
// import 'undraw-ui/dist/style.css'


// // 数据存储本地
// const setStorage = (key: string, value: any) => {
//     localStorage.setItem(key, JSON.stringify(value))
// }
// // 获取本地数据
// const getStorage = (key: string) => {
//     const data = localStorage.getItem(key)
//     return data ? JSON.parse(data) : {};
// }

const piniaPlugin = (context: PiniaPluginContext) => {
    const { store } = context;
    // $subscribe state值发生变化时会执行传入的回调
    store.$subscribe(() => {
        // 每次修改值的时候更新localStorage数据
        setStorage(`pinia-${store.$id}`, toRaw(store.$state))
    })
    // 每次构建项目的时候从本地存储取值
    const data = getStorage(`pinia-${store.$id}`)
    return {
        ...data
    }
}


const app = createApp(App)

app.use(router)

app.use(ElementPlus)
app.component('virtual-list', VirtualList)
const store = createPinia()
store.use(piniaPlugin)
app.use(store)

// app.use(VueVirtualScroller)

// app.use(createPinia())

// app.use(UndrawUi)

app.mount('#app')
