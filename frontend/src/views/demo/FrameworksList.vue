<template>
  <div>
    <BreadCrumb/>
    <a-divider/>
    <a-button type="primary" @click="importFramework">添加框架</a-button>
    <a-list item-layot="horizontal" :data-source="data" :bordered="true">
      <a-list-item slot="renderItem" slot-scope="item, index">
        <a-list-item-meta :description="item.description">
          <router-link :to="'frameworks/'+item.id" class="title" slot="title">{{ item.name }}</router-link>
        </a-list-item-meta>
      </a-list-item>
    </a-list>
  </div>
</template>

<script>
const data = [
  {
    name:'USCB服务总线框架',
    description: 'USCB服务总线框架',
    id: 1
  },
  {
   name:'分布式基础设施框架',
    description: '分布式基础设施框架',
    id: 2
  },
  {
    name:'数据管理框架',
    description: '数据管理框架',
    id: 3
  },
  {
    name:'服务实现模板库框架',
    description: '服务实现模板库框架',
    id: 4
  },
  {
    name:'服务运行管理框架',
    description: '服务运行管理框架',
    id: 5
  },
]
import BreadCrumb from '@comp/breadcrumb/BreadCrumb'
export default {
  name: 'DomainsList',
  data(){
    return{
      data
    }
  },
  components:{
    BreadCrumb
  },
  methods:{
    importFramework(){
      console.log("importFramework")
    }
  }
}
</script>

<style scoped>
.title{
  font-weight: bold;
  font-size: 20px;
}

</style>