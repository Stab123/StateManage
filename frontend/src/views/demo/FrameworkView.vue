<template>
  <div>
    <a-divider/>
    <div id="info">
      <span class="domain-name">{{metaData.name}}</span>
      <div id="metadata">
        <div class="metadata-item">
          <i class="anticon-jeecg anticongit-commit"/>
          <span class="metadata-item-data">{{metaData.commitTimes}}</span>
          <span>次提交</span>
        </div>
        <div class="metadata-item">
          <i class="anticon-jeecg anticonbranches"/>
          <span class="metadata-item-data">{{metaData.branches}}</span>
          <span>个分支</span>
        </div>
        <div class="metadata-item">
          <i class="anticon-jeecg anticontag"/>
          <span class="metadata-item-data">{{metaData.tags}}</span>
          <span>个标签</span>
        </div>
        <div class="metadata-item">
          <i class="anticon-jeecg anticon-Files"/>
          <span class="metadata-item-data">{{metaData.files}}</span>
          <span>文件</span>
        </div>
        <div class="metadata-item">
          <i class="anticon-jeecg anticonstorage"/>
          <span class="metadata-item-data">{{metaData.storage}}</span>
          <span>存储</span>
        </div>
      </div>
      <div>
        {{metaData.description}}
      </div>
      <a-button type="primary" icon="plus" style="margin-top: 5px">导入</a-button>
    </div>
    <a-table :columns="columns" :data-source="data" :pagination="false">
        <span slot="actions" slot-scope="text, record">
          <i class="anticon-jeecg anticonfile" v-if="record.type == 'blob'" style="font-size: 20px"/>
          <i class="anticon-jeecg anticonfolder" v-else-if="record.type == 'tree'" style="font-size: 20px"/>
          <a class="filename">{{text}}</a>
        </span>
    </a-table>

  </div>
</template>

<script>
const columns = [
  {
    title: '名称',
    dataIndex: "name",
    key: 'name',
    scopedSlots: {customRender: 'actions'}
  },
  {
    title: '最后提交',
    dataIndex: 'lastCommit',
    key: 'lastCommit',
  },
  {
    title: '最后更新',
    dataIndex: 'lastUpdate',
    key: 'lastUpdate',
  },
];

const data = [
  {
    key: '0',
    name: '态势域服务实现模板',
    type: 'tree',
    lastCommit: '[add]stuff',
    lastUpdate: '5 minutes ago'
  },
  {
    key: '1',
    name: '情报域实现模板',
    type: 'tree',
    lastCommit: '[add]stuff',
    lastUpdate: '5 minutes ago'
  },
  {
    key: '2',
    name: '指挥域服务实现模板',
    type: 'tree',
    lastCommit: '[add]stuff',
    lastUpdate: '5 minutes ago'
  },  {
    key: '3',
    name: '监控域服务实现模板',
    type: 'tree',
    lastCommit: '[add]stuff',
    lastUpdate: '5 minutes ago'
  },  {
    key: '4',
    name: '管理域服务实现模板',
    type: 'tree',
    lastCommit: '[add]stuff',
    lastUpdate: '5 minutes ago'
  },  {
    key: '5',
    name: '规划域服务实现模板',
    type: 'tree',
    lastCommit: '[add]stuff',
    lastUpdate: '5 minutes ago'
  },
];

const metaData = {
  id: '1',
  name: '服务实现模板框架',
  commitTimes: '2',
  branches: '1',
  tags: '0',
  files: '246 KB',
  storage: '246 KB',
  description: '服务实现模板库框架'
}

export default {
  name: 'FrameworkView',
  data(){
    return{
      data,
      columns,
      metaData
    }
  }
}
</script>

<style scoped>
.domain-name{
  font-weight: bolder;
  font-size: x-large;
}
#info{
  margin-bottom: 20px;
}
#metadata{
  display: flex;
  margin-top: 5px;
  margin-bottom: 5px;
}
.metadata-item{
  margin-right: 25px;
}

.metadata-item i{
  font-size: 20px;
}
.metadata-item .metadata-item-data{
  font-weight: bold;
  margin-right: 2px;
  margin-left: 2px;
}

.filename{
  margin-left: 3px;
  color: #000000;
}
.filename:hover{
  text-decoration: underline;
}
</style>