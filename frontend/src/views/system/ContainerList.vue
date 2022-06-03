<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :span="6">
            <a-form-item label="项目名称">
              <j-input placeholder="请输入项目名称" v-model="queryParam.titile"></j-input>
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

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新建项目</a-button>
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
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a  v-if="record.sendStatus == 0" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" v-if="record.sendStatus == 0"/>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <container-modal ref="modalForm" @ok="modalFormOk"></container-modal>
    <!-- 查看详情 -->
    <j-modal class="detail-modal" title="查看详情" :visible.sync="detailModal.visible" :top="50" :width="600" switchFullscreen :footer="null">
      <iframe v-if="detailModal.url" class="detail-iframe" :src="detailModal.url"/>
    </j-modal>

  </a-card>
</template>

<script>
  import ContainerModal from './modules/ContainerModal'
  import {doReleaseData, doReovkeData} from '@/api/api'
  import {getAction} from '@/api/manage'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import { ACCESS_TOKEN } from '@/store/mutation-types'

  export default {
    name: "ContainerList",
    mixins: [JeecgListMixin],
    components: {
      ContainerModal
    },
    data() {
      return {
        description: '系统通告表管理页面',
        // 查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: '项目名称',
            align: "center",
            dataIndex: 'titile',
            customRender: function (text) {
              if(text == '放假通知'){
                return 'online-reschedule-service';
              } else {
                return text;
              }
            }
          },
          {
            title: '访问级别',
            align: "center",
            dataIndex: 'msgCategory',
            customRender: function (text) {
              if (text == '1') {
                return "公开";
              } else if (text == "2") {
                return "私有";
              } else {
                return text;
              }
            }
          },
          {
            title: '角色',
            align: "center",
            dataIndex: 'sender',
            customRender: function (text) {
              return '项目管理员';
            }
          },
          {
            title: '类型',
            align: "center",
            dataIndex: 'priority',
            customRender: function (text) {
              if (text == 'L') {
                return "项目";
              } else if (text == "M") {
                return "项目";
              } else if (text == "H") {
                return "项目";
              } else {
                return '项目';
              }
            }
          },
          {
            title: '镜像仓库数',
            align: "center",
            dataIndex: 'msgType',
            customRender: function (text) {
              if (text == 'USER') {
                return "1";
              } else if (text == "ALL") {
                return "5";
              } else {
                return text;
              }
            }
          },
          {
            title: '创建时间',
            align: "center",
            dataIndex: 'startTime'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {customRender: 'action'},
          }
        ],
        detailModal: {visible: false, url: '',},
        url: {
          list: "/sys/annountCement/list",
          delete: "/sys/annountCement/delete",
          deleteBatch: "/sys/annountCement/deleteBatch",
          releaseDataUrl: "/sys/annountCement/doReleaseData",
          reovkeDataUrl: "sys/annountCement/doReovkeData",
          exportXlsUrl: "sys/annountCement/exportXls",
          importExcelUrl: "sys/annountCement/importExcel",
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      //执行发布操作
      releaseData: function (id) {
        console.log(id);
        var that = this;
        doReleaseData({id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData(1);
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      //执行撤销操作
      reovkeData: function (id) {
        var that = this;
        doReovkeData({id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData(1);
            this.syncHeadNotic(id)
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      syncHeadNotic(anntId){
        getAction("sys/annountCement/syncNotic",{anntId:anntId})
      },
      handleDetail:function(record){
        const domain = window._CONFIG['domianURL']
        const token = this.$ls.get(ACCESS_TOKEN)
        this.detailModal.url = `${domain}/sys/annountCement/show/${record.id}?token=${token}`
        this.detailModal.visible = true
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

</style>