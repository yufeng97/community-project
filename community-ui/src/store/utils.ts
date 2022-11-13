// 数据存储本地
export function setStorage(key: string, value: any) {
    localStorage.setItem(key, JSON.stringify(value))
}
// 获取本地数据
export function getStorage(key: string) {
    const data = localStorage.getItem(key)
    return data ? JSON.parse(data) : {};
}

export function removeStorage(key: string) {
    localStorage.removeItem(key);
}