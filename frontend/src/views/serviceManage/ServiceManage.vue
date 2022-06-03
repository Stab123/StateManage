<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div>
      <br>
    </div>

    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :span="6">
            <a-form-item label="关键字：">
              <j-input placeholder="请输入关键字" v-model="queryParam.titile"></j-input>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="data"
        :loading="loading">

           <span slot="action" slot-scope="text,record">
          <a @click="handleStart(record)">启动</a>

          <a-divider type="vertical" />

          <a @click="handlePause(record)">停止</a>

             <a-divider type="vertical" />

          <a @click="handleDelete(record)">删除</a>

        </span>

      </a-table>
    </div>
    <!-- table区域-end -->
    <state-data-modal ref = "modalForm" @ok="modalFormOk"></state-data-modal>
    <state-data-chart ref = "StateDataChart"  @ok="modalFormOk"></state-data-chart>
  </a-card>
</template>

<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import StateDataChart from './module/StateDataChart'
  import StateDataModal from './module/StateDataModal'
  import {getPod, getPodLabel, getServiceList, pause, start, deleteService} from "../../api/api";

  export default {
    name: "ServiceManage",
    mixins: [JeecgListMixin],
    components: {
      StateDataModal,
      StateDataChart,
    },
    data() {
      return {
        description: '状态数据管理页面',
        valueType: null,
        secondValueType: null,
        // 查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: 'Application Id',
            align: "center",
            dataIndex: 'application',
          },
          {
            title: 'Service Id',
            align: 'center',
            dataIndex: 'service',
          },
          {
            title: 'Service Name',
            align: 'center',
            dataIndex: 'name',
          },
          {
            title: 'State',
            dataIndex: 'data',
            align: "center",
          },
          {
            title: 'Create Time',
            dataIndex: 'time',
            align: "center",
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {customRender: 'action'},
          }
        ],
        data:[
        ],
        detailModal: {visible: false, url: '',}
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods:{
      handleStart: function (record) {
        let url = {"url": record.application +":" + record.name};
        start(url).then(response => (console.log(response)));
        this.$refs.StateDataChart.visible = true;
        this.$refs.StateDataChart.message = "已启动";
        setTimeout(() => {
          getServiceList().then(
            response => {for (let i = 0; i < response.serverList.length; i++){
              if (response.serverList[i].name === record.name){
                record.data = response.serverList[i].state;
              }}});
        }, 1000);
      },
      handlePause: function (record) {
        let url = {"url": record.application +":" + record.name};
        pause(url).then(response => (console.log(response)));
        this.$refs.StateDataChart.visible = true;
        this.$refs.StateDataChart.message = "已停止";
        setTimeout(() => {
          getServiceList().then(
            response => {for (let i = 0; i < response.serverList.length; i++){
              if (response.serverList[i].name === record.name){
                record.data = response.serverList[i].state;
              }}});
        }, 1000);
      },
      handleDelete: function (record) {
        let url = {"url": record.application +":" + record.name};
        deleteService(url).then(response => (console.log(response)));
        this.$refs.StateDataChart.visible = true;
        this.$refs.StateDataChart.message = "已删除";
        this.data = [];
        getServiceList().then(
          response => {for (let i = 0; i < response.serverList.length; i++){
            let newData = {
              namespace: null,
              pod: null,
              data: null,
              action:''
            };
            newData.application = response.serverList[i].namespaceId;
            newData.service = response.serverList[i].serviceId;
            newData.name = response.serverList[i].name;
            newData.data = response.serverList[i].state;
            newData.time = response.serverList[i].createdTime;
            this.data.push(newData);}});
      }
    },
    created() {
      getServiceList().then(
        response => {for (let i = 0; i < response.serverList.length; i++){
          let newData = {
            namespace: null,
            pod: null,
            data: null,
            action:''
          };
            newData.application = response.serverList[i].namespaceId;
            newData.service = response.serverList[i].serviceId;
            newData.name = response.serverList[i].name;
            newData.data = response.serverList[i].state;
            newData.time = response.serverList[i].createdTime;
            this.data.push(newData);}});
    }

  }
</script>