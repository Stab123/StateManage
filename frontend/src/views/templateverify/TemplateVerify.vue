<template>
  <a-card :bordered="false">

    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-model-item label="应用数据类型" >
              <a-select style="width: 150px"  v-model="selectedOption.appDataType">
                <a-select-option  v-for="(appDataTypeOpt,index) in availableOptions.appDataType" :value="appDataTypeOpt" :key="index">
                  {{appDataTypeOpt}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>

          <a-col :span="6">
            <a-form-model-item label="服务客户端模板" >
              <a-select style="width: 150px"  v-model="selectedOption.serviceClientTemplate">
                <a-select-option  v-for="(templateOpt,index) in availableOptions.serviceClientTemplate" :value="templateOpt" :key="index">
                  {{templateOpt}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="3">
              <a-upload
                :show-upload-list="true"
                :before-upload="rejectUpload"
                @change="handleChange"
              >
                <a-button type="primary" :size="'large'" >加载</a-button>
              </a-upload>
          </a-col>
          <a-col :span="3">
            <a-button type="primary"  :size="'large'" :disabled="isVerifyDisabled" @click="handleVerify">验证</a-button>
          </a-col>
        </a-row>

        <a-row v-if="isVerifyDisabled" :gutter="24">
          <div class="hint">
            请先加载验证项目
          </div>
        </a-row>
        <template v-if="!isVerifyDisabled">
          <a-row :gutter="24" >
            <div style="margin-top: 10px">
                <div class="result-label">
                  验证结果:
                </div>
                <div v-if="verifyResult === null" style="display: inline">
                </div>
                <div v-else-if="verifyResult === true" class="result-success">
                  通过 ✓
                </div>
                <div v-else class="result-fail">
                  未通过 ✕
                </div>
            </div>
          </a-row>

          <a-row :gutter="24">
            <div style="margin-top: 10px">
              <div class="result-label">
                运行输出:
              </div>
              <div style="display: inline" v-if="verifyResult !== null" >
                <img src="~@/views/templateverify/example.png" style="vertical-align: top; height: 400px; width: 400px"/>
              </div>
            </div>
          </a-row>
        </template>

      </a-form>
    </div>

  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
export default {
  name: "TemplateVerify",
  mixins: [JeecgListMixin],
  components: {
  },
  computed:{
    isVerifyDisabled(){
      return this.fileList.length === 0
    }
  },
  data() {
    return {
      description: '客户端模板验证系统界面',
      selectedOption: {},
      fileList: [],
      availableOptions: {
        appDataType: ['态势类',  '情报类'],
        serviceClientTemplate: ['情报域', '管理域']
      },
      verifyResult: null
    }
  },
  methods: {
    // FIXME
    // 这部分应该放到后端来做
    rejectUpload(){
      return false
    },

    handleChange({fileList}){
      this.fileList = fileList
      this.verifyResult = null
    },

    handleVerify(){
      this.verifyResult = true
    },

    loadData(){
      this.selectedOption = {
          appDataType: this.availableOptions.appDataType[0],
          serviceClientTemplate: this.availableOptions.serviceClientTemplate[0]
      }
    },
    initDictConfig(){
      // please remove it
      console.log('Do nothing')
    },
  }
}
</script>


<style scoped lang="less">
@import '~@assets/less/common.less';
/** 查看详情弹窗的样式 */
.detail-modal {
  .detail-iframe {
    border: 0;
    width: 100%;
    height: 88vh;
    min-height: 600px;
  }
  &.fullscreen .detail-iframe {
    height: 100%;
  }
}
.detail-modal /deep/ .ant-modal {
  top: 30px;
  .ant-modal-body {
    font-size: 0;
    padding: 0;
  }
}
.detail-modal.fullscreen /deep/ .ant-modal {
  top: 0;
}
.underline-hover:hover{
  text-decoration: underline;
}
.hint{
  color: lightgray;
  font-weight: bold;
  margin-top: 10px;

}
.result-label{
  display: inline;
  font-weight: bold;
}
.result-success{
  color: green;
  display: inline;
  font-weight: bold;
}
.result-fail{
  color: red;
  display: inline;
  font-weight: bold;
}
</style>