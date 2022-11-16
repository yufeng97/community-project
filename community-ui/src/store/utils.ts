// 数据存储本地
export function setStorage(key: string, value: any): void {
    localStorage.setItem(key, JSON.stringify(value))
}
// 获取本地数据
export function getStorage(key: string) {
    const data = localStorage.getItem(key)
    console.log("data", data);
    return data ? JSON.parse(data) : "";
}

export function removeStorage(key: string): void {
    localStorage.removeItem(key);
}