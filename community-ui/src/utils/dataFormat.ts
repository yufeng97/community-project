export function dataFormat(value: string) {
    var year = value.substring(0, 4)
    var month = value.substring(5, 2)
    var day = value.substring(8, 2)
    var hour = value.substring(11, 2)
    var min = value.substring(14, 2)
    var second = value.substring(17, 2)
    return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + second
}


export function formatTime(time: string, local = false) {
    const d = new Date(time);
    if (local) {
      return d.toString();
    }
    const now = Date.now();
    const diff = (now - d.getTime()) / 1000;
    switch (true) {
      case diff < 30:
        return "刚刚";
      case diff < 3600:
        return Math.ceil(diff / 60) + "分钟前";
      case diff < 3600 * 24:
        return Math.ceil(diff / 3600) + "小时前";
      case diff < 3600 * 24 * 30:
        return Math.floor(diff / 3600 / 24) + "天前";
      case diff < 3600 * 24 * 365:
        return Math.floor(diff / 3600 / 24 / 30) + "月前";
      default:
        return Math.floor(diff / 3600 / 24 / 365) + "年前";
    }
  };