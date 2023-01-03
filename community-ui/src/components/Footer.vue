<template>
  <!-- 尾部 -->
  <footer class="nk-footer">
    <el-row>
      <el-col :span="10" class="qrcode-info">
        <!-- 二维码 -->
        <el-image
          class="qrcode"
          src="https://uploadfiles.nowcoder.com/app/app_download.png"
        />
      </el-col>
      <!-- 公司信息 -->
      <el-col :span="14" class="detail-info">
        <!-- 上半部链接 -->
        <el-row>
          <a href="#" class="link-info">关于我们</a>
          <a href="#" class="link-info">加入我们</a>
          <a href="#" class="link-info">意见反馈</a>
          <a href="#" class="link-info">企业服务</a>
          <a href="#" class="link-info">联系我们</a>
          <a href="#" class="link-info">免责声明</a>
          <a href="#" class="link-info">友情链接</a>
        </el-row>
        <!-- 下半部信息 -->
        <table class="company-info">
          <tbody>
            <tr>
              <td>
                公司地址：北京市朝阳区大屯路东金泉时代3-2708北京牛客科技有限公司
              </td>
            </tr>
            <tr>
              <td>
                联系方式：010-60728802(电话)&nbsp;&nbsp;&nbsp;&nbsp;admin@nowcoder.com
              </td>
            </tr>
            <tr>
              <td>牛客科技©2018 All rights reserved</td>
            </tr>
          </tbody>
        </table>
        <table class="company-info">
          <tbody>
            <tr>
              <td>京ICP备14055008号-4&nbsp;&nbsp;&nbsp;&nbsp;</td>
              <el-image
                style="width: 18px"
                src="http://static.nowcoder.com/company/images/res/ghs.png"
              />
              <td>京公网安备 11010502036488号</td>
            </tr>
          </tbody>
        </table>
      </el-col>
    </el-row>
  </footer>
  <!-- </footer> -->
</template>
<script setup lang="ts"></script>
<style lang="scss" scoped>
a:link {
  text-decoration: none;
}
a:visited {
  text-decoration: none;
}
a:hover {
  text-decoration: none;
  color: #c1c1c9;
}
a:active {
  text-decoration: none;
  color: #c1c1c9;
}

footer {
  background-color: #343a40;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 22px;
  height: 148px;
  /* display: flex;
  flex-direction: column;
  align-items: center; */
}
.qrcode-info {
  padding-right: 3rem;
  display: flex;
  flex-direction: row-reverse;
  align-items: center;
}
.qrcode {
  width: 128px;
  border-radius: 4px;
  border: 4px solid rgb(255, 255, 255);
}
.detail-info {
  border-left: 1px solid #888;
  text-align: left;
  padding-left: 18px;
}
.link-info {
  font-size: var(--el-font-size-extra-small);
  color: #eef0f7;
  line-height: 35px;
  padding-left: 16px;
  padding-right: 16px;
}
.company-info {
  font-size: var(--el-font-size-extra-small);
  color: #a5a8ad;
  padding-left: 12px;
}
</style>
