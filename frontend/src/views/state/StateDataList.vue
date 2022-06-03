<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-space>
        状态数据类型：
        <a-select v-model="valueType" style="width: 200px" @change="handleChange">
          <a-select-option value="hardware">
            硬件数据
          </a-select-option>
          <a-select-option value="software">
            服务器端数据
          </a-select-option>
          <a-select-option value="clientSoftware">
            客户端应用软件数据
          </a-select-option>
          <a-select-option value="log">
            日志数据
          </a-select-option>
        </a-select>
        <span v-show = "isShowClientSoftware">应用软件</span>
        <a-select v-model="secondValueType" v-show = "isShowClientSoftware"  style="width: 200px" @change="handleChange">
          <a-select-option value="1">
            1
          </a-select-option>
        </a-select>
        <span v-show = "isShowHardware">节点：</span>
        <a-select  v-model = "fourthValueType" v-show = "isShowHardware"  style="width: 200px" @change="handleChange">
          <a-select-option value="172.17.0.13:9100">
            172.17.0.13
          </a-select-option>
          <a-select-option value="172.17.0.17:9100">
            172.17.0.17
          </a-select-option>
          <a-select-option value="172.17.0.5:9100">
            172.17.0.5
          </a-select-option>
          <a-select-option value="windows">
            windows
          </a-select-option>
        </a-select>
        <span v-show = "isShowSoftware">功能域</span>
        <a-select v-model="secondValueType" v-show = "isShowSoftware"  style="width: 200px" @change="handleChange">
          <a-select-option value="1">
            1
          </a-select-option>
        </a-select>
        <span v-show = "isShowSoftware">功能子域</span>
        <a-select v-model = "thirdValueType" v-show = "isShowSoftware" style="width: 200px" @change="handleChange">
          <a-select-option value="ai-graph-detection">
            ai-graph-detection
          </a-select-option>
        </a-select>
        <span v-show = "isLog">来源：</span>
        <a-select v-model="firthValueType" v-show = "isLog"  style="width: 200px" @change="handleChange">
          <a-select-option value="service">
            服务端
          </a-select-option>
          <a-select-option value="client">
            客户端
          </a-select-option>
        </a-select>
      </a-space>
    </div>
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
              <a-button v-show = "isLog" type="primary" @click="Reset" icon="reload" style="margin-left: 8px">刷新</a-button>
              <a-button v-show = "isLog" type="primary" @click="Download" icon="download" style="margin-left: 8px">批量下载</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
    <div v-show = "!isLog">
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="data"
        :loading="loading">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">查看</a>

          <a-divider type="vertical" />

          <a @click="handleVisual(record)">可视化</a>

        </span>

      </a-table>
    </div>

    <div v-show = "isLog">
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="data"
        :loading="loading">

        <span slot="action" slot-scope="text, record">
          <a @click="handleDownload(record)">下载</a>

          <a-divider type="vertical" />

          <a @click="handleDelete(record)">删除</a>

        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <state-data-modal ref = "modalForm" @ok="modalFormOk"></state-data-modal>
    <state-data-chart ref = "StateDataChart"  @ok="modalFormOk"></state-data-chart>
    <!-- 查看详情 -->
    <j-modal class="detail-modal" title="查看详情" :visible.sync="detailModal.visible" :top="50" :width="600" switchFullscreen :footer="null">
      <iframe v-if="detailModal.url" class="detail-iframe" :src="detailModal.url"/>
    </j-modal>

  </a-card>
</template>

<script>
  import StateDataModal from './module/StateDataModal'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import StateDataChart from './module/StateDataChart'
  import {
    getCpu,
    getMemory,
    getMemoryAvailable,
    getFile,
    getDiskWrite,
    getDiskRead,
    getProcess,
    getThread,
    getFd,
    getCtnCpu,
    getCtnMem,
    getLogList,
    getClientLogList,
    deleteClientLog,
    getServiceList,
    deleteServiceLog
  } from "../../api/api";
  import axios from "axios";
  import {saveAs} from 'file-saver';
  import yaml from 'js-yaml'

  export default {
    name: "StateDataList",
    mixins: [JeecgListMixin],
    components: {
      StateDataModal,
      StateDataChart
    },
    data() {
      return {
        description: '状态数据管理页面',
        valueType: null,
        secondValueType: null,
        thirdValueType: null,
        fourthValueType: null,
        firthValueType: null,
        isShowHardware: false,
        isShowSoftware: false,
        isShowClientSoftware: false,
        isLog: false,
        // 查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: '状态数据名称',
            align: "center",
            dataIndex: 'title',
          },
          {
            title: '当前数据',
            dataIndex: 'data',
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
      handleChange:function(){
        this.isShowSoftware = this.valueType === "software";
        this.isShowHardware = this.valueType === "hardware";
        this.isShowClientSoftware = this.valueType === "clientSoftware";
        this.isLog = this.valueType === "log";
        if (this.valueType === "clientSoftware" && this.secondValueType === "1"){
          this.data  = [{
            title:"运行状态",
            data: "Running",
            action:'',
          }];
        }
        if (this.valueType === "hardware" && this.fourthValueType === "windows"){
          this.data = [];
          axios
            .get('http://192.168.95.132:9090/api/v1/query?query=100 - (avg by (instance) (irate(windows_cpu_time_total{mode="idle"}[2m])) * 100)')
            .then(response => {
              console.log(response);
              let newData = {
                title: "CPU占用率",
                data: null,
                action:''
              };
              newData.data = Number(response.data.data.result[0].value[1]).toFixed(2) + "%";
              this.data.push(newData);
            })
            .catch(function (error) { // 请求失败处理
              console.log(error);
            });
          axios
            .get('http://192.168.95.132:9090/api/v1/query?query=windows_os_physical_memory_free_bytes')
            .then(response => {
              console.log(response);
              let newData = {
                title: "可用内存",
                data: null,
                action:''
              };
              newData.data = Number(response.data.data.result[0].value[1] / (1024 * 1024)).toFixed(2)+"M";
              this.data.push(newData);
            })
            .catch(function (error) { // 请求失败处理
              console.log(error);
            });
          axios
            .get('http://192.168.95.132:9090/api/v1/query?query=1 - (windows_os_physical_memory_free_bytes /windows_os_visible_memory_bytes)')
            .then(response => {
              console.log(response);
              let newData = {
                title: "内存使用率",
                data: null,
                action:''
              };
              newData.data = Number(response.data.data.result[0].value[1] * 100).toFixed(2)+"%";
              this.data.push(newData);
            })
            .catch(function (error) { // 请求失败处理
              console.log(error);
            });
        }
        if (this.valueType === "hardware" && this.fourthValueType != null && this.fourthValueType !== "windows"){
          this.data = [];
          let str = {"instance":this.fourthValueType};
          getCpu(str).then(
            response => {
              let newData = {
                title: "CPU占用率",
                data: null,
                action:''
              };
              newData.data = Number(response.data.result[0].value[1] * 100).toFixed(2) + "%";
              this.data.push(newData);});
          getMemory(str).then(
            response => {
              let newData = {
                title: "内存占用率",
                data: null,
                action:''
              };
              newData.data = Number(response.data.result[0].value[1] * 100).toFixed(2) + "%";
              this.data.push(newData);});
          getMemoryAvailable(str).then(
            response => {
              let newData = {
                title: "可用内存",
                data: null,
                action:''
              };
              newData.data = Number(response.data.result[0].value[1] / (1024 * 1024 * 1024)).toFixed(2) + "GB";
              this.data.push(newData);});
          getFile(str).then(
            response => {
              let newData = {
                title: "磁盘使用率",
                data: null,
                action:''
              };
              newData.data = Number(response.data.result[0].value[1] * 100).toFixed(2) + "%";
              this.data.push(newData);});
          getDiskRead(str).then(
            response => {
              let newData = {
                title: "磁盘读IO",
                data: null,
                action:''
              };
              newData.data = Number(response.data.result[0].value[1]).toFixed(2);
              this.data.push(newData);});
          getDiskWrite(str).then(
            response => {
              let newData = {
                title: "磁盘写IO",
                data: null,
                action:''
              };
              newData.data = Number(response.data.result[0].value[1]).toFixed(2);
              this.data.push(newData);});
        }
        if (this.valueType === "software" && this.secondValueType != null && this.thirdValueType != null){
          this.data = [];
          let str = {"container":this.thirdValueType};
          getProcess(str).then(
            response => {
              let newData = {
                title: "运行进程个数",
                data: null,
                action:''
              };
              newData.data = response.data.result[0].value[1];
              this.data.push(newData);});
          getThread(str).then(
            response => {
              let newData = {
                title: "运行线程个数",
                data: null,
                action:''
              };
              newData.data = response.data.result[0].value[1];
              this.data.push(newData);});
          getFd(str).then(
            response => {
              let newData = {
                title: "打开文件描述符个数",
                data: null,
                action:''
              };
              newData.data = response.data.result[0].value[1];
              this.data.push(newData);});
          getCtnCpu(str).then(
            response => {
              let newData = {
                title: "容器cpu使用率",
                data: null,
                action:''
              };
              newData.data = Number(response.data.result[0].value[1] / 10000).toFixed(2) + "%";
              this.data.push(newData);});
          getCtnMem(str).then(
            response => {
              let newData = {
                title: "容器内存使用",
                data: null,
                action:''
              };
              newData.data = Number(response.data.result[0].value[1]);
              this.data.push(newData);});
        }
        if (this.valueType !== "log"){
          this.columns = [
            {
              title: '状态数据名称',
              align: "center",
              dataIndex: 'title',
            },
            {
              title: '当前数据',
              dataIndex: 'data',
              align: "center",
            },
            {
              title: '操作',
              dataIndex: 'action',
              align: "center",
              scopedSlots: {customRender: 'action'},
            }
          ];
        }
        if (this.valueType === "log" && this.firthValueType === "service"){
          this.data = [];
          getLogList().then(
            response => {
              for (let i = 0; i < response.length; i++) {
                let newData = {
                  application: null,
                  service: null,
                  name: null,
                  log: null,
                  time: null,
                  level: null,
                };
                newData.application = response[i].appId;
                newData.service = response[i].serviceId;
                newData.name = response[i].serviceName;
                newData.log = response[i].content;
                newData.time = response[i].createTime;
                newData.level = response[i].level;
                this.data.push(newData);
              }}
          );
          this.columns = [{
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
              title: 'Level',
              dataIndex: 'level',
              align: "center",
              filters: [
                { text: 'Info', value: 'Info' },
                { text: 'Warning', value: 'Warning' },
                { text: 'Error', value: 'Error' },
                { text: 'Critical', value: 'Critical' },],
              onFilter: (value, record) => record.level === value,
            },
            {
              title: 'CreatedTime',
              dataIndex: 'time',
              align: "center",
            },{
            title: 'Log',
            align: "center",
            dataIndex: 'log',
          },
            {
              title: '操作',
              dataIndex: 'action',
              align: "center",
              scopedSlots: {customRender: 'action'},
            }];
        }
        if (this.valueType === "log" && this.firthValueType === "client"){
          this.data = [];
          getClientLogList().then(
            response => {
              for (let i = 0; i < response.length; i++) {
                let newData = {
                  client: null,
                  log: null,
                  time: null,
                  level: null,
                };
                newData.client = response[i].clientId;
                newData.log = response[i].content;
                newData.time = response[i].createTime;
                newData.level = response[i].level;
                this.data.push(newData);
              }}
          );
          this.columns = [{
            title: 'Client Id',
            align: "center",
            dataIndex: 'client',
          },
            {
              title: 'Level',
              dataIndex: 'level',
              align: "center",
              filters: [
                { text: 'Info', value: 'Info' },
                { text: 'Warning', value: 'Warning' },
                { text: 'Error', value: 'Error' },
                { text: 'Critical', value: 'Critical' },],
              onFilter: (value, record) => record.level === value,
            },
            {
              title: 'CreatedTime',
              dataIndex: 'time',
              align: "center",
            },{
              title: 'Log',
              align: "center",
              dataIndex: 'log',
            },
            {
              title: '操作',
              dataIndex: 'action',
              align: "center",
              scopedSlots: {customRender: 'action'},
            }];
        }
      },
      handleVisual: function (record) {
        this.$refs.StateDataChart.visible = true;
        if (record.title === "CPU占用率"){
          if (this.fourthValueType === "172.17.0.13:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/d3E8KYwnk/cpu_load1?viewPanel=6&kiosk=tv&orgId=1";
          }
          if (this.fourthValueType === "172.17.0.17:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/d3E8KYwnk/cpu_load1?viewPanel=2&kiosk=tv&orgId=1";
          }
          if (this.fourthValueType === "172.17.0.5:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/d3E8KYwnk/cpu_load1?viewPanel=4&kiosk=tv&orgId=1";
          }
        }
        if (record.title === "内存占用率"){
          if (this.fourthValueType === "172.17.0.13:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/1yFTJywnz/memoryload?orgId=1&viewPanel=4&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.17:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/1yFTJywnz/memoryload?orgId=1&viewPanel=6&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.5:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/1yFTJywnz/memoryload?orgId=1&viewPanel=2&kiosk=tv";
          }
        }
        if (record.title === "可用内存"){
          if (this.fourthValueType === "172.17.0.13:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/ybzDbswnz/memavailable?orgId=1&viewPanel=4&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.17:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/ybzDbswnz/memavailable?orgId=1&viewPanel=6&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.5:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/ybzDbswnz/memavailable?orgId=1&viewPanel=2&kiosk=tv";
          }
        }
        if (record.title === "磁盘使用率"){
          if (this.fourthValueType === "172.17.0.13:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/29SNasQnk/filesystem?orgId=1&viewPanel=4&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.17:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/29SNasQnk/filesystem?orgId=1&viewPanel=2&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.5:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/29SNasQnk/filesystem?orgId=1&viewPanel=6&kiosk=tv";
          }
        }
        if (record.title === "磁盘读IO"){
          if (this.fourthValueType === "172.17.0.13:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/BrwPByQnk/diskio?orgId=1&viewPanel=4&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.17:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/BrwPByQnk/diskio?orgId=1&viewPanel=2&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.5:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/BrwPByQnk/diskio?orgId=1&viewPanel=6&kiosk=tv";
          }
        }
        if (record.title === "磁盘写IO"){
          if (this.fourthValueType === "172.17.0.13:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/BrwPByQnk/diskio?orgId=1&viewPanel=10&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.17:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/BrwPByQnk/diskio?orgId=1&viewPanel=8&kiosk=tv";
          }
          if (this.fourthValueType === "172.17.0.5:9100"){
            this.$refs.StateDataChart.iframeUrl = "http://101.34.255.125:30001/d/BrwPByQnk/diskio?orgId=1&viewPanel=12&kiosk=tv";
          }
        }
      },
      handleDelete: function (record) {
        if (this.valueType === "log" && this.firthValueType === "client"){
          getClientLogList().then(
            response => {
              for (let i = 0; i < response.length; i++) {
                if (response[i].clientId === record.client && response[i].content === record.log){
                  let str = {"id": response[i].id};
                  console.log(str);
                  deleteClientLog(str);
                  break;
                }
              }}
          );
          setTimeout(() => {
            this.Reset();
          }, 1000);
        }
        if (this.valueType === "log" && this.firthValueType === "service"){
          getLogList().then(
            response => {
              for (let i = 0; i < response.length; i++) {
                if (response[i].appId === record.application && response[i].serviceId === record.service && response[i].content === record.log){
                  let str = {"id": response[i].id};
                  console.log(str);
                  deleteServiceLog(str);
                  break;
                }
              }}
          );
          setTimeout(() => {
            this.Reset();
          }, 1000);
        }

      },
      Reset:function(){
        if (this.valueType === "log" && this.firthValueType === "service"){
          this.data = [];
          getLogList().then(
            response => {
              for (let i = 0; i < response.length; i++) {
                let newData = {
                  application: null,
                  service: null,
                  name: null,
                  log: null,
                  time: null,
                  level: null,
                };
                newData.application = response[i].appId;
                newData.service = response[i].serviceId;
                newData.name = response[i].serviceName;
                newData.log = response[i].content;
                newData.time = response[i].createTime;
                newData.level = response[i].level;
                this.data.push(newData);
              }}
          );
        }
        if (this.valueType === "log" && this.firthValueType === "client"){
          this.data = [];
          getClientLogList().then(
            response => {
              for (let i = 0; i < response.length; i++) {
                let newData = {
                  client: null,
                  log: null,
                  time: null,
                  level: null,
                };
                newData.client = response[i].clientId;
                newData.log = response[i].content;
                newData.time = response[i].createTime;
                newData.level = response[i].level;
                this.data.push(newData);
              }}
          );
        }
      },
      Download: function(){
        for (let i = 0; i < this.data.length; i++){
          let str = new Blob([yaml.dump(this.data[i])], {type: 'text/plain;charset=utf-8'});
          if (this.firthValueType === "service"){
            saveAs(str, this.data[i].application + `_` + this.data[i].name  + `_log.txt`);
          }
          if (this.firthValueType === "client"){
            saveAs(str, this.data[i].client + `_log.txt`);
          }
        }
      },
      handleDownload:function(record){
        let str = new Blob([yaml.dump(record)], {type: 'text/plain;charset=utf-8'});
        if (this.firthValueType === "service"){
          saveAs(str, record.application + `_` + record.name  + `_log.txt`);
        }
        if (this.firthValueType === "client"){
          saveAs(str, record.client + `_log.txt`);
        }
      }
    },
    created() {
    }

  }
</script>