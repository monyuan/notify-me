<script>
import axios from "axios";

const http = axios.create({
  baseURL: "/",
  timeout: 1000,
});

export default {
  data() {
    return {
      list: [],
      update: false,
      saveTip: false,
      success: false,
      tipText: '',
      data: {
        apiKey: "",
        channel: "",
        siteUrl: "",
        status: false,
        commentStatus: false,
        postStatus: false,
        wechatStatus: false,
        commentAuditsStatus: false,
        postAuditsStatus: false,
        apiVersion: "org.xiqi.notifyme/v1alpha1",
        kind: "NotifyMe",
        metadata: {
          name: "notify-config"
        }
      }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {

    getSiteUrl() {
      this.data.siteUrl = "https://" + window.location.host
    },

    getData() {
      http.get("/apis/org.xiqi.notifyme/v1alpha1/notifymes")
        .then((res) => {
          if (res.data.items.length > 0) {
            this.data = res.data.items[0]
            this.update = true
          } else {
            this.getSiteUrl()
          }
        });
    },
    createData() {
      if (this.update) {
        http.put("/apis/org.xiqi.notifyme/v1alpha1/notifymes/notify-config",
          this.data
        ).then((response) => {
          if (response.status == 200) {
            this.saveTip = true
            this.success = true
            this.tipText = "更新成功"
          }
          this.getData();
        }).catch(res => {
          this.success = false
          this.tipText = "保存失败,请检查API密钥和通道ID是否正确"
        }).finally(() => {
          this.saveTip = true
        })
      } else {
        http.post("/apis/org.xiqi.notifyme/v1alpha1/notifymes", this.data)
          .then((response) => {
            if (response.status == 201) {
              this.saveTip = true
              this.update = true
              this.success = true
              this.tipText = "保存成功"
            }
          }).catch(res => {
          this.success = false
          this.tipText = "保存失败,请检查API密钥和通道ID是否正确"
        }).finally(() => {
          this.saveTip = true
        })
      }
    }
  }
}


</script>

<template>
  <section class="todoapp">
    <div class="toast-container">
      <div class="toast-wrapper" id="toast-0">
        <div class="toast-body" v-if="saveTip">
          <div class="toast-icon" v-if="success">
            <svg viewBox="0 0 24 24" width="1.2em" height="1.2em" class="text-green-500">
              <path fill="currentColor"
                    d="M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10s-4.477 10-10 10Zm0-2a8 8 0 1 0 0-16a8 8 0 0 0 0 16Zm-.997-4L6.76 11.757l1.414-1.414l2.829 2.829l5.657-5.657l1.414 1.414L11.003 16Z">
              </path>
            </svg>
          </div>
          <div class="toast-icon" v-if="!success">
            <svg t="1704278585303" class="icon" viewBox="0 0 1028 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="4204" width="1.2em" height="1.2em">
              <path
                d="M875.086452 153.730058C676.053818-45.302575 353.260522-45.302575 154.128323 153.730058s-199.032634 521.825929 0 720.958129 521.825929 199.032634 720.958129 0 199.032634-521.825929 0-720.958129zM725.836868 725.438604c-9.757478 9.757478-25.488922 9.757478-35.246399 0L514.557604 549.405739 338.624306 725.438604c-9.757478 9.757478-25.488922 9.757478-35.2464 0s-9.757478-25.488922 0-35.2464l176.032865-176.032864-176.032865-175.933299c-9.757478-9.757478-9.757478-25.488922 0-35.246399 9.757478-9.757478 25.488922-9.757478 35.2464 0l176.032864 176.032865 176.032865-176.032865c9.757478-9.757478 25.488922-9.757478 35.246399 0 9.757478 9.757478 9.757478 25.488922 0 35.246399L549.804004 514.15934 725.836868 690.192204c9.657912 9.757478 9.657912 25.488922 0 35.2464z"
                fill="#F56C6C" p-id="4205"></path>
            </svg>
          </div>
          <div class="toast-content">
            <div class="toast-description">
              {{ tipText }}
            </div>
          </div>
          <div class="toast-control" @click="saveTip = false">
            <svg viewBox="0 0 24 24" width="1.2em" height="1.2em" class="">
              <path fill="currentColor"
                    d="m12 10.587l4.95-4.95l1.415 1.414l-4.95 4.95l4.95 4.95l-1.415 1.414l-4.95-4.95l-4.95 4.95l-1.413-1.415l4.95-4.95l-4.95-4.95L7.05 5.638l4.95 4.95Z">
              </path>
            </svg>
          </div>
        </div><!---->
      </div>
    </div>
    <div class="flex items-center justify-between bg-white p-4 h-14">
      <div class="min-w-0 flex-1 self-center">
        <h2 class="flex items-center truncate text-xl font-bold text-gray-800">
          <svg t="1704109280821" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
               p-id="12080" width="30" height="30">
            <path
              d="M469.333333 217.6V170.666667h85.333334v46.933333c136.533333 21.333333 243.2 132.266667 256 273.066667V768H213.333333v-277.333333c12.8-140.8 119.466667-256 256-273.066667zM298.666667 682.666667h426.666666v-170.666667c0-119.466667-93.866667-213.333333-213.333333-213.333333s-213.333333 93.866667-213.333333 213.333333v170.666667z m85.333333 128h256v85.333333H384v-85.333333z"
              fill="#000000" p-id="12081"></path>
          </svg>
          <span>通知我</span>
        </h2>
      </div>
    </div>
    <div class="m-0 md:m-4">
      <div class="card-wrapper">
        <div class="card-header">
          <div class="tabbar-outline tabbar-direction-row tabbar-wrapper w-full !rounded-none">
            <div class="tabbar-items">
              <div class="tabbar-item-active tabbar-item">
                <div class="tabbar-item-label text-center">设置</div>
              </div>
            </div>
          </div>
        </div>
        <div class="!p-0 card-body">
          <div style="padding: 40px;">
            <div class="tip">
              🎈 通过AnPush的接口实现的通知推送, 免费套餐已足够博客使用。 <a href="https://anpush.com/welcome?halo" target="_blank"
                                                     style="text-decoration: underline;color: #000000;"> 获取密钥 > </a>
              <br>
              <div>根据官网描述可以推送至12个应用，我测试了微信、企业微信、微信测试号、飞书、钉钉都没有问题</div>
            </div>
            <section class="main">
              <div
                class="nm-title-container formkit-outer formkit-disabled:opacity-50 py-4 first:pt-0 last:pb-0 transition-all">
                <div class="nm-checkbox-title">
                  <svg style="display: inline" class="icon" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="4565" width="20" height="20">
                    <path
                      d="M822.272 523.776H202.24c-13.824 0-25.088-11.264-25.088-25.088s11.264-25.088 25.088-25.088h620.032c13.824 0 25.088 11.264 25.088 25.088 0.512 13.824-10.752 25.088-25.088 25.088z"
                      fill="#000000" p-id="4566"></path>
                    <path
                      d="M484.352 817.664V197.632c0-13.824 11.264-25.088 25.088-25.088s25.088 11.264 25.088 25.088v620.032c0 13.824-11.264 25.088-25.088 25.088s-25.088-11.264-25.088-25.088z"
                      fill="#000000" p-id="4567"></path>
                    <path
                      d="M509.952 867.84c-198.656 0-360.448-161.792-360.448-360.448s161.792-360.448 360.448-360.448S870.4 308.736 870.4 507.392s-161.792 360.448-360.448 360.448z m0-671.744c-171.52 0-311.296 139.776-311.296 311.296s139.776 311.296 311.296 311.296 311.296-139.776 311.296-311.296-139.776-311.296-311.296-311.296z"
                      fill="#000000" p-id="4568"></path>
                    <path
                      d="M456.704 856.064c-325.632-400.384-0.512-696.32 2.56-699.392l32.768 36.864-16.384-18.432 16.384 18.432c-12.288 10.752-292.864 268.288 2.56 631.808l-37.888 30.72zM571.904 856.064l-37.888-30.72c295.424-363.008 14.848-620.544 2.56-631.808l32.768-36.864c3.072 3.072 328.192 299.52 2.56 699.392z"
                      fill="#000000" p-id="4569"></path>
                  </svg>
                  <span class="icon-title">站点地址</span>
                </div>
                <div
                  class="formkit-inner inline-flex items-center w-full relative box-border border border-gray-300 formkit-invalid:border-red-500 h-9 rounded-base overflow-hidden focus-within:border-primary focus-within:shadow-sm sm:max-w-lg transition-all">
                  <input
                    class="nm-input formkit-input bg-white resize-none w-full text-black block transition-all h-full px-3 text-sm"
                    type="text" v-model="data.siteUrl" placeholder="eg: https://xiqi.org"/>
                </div>
              </div>
              <div
                class="nm-title-container formkit-outer formkit-disabled:opacity-50 py-4 first:pt-0 last:pb-0 transition-all">
                <div class="nm-checkbox-title">
                  <svg style="display: inline" class="icon" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="10134" width="24" height="24">
                    <path
                      d="M256 426.666667h512v426.666666H256V426.666667z m85.333333 85.333333v256h341.333334v-256H341.333333z m0-42.666667H256v-42.666666h85.333333V341.333333c0-93.866667 76.8-170.666667 170.666667-170.666666s170.666667 76.8 170.666667 170.666666v85.333334h85.333333v42.666666h-170.666667V341.333333c0-46.933333-38.4-85.333333-85.333333-85.333333s-85.333333 38.4-85.333333 85.333333v128H341.333333z m170.666667 213.333334c-25.6 0-42.666667-17.066667-42.666667-42.666667s17.066667-42.666667 42.666667-42.666667 42.666667 17.066667 42.666667 42.666667-17.066667 42.666667-42.666667 42.666667z"
                      fill="#000000" p-id="10135"></path>
                  </svg>
                  <span class="icon-title">API密钥</span>
                </div>
                <div
                  class="formkit-inner inline-flex items-center w-full relative box-border border border-gray-300 formkit-invalid:border-red-500 h-9 rounded-base overflow-hidden focus-within:border-primary focus-within:shadow-sm sm:max-w-lg transition-all">
                  <input
                    class="nm-input formkit-input bg-white resize-none w-full text-black block transition-all h-full px-3 text-sm"
                    type="password" v-model="data.apiKey" placeholder="AnPush后台获取"/>
                </div>
              </div>
              <div
                class="nm-title-container formkit-outer formkit-disabled:opacity-50 py-4 first:pt-0 last:pb-0 transition-all">
                <div class="nm-checkbox-title">
                  <svg style="display: inline" viewBox="0 0 24 24" width="20px" height="20px">
                    <path fill="currentColor"
                          d="M12 2c5.52 0 10 4.48 10 10s-4.48 10-10 10S2 17.52 2 12S6.48 2 12 2Zm0 18c4.42 0 8-3.58 8-8s-3.58-8-8-8s-8 3.58-8 8s3.58 8 8 8Zm1-8v4h-2v-4H8l4-4l4 4h-3Z">
                    </path>
                  </svg>
                  <span class="icon-title">通道ID</span>
                </div>
                <div
                  class="formkit-inner inline-flex items-center w-full relative box-border border border-gray-300 formkit-invalid:border-red-500 h-9 rounded-base overflow-hidden focus-within:border-primary focus-within:shadow-sm sm:max-w-lg transition-all">
                  <input
                    class="nm-input formkit-input bg-white resize-none w-full text-black block transition-all h-full px-3 text-sm"
                    type="text" v-model="data.channel" placeholder="有多个就用英文逗号隔开"/>
                </div>
              </div>
              <div
                class="nm-title-container formkit-outer formkit-disabled:opacity-50 py-4 first:pt-0 last:pb-0 transition-all">
                <div class="nm-checkbox-title">
                  <svg style="display: inline" viewBox="0 0 24 24" width="20px" height="20px">
                    <path fill="currentColor"
                          d="M12 2c5.52 0 10 4.48 10 10s-4.48 10-10 10S2 17.52 2 12S6.48 2 12 2Zm0 18c4.42 0 8-3.58 8-8s-3.58-8-8-8s-8 3.58-8 8s3.58 8 8 8Zm1-8v4h-2v-4H8l4-4l4 4h-3Z">
                    </path>
                  </svg>
                  <span class="icon-title">通道是否是微信公众号</span>
                  <div style="font-size: 13px;font-weight: normal;color: #8077a4;padding: 5px">
                    ❗️ 如果你用的通道是微信公众号一定要勾选[其他的不要勾选]，因为微信公众号的模板消息标题长度固定，超出长度无法发送！
                  </div>
                </div>
                <div style="padding: 10px"
                     class="formkit-inner inline-flex items-center w-full relative box-border border border-gray-300 formkit-invalid:border-red-500 h-9 rounded-base overflow-hidden focus-within:border-primary focus-within:shadow-sm sm:max-w-lg transition-all">
                  <input class="toggle-all" type="checkbox" :checked="data.wechatStatus"
                         v-model="data.wechatStatus"/>
                  <label for="toggle-all"><span class="nm-checkbox-text"
                                                style="font-size: 14px;color: red;vertical-align: 1px">
                    是</span></label>
                </div>
              </div>
              <div
                class="nm-title-container formkit-outer formkit-disabled:opacity-50 py-4 first:pt-0 last:pb-0 transition-all">
                <div class="nm-checkbox-title">
                  <svg style="display: inline" viewBox="0 0 24 24" width="20px" height="20px">
                    <path fill="currentColor"
                          d="M2 12c0-.865.11-1.704.316-2.504A3 3 0 0 0 4.99 4.867a9.99 9.99 0 0 1 4.335-2.506a3 3 0 0 0 5.348 0a9.99 9.99 0 0 1 4.335 2.506a3 3 0 0 0 2.675 4.63c.206.8.316 1.638.316 2.503c0 .864-.11 1.703-.316 2.503a3 3 0 0 0-2.675 4.63a9.99 9.99 0 0 1-4.335 2.505a3 3 0 0 0-5.348 0a9.99 9.99 0 0 1-4.335-2.505a3 3 0 0 0-2.675-4.63C2.11 13.703 2 12.864 2 12Zm4.804 3c.63 1.091.81 2.346.564 3.524c.408.29.842.541 1.297.75A4.993 4.993 0 0 1 12 18c1.26 0 2.438.471 3.335 1.274c.455-.209.889-.46 1.297-.75A4.993 4.993 0 0 1 17.196 15a4.993 4.993 0 0 1 2.77-2.25a8.142 8.142 0 0 0 0-1.5A4.993 4.993 0 0 1 17.196 9a4.993 4.993 0 0 1-.564-3.524a7.991 7.991 0 0 0-1.297-.75A4.993 4.993 0 0 1 12 6a4.993 4.993 0 0 1-3.335-1.274a7.99 7.99 0 0 0-1.297.75A4.993 4.993 0 0 1 6.804 9a4.993 4.993 0 0 1-2.77 2.25a8.125 8.125 0 0 0 0 1.5A4.993 4.993 0 0 1 6.805 15ZM12 15a3 3 0 1 1 0-6a3 3 0 0 1 0 6Zm0-2a1 1 0 1 0 0-2a1 1 0 0 0 0 2Z">
                    </path>
                  </svg>
                  <span class="icon-title">功能设置</span>
                </div>
                <div style="padding: 20px;"
                     class="formkit-inner inline-flex items-center w-full relative box-border border border-gray-300 formkit-invalid:border-red-500 h-9 rounded-base overflow-hidden focus-within:border-primary focus-within:shadow-sm sm:max-w-lg transition-all">
                  <div class="nm-checkbox">
                    <input class="toggle-all" type="checkbox" :checked="data.commentStatus"
                           v-model="data.commentStatus"/>
                    <label for="toggle-all"><span class="nm-checkbox-text">新评论通知</span></label>
                  </div>
                  <div class="nm-checkbox">
                    <input class="toggle-all" type="checkbox" :checked="data.commentAuditsStatus"
                           v-model="data.commentAuditsStatus"/>
                    <label for="toggle-all"><span class="nm-checkbox-text">评论审核通知</span></label>
                  </div>
                  <div class="nm-checkbox">
                    <input class="toggle-all" type="checkbox" :checked="data.postStatus" v-model="data.postStatus"/>
                    <label for="toggle-all"><span class="nm-checkbox-text">文章发布通知</span></label>
                  </div>
                  <div class="nm-checkbox">
                    <input class="toggle-all" type="checkbox" :checked="data.postAuditsStatus"
                           v-model="data.postAuditsStatus"/>
                    <label for="toggle-all"><span class="nm-checkbox-text">投稿审核通知</span></label>
                  </div>
                </div>
              </div>

              <button class="btn-md btn-secondary btn" type="button" style="margin-top: 50px;" @click="createData">
                <span class="btn-content">保存</span>
              </button>
            </section>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<style>
.nm-checkbox {
  font-size: 13px;
  margin-right: 20px;
}

.nm-checkbox-text {
  margin-left: 5px;
  vertical-align: -1px;
}

.nm-input {
  border-radius: 5px !important;
  font-size: 13px !important;
}

.nm-checkbox-title {
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 10px;
}

.nm-title-container {
  border-bottom: 1px solid #e5e7eb;
  margin-bottom: 20px;
}

.icon-title {
  vertical-align: -2px;
  margin-left: 3px;
}

.tip {
  padding: 14px;
  font-size: 13px;
  border-radius: 10px;
  margin-bottom: 30px;
  background: #fafafc;
  color: #8077A4;
}
</style>
