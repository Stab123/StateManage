<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-space>
        功能域：
        <a-select default-value="" style="width: 200px" @change="handleChange">
          <a-select-option value="jack">
            态势域
          </a-select-option>
          <a-select-option value="lucy">
            情报域
          </a-select-option>
          <a-select-option value="disabled" >
            指挥域
          </a-select-option>
          <a-select-option value="Yiminghe">
            监控域
          </a-select-option>
          <a-select-option value="disabled" >
            管理域
          </a-select-option>
          <a-select-option value="Yiminghe">
            规划域
          </a-select-option>
        </a-select>

        应用集成模板：
        <a-select default-value="" style="width: 200px" @change="handleChange">
          <a-select-option value="jack">
            图像目标检测识别应用
          </a-select-option>
        </a-select>

        服务：
        <a-select default-value="" style="width: 200px" @change="handleChange">
          <a-select-option value="jack">
            图像目标检测计算服务
          </a-select-option>
          <a-select-option value="lucy">
            视频目标追踪计算服务
          </a-select-option>
        </a-select>
      </a-space>
    </div>
    <div>
      <br>
    </div>

    <div>
      <a-space>
        验证客户端模板
      <a-select default-value="" style="width: 200px" @change="handleChange">
        <a-select-option value="jack">
          测试模板
        </a-select-option>
      </a-select>
      </a-space>
    </div>

    <div>
      <br>
    </div>

    <div>
      <a-space>
            <a-config-provider :auto-insert-space-in-button="false" style="width: 100px">
              <a-button type="primary">
                验证
              </a-button>
            </a-config-provider>
            <a-button type="primary" style="width: 100px">
              生成
            </a-button>
      </a-space>
    </div>

  </a-card>
</template>

<script>
  import ContainerModal from '../system/modules/ContainerModal'
  import {doReleaseData, doReovkeData} from '@/api/api'
  import {getAction} from '@/api/manage'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import { ACCESS_TOKEN } from '@/store/mutation-types'

  export default {
    name: "ShengCheng",
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
            title: '模板名称',
            align: "center",
            dataIndex: 'titile',
            customRender: function (text) {
              return "测试模板"
            }
          },
          {
            title: '大小',
            align: "center",
            dataIndex: 'msgCategory',
            customRender: function (text) {
              if (text == '1') {
                return "12Mb";
              } else if (text == "2") {
                return "10Mb";
              } else {
                return text;
              }
            }
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