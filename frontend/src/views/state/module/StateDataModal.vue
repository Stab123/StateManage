<template>
  <a-modal
    :title="title"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :okButtonProps="{ props: {disabled: disabled} }"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-row style="width: 100%;">
          <a-col :span="24/2">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="title"
              label="状态数据名称">
              <a-input placeholder='model.title' v-model="model.title" :readOnly="disableSubmit" disabled="true"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24/2">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="creator"
              label="创建者">
              <a-input placeholder='model.creator' v-model="model.creator" :readOnly="disableSubmit" disabled="true"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row style="width: 100%;">
          <a-col :span="24/2">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="最后更新时间">
              <a-input placeholder='model.updateTime' v-model="model.updateTime" :readOnly="disableSubmit" disabled="true"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24/2">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="accessLevel"
              label="访问级别">
              <a-input placeholder='model.accessLevel' v-model="model.accessLevel" :readOnly="disableSubmit" disabled="true"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row style="width: 100%;">
          <a-col :span="24/2">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="记录次数">
              <a-input placeholder='model.count' v-model="model.count" :readOnly="disableSubmit" disabled="true"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row style="width: 100%;">
          <a-col :span="24/2">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="当前数据">
              <a-input placeholder='model.data' v-model="model.data" :readOnly="disableSubmit" disabled="true"/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-spin>
    <select-user-list-modal ref="UserListModal" @choseUser="choseUser"></select-user-list-modal>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import { getAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import JEditor from '@/components/jeecg/JEditor'
  import SelectUserListModal from "../../system/modules/SelectUserListModal";
  import moment from 'moment'

  export default {
    components: { JEditor, JDate, SelectUserListModal},
    name: "StateDataModal",
    data () {
      return {
        title:"操作",
        visible: false,
        disableSubmit:false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 18 },
        },
        labelColX1: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperColX1: {
          xs: { span: 24 },
          sm: { span: 21 },
        },
        confirmLoading: false,
        validatorRules:{
          titile: [{ required: true, message: '请输入标题!' }],
          msgCategory: [{ required: true, message: '请选择消息类型!' }],
          msgType:[{ required: true, message: '请选择通告对象类型!' }],
          endTime:[{ required: true, message: '请选择结束时间!'} ,{validator: this.endTimeValidate}],
          startTime:[{required: true, message: '请选择开始时间!'},{validator: this.startTimeValidate}],
          msgAbstract: [{ required: true, message: '请输入摘要!' }],
        },
        url: {
          queryByIds: "/sys/user/queryByIds",
          add: "/sys/annountCement/add",
          edit: "/sys/annountCement/edit",
        },
        userType:false,
        userIds:[],
        selectedUser:[],
        disabled:false,
        msgContent:"",
        userList:[]
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.disable = false;
        this.visible = true;
      },
      close () {
        this.$emit('close');
        this.selectedUser = [];
        this.visible = false;
        this.$refs.form.resetFields();
      },
      handleOk () {
        this.$emit('close');
        this.selectedUser = [];
        this.visible = false;
        this.$refs.form.resetFields();
      },
      handleCancel () {
        this.visible = false;
        this.$emit('close');
        this.$refs.form.resetFields();
        this.resetUser();
      }
    }
  }
</script>

<style scoped>

</style>