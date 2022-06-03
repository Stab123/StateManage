<template>
  <a-modal
    :title="title"
    :width="200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :okButtonProps="{ props: {disabled: disabled} }"
    cancelText="关闭">
    <span>{{message}}</span>
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
        userList:[],
        message:null
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