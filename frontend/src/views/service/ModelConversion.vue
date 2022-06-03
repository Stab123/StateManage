<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline" :model="selectedOption">
        <a-row :gutter="24">
          <a-col :span="4">
            <a-form-model-item label="代码模板" >
              <a-select style="width: 150px"  v-model="selectedOption.codeFrame">
                <a-select-option  v-for="codeFrameOpt in codeFrameOptions" :value="codeFrameOpt">
                  {{codeFrameOpt}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>

          <a-col :span="4">
            <a-form-model-item label="语言" >
              <a-select style="width: 150px"  v-model="selectedOption.language">
                <a-select-option  v-for="languageOpt in languageOptions" :value="languageOpt" >
                  {{languageOpt}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>

          <a-col :span="4">
            <a-form-model-item label="操作系统" >
              <a-select style="width: 150px"  v-model="selectedOption.os">
                <a-select-option  v-for="osOpt in operatingSystemOptions" :value="osOpt">
                  {{osOpt}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary">逻辑及接口符合验证</a-button>
      <a-button type="primary">语法验证</a-button>
      <a-button type="primary">平台相关模型转换</a-button>
      <a-button type="primary">应用代码生成</a-button>
      <!--      <a-button @click="testChange" type="primary" icon="plus">导入</a-button>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="key"
        :columns="columns"
        :dataSource="tableData"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit_(record.name)">编辑</a>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->
    <communication-service-modal ref="communicationForm"></communication-service-modal>
    <a-i-detect-service-modal ref="aiDetectForm"></a-i-detect-service-modal>
    <database-service-modal ref="databaseForm"></database-service-modal>
    <!-- 表单区域 -->


  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import CommunicationServiceModal from '@/views/service/CommunicationServieModal'
import AIDetectServiceModal from '@/views/service/AIDetectServiceModal'
import DatabaseServiceModal from '@/views/service/DatabaseServiceModal'

export default {
  name: "ModelConversion",
  mixins: [JeecgListMixin],
  components: {
    CommunicationServiceModal,
    AIDetectServiceModal,
    DatabaseServiceModal
  },
  computed:{
    tableData(){
      if(Object.keys(this.queryParam).length === 0){
        return this.dataSource
      }
      return this.dataSource.filter(record => record.name.includes(this.queryParam.name.trim()))
    }
  },
  data() {
    return {
      description: '服务开发平台模型转换工具',
      // 查询条件
      queryParam: {},
      // 表头
      columns: [
        {
          title: '服务名称',
          align: "center",
          dataIndex: 'name',
        },
        {
          title: '版本',
          align: "center",
          dataIndex: 'version'
        },
        {
          title: '修改日期',
          align: "center",
          dataIndex: 'modifiedDate'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          scopedSlots: {customRender: 'action'},
        }
      ],
      // 代码模板选项
      codeFrameOptions:['AIDetectApp', 'Test1App', 'Test2App'],
      languageOptions: ['C++', 'Python', 'Golang'],
      operatingSystemOptions: ['Windows 7', 'Windows XP'],
      selectedOption:{}
    }
  },
  watch:{
    selectedOption:{
      deep: true,
      handler(oldValue, newValue){
        console.log(oldValue)
      }
    }
  },
  methods: {
    handleEdit_(name){
      switch (name){
        case 'CommunicationService':
          this.$refs.communicationForm.edit()
          break
        case 'AIDetectService':
          this.$refs.aiDetectForm.edit()
          break
        case 'DatabaseService':
          this.$refs.databaseForm.edit()
          break
      }
    },
    loadData(){
      this.selectedOption = {
        codeFrame: this.codeFrameOptions[0],
        language: this.languageOptions[0],
        os: this.operatingSystemOptions[0]
      }
      this.dataSource = [
        {
          key: '1',
          name: 'CommunicationService',
          version: 'v1.0',
          modifiedDate: '2021.10.15',
        },
        {
          key: '2',
          name: 'AIDetectService',
          version: 'v1.1',
          modifiedDate: '2021.10.16',
        },
        {
          key: '3',
          name: 'DatabaseService',
          modifiedDate: '2021.11.1',
          version: 'v0.8',
        },
      ]
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
</style>