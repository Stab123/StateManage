<template>
  <a-modal
    :title="title"
    :visible="visible"
    :width="600"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form-model
      ref="form"
      :rules="validatorRules"
      :model="model"
      :labelCol="labelCol"
      :wrapperCol="wrapperCol"
    >
      <a-form-model-item label="Service_ID" prop="Service_ID">
        <a-input v-model="model.Service_ID"></a-input>
      </a-form-model-item>
      <a-form-model-item label="Service_Chinese_Name" prop="Service_Chinese_Name">
        <a-input v-model="model.Service_Chinese_Name"></a-input>
      </a-form-model-item>
      <a-form-model-item label="Service_English_Name" prop="Service_English_Name">
        <a-input v-model="model.Service_English_Name"></a-input>
      </a-form-model-item>
      <a-form-model-item label="Database_Environment" prop="Database_Environment">
        <a-select v-model="model.Database_Environment">
          <a-select-option v-for="environmentOpt in environmentOptions" :value="environmentOpt">
            {{environmentOpt}}
          </a-select-option>
        </a-select>
      </a-form-model-item>

      <a-form-model-item label="Database_Remote_Service" prop="Database_Remote_Service">
        <a-select v-model="model.Database_Remote_Service">
          <a-select-option v-for="serviceOpt in remoteServiceOptions" :value="serviceOpt">
            {{serviceOpt}}
          </a-select-option>
        </a-select>
      </a-form-model-item>

      <a-form-model-item label="AutoReconnect" prop="AutoReconnect">
        <a-select v-model="model.AutoReconnect">
          <a-select-option v-for="autoReconnectOpt in autoReconnectOptions" :value="autoReconnectOpt">
            {{autoReconnectOpt}}
          </a-select-option>
        </a-select>
      </a-form-model-item>

      <a-form-model-item label="connectTimeout" prop="connectTimeout">
        <a-select v-model="model.connectTimeout">
          <a-select-option v-for="timeoutOpt in connectTimeoutOptions" :value="timeoutOpt">
            {{timeoutOpt}}
          </a-select-option>
        </a-select>
      </a-form-model-item>
    </a-form-model>
  </a-modal>
</template>

<script>
export default {
  name: "DatabaseServiceModal",
  data(){
    return{
      visible: false,
      model:{},
      title: 'Database Service-编辑设置',
      labelCol: {
        xs: { span: 24 },
        sm: { span: 12 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 },
      },
      validatorRules:{
        Service_ID: [{ required: true}],
        Service_Chinese_Name: [{ required: true}],
        Service_English_Name: [{ required: true}],
        Database_Environment: [{ required: true}],
        Database_Remote_Service: [{ required: true}],
        AutoReconnect: [{ required: true}],
        connectTimeout: [{ required: true}],
      },
      environmentOptions: ['normal', 'test'],
      remoteServiceOptions: ['AIDatabase1', 'AIDatabase2'],
      autoReconnectOptions: ['true', 'false'],
      connectTimeoutOptions: [5000, 1000]
    }
  },
  methods:{
    edit(){
      this.visible = true
    },
    close () {
      this.$emit('close');
      this.visible = false;
      // make sure that:
      // 1. form.Item prop attribute has been set
      // 2. value of prop should be the same as binding name of the model
      // for example, prop="test", so v-model should be "model.test"
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