<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :span="6">
            <a-form-item label="模型名称">
              <a-input placeholder="请输入模型名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
<!--              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>-->
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">导入</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
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
        <span slot="modelName" slot-scope="text, record">
          <router-link :to="'/ai/models/'+text" class="underline-hover">{{ text }}</router-link>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleTrain(record.name)">训练</a>
          <a-divider type="vertical"></a-divider>
          <a>评估</a>
          <a-divider type="vertical"></a-divider>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.key)">
                  <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
<!--    <framework-modal ref="modalForm" @ok="testAdd"></framework-modal>-->
    <train-param-modal ref="trainParamModelForm"></train-param-modal>


  </a-card>
</template>

<script>
import FrameworkModal from "@views/demo/FrameworkModal";
import TrainParamModal from "@views/ai/TrainParamModal"
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
export default {
  name: "AIModelList",
  mixins: [JeecgListMixin],
  components: {
    // FrameworkModal
    TrainParamModal
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
      description: 'AI平台模型管理界面',
      // 查询条件
      queryParam: {},
      // 表头
      columns: [
        {
          title: '模型名称',
          align: "center",
          dataIndex: 'name',
          scopedSlots: {customRender: 'modelName'}
        },
        {
          title: '修改日期',
          align: "center",
          dataIndex: 'modifiedDate'
        },
        {
          title: '当前状态',
          align: "center",
          dataIndex: 'currentState'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          scopedSlots: {customRender: 'action'},
        }
      ],
    }
  },
  methods: {
    handleTrain(modelName){
      this.$refs.trainParamModelForm.paramSet(modelName)
    },
    handleDelete(key){
      this.dataSource = this.dataSource.filter(record => record.key !== key)
    },
    batchDel(){
      this.dataSource = this.dataSource.filter(record => !this.selectedRowKeys.includes(record.key))
      this.onClearSelected()
    },
    loadData(){
      this.dataSource = [
        {
          key: '1',
          name: 'Yolov3',
          modifiedDate: '2021.10.15',
          currentState: '训练中'
        },
        {
          key: '2',
          name: 'test2',
          modifiedDate: '2021.10.16',
          currentState: '训练中'
        },
        {
          key: '3',
          name: 'test1',
          modifiedDate: '2021.11.1',
          currentState: '训练完成'
        },
      ]
    },
    initDictConfig(){
      // please remove it
      console.log('Do nothing')
    },
    testAdd(){
      console.log(1)
    }
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