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
          <a-col :span="20/2">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="domain"
              label="功能域">
              <a-input placeholder="请输入功能域" v-model="model.domain" :readOnly="disableSubmit"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="20/2">
            <a-form-model-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              prop="framework"
              label="代码框架">
              <a-input placeholder="请输入代码框架" v-model="model.framework" :readOnly="disableSubmit"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row style="width: 100%;">
          <a-col :span="20">
            <a-form-model-item
              :labelCol="{span: 3}"
              :wrapperCol="{span: 21}"
              prop="name"
              label="名称">
              <a-input placeholder="请输入名称" v-model="model.name" :readOnly="disableSubmit"/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
export default {
  name: "FrameworkModal",
  data () {
    return {
      title:"操作",
      visible: false,
      disableSubmit:false,
      disabled:false,
      confirmLoading: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 18 },
      },
      validatorRules:{
        domain: [{ required: true, message: '请输入功能域！' }],
        framework: [{ required: true, message: '请输入代码框架!' }],
        name:[{ required: true, message: '请输入名称!' }],
      },
    }
  },
  created () {
  },
  methods: {
    add () {
      this.visible = true;
    },
    close () {
      this.$emit('close');
      this.visible = false;
      this.$refs.form.resetFields();
    },
    handleOk () {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true;

          // 异步
          that.confirmLoading = false;
          that.close()
        }else{
          return false;
        }
      })
    },
    handleCancel () {
      this.visible = false;
      this.$emit('close');
      this.$refs.form.resetFields();
    },
  }
}
</script>

<style scoped>

</style>