<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-model-item label="应用数据类型" >
              <a-select style="width: 150px"  v-model="selectedOption.appDataType">
                <a-select-option  v-for="(appDataTypeOpt,index) in appDataTypeOptions" :value="appDataTypeOpt" :key="index">
                  {{appDataTypeOpt}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>

          <a-col :span="6">
            <a-form-model-item label="功能域" >
              <a-select style="width: 150px"  v-model="selectedOption.domain">
                <a-select-option  v-for="(domainOpt,index) in domainOptions" :value="domainOpt" :key="index">
                  {{domainOpt}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>

          <a-col :span="6">
            <a-form-model-item label="功能子域" >
              <a-select style="width: 150px"  v-model="selectedOption.domain">
                <a-select-option  v-for="(domainOpt,index) in domainOptions" :value="domainOpt" :key="index">
                  {{domainOpt}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">

          <a-col :span="6">
            <a-form-item label="模型名称">
              <a-input placeholder="请输入模型名称" v-model="queryParam.name" allow-clear></a-input>
            </a-form-item>
          </a-col>

          <!--          <a-col :span="8">-->
          <!--            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">-->
          <!--              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>-->
          <!--              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
          <!--            </span>-->
          <!--          </a-col>-->
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
        <span slot="action" slot-scope="text, record">
          <a>查看</a>
          <a-divider type="vertical"></a-divider>
          <a>编辑</a>
          <a-divider type="vertical"></a-divider>
          <!--          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.key)">-->
          <!--                  <a>删除</a>-->
          <!--          </a-popconfirm>-->
          <a>下载</a>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->


  </a-card>
</template>

<script>
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
export default {
  name: "AppDataList",
  mixins: [JeecgListMixin],
  components: {
  },
  computed:{
    tableData(){
      return this.dataSource.filter(record => {
        if(Object.keys(this.queryParam).length !== 0 && !record.name.includes(this.queryParam.name.trim())){
          return false
        }
        for(let key in this.selectedOption){
          if (this.selectedOption[key] !== '全部' && (record[key] === undefined || record[key] != this.selectedOption[key])){
            return false
          }
        }
        return true
      })
    }
  },
  data() {
    return {
      description: '应用数据管理系统界面',
      // 查询条件
      queryParam: {},
      // 表头
      columns: [
        {
          title: '应用数据名称',
          align: "center",
          dataIndex: 'name',
        },
        {
          title: '创建者',
          align: "center",
          dataIndex: 'creator'
        },
        {
          title: '创建时间',
          align: "center",
          dataIndex: 'createdTime'
        },
        {
          title: '访问级别',
          align: "center",
          dataIndex: 'accessLevel'
        },
        {
          title: '大小',
          align: "center",
          dataIndex: 'size'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          scopedSlots: {customRender: 'action'},
        }
      ],
      selectedOption: {},
      // 元数据类型选项
      appDataTypeOptions: ['态势类', '情报类', '全部'],
      // 功能域选项
      domainOptions:['情报域', '指挥域', '监控域', '全部'],
      // 功能子域选项
      subDomainOptions: ['AI图像识别']
    }
  },
  methods: {
    handleDelete(key){
      this.dataSource = this.dataSource.filter(record => record.key !== key)
    },
    batchDel(){
      this.dataSource = this.dataSource.filter(record => !this.selectedRowKeys.includes(record.key))
      this.onClearSelected()
    },
    loadData(){
      this.selectedOption = {
        appDataType: this.appDataTypeOptions[0],
        domain : this.domainOptions[0],
      }
      this.dataSource = [
        {
          key: '1',
          name: '目标识别数据',
          creator: 'admin',
          createdTime : '2021 年 7 月 9 日 13:11',
          accessLevel: '公开',
          size: '1.4 KB',
          domain: '情报域',
          appDataType: '态势类',
        },
        {
          key: '2',
          name: '任务数据',
          creator: 'admin',
          createdTime : '2021 年 7 月 9 日 15:43',
          accessLevel: '公开',
          size: '1.9 KB',
          domain: '情报域',
          appDataType: '态势类',
        },
        {
          key: '3',
          name: 'test1',
          creator: 'admin',
          createdTime : '2021 年 7 月 9 日 15:43',
          accessLevel: '公开',
          size: '1.9 KB',
          domain: '监控域',
          appDataType: '情报类',
        },
        {
          key: '4',
          name: 'test2',
          creator: 'admin',
          createdTime : '2021 年 7 月 9 日 15:43',
          accessLevel: '公开',
          size: '1.9 KB',
          domain: '指挥域',
          appDataType: '情报类',
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