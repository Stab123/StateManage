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
      <a-form-model-item label="AI_Detect_Model" prop="AI_Detect_Model">
        <a-select v-model="model.AI_Detect_Model">
          <a-select-option v-for="modeOpt in ModelOptions" :value="modeOpt">
            {{modeOpt}}
          </a-select-option>
        </a-select>
      </a-form-model-item>

      <a-form-model-item label="AI_Detect_Engine" prop="AI_Detect_Engine">
        <a-select v-model="model.AI_Detect_Engine">
          <a-select-option v-for="engineOpt in EngineOptions" :value="engineOpt">
            {{engineOpt}}
          </a-select-option>
        </a-select>
      </a-form-model-item>

      <a-form-model-item label="AI_Detect_Version" prop="AI_Detect_Version">
        <a-select v-model="model.AI_Detect_Version">
          <a-select-option v-for="versionOpt in VersionOptions" :value="versionOpt">
            {{versionOpt}}
          </a-select-option>
        </a-select>
      </a-form-model-item>
    </a-form-model>
  </a-modal>
</template>

<script>
export default {
  name: "AIDetectServiceModal",
  data(){
    return{
      visible: false,
      model:{},
      title: 'AI Detect Service-编辑设置',
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
        AI_Detect_Model: [{ required: true}],
        AI_Detect_Engine: [{ required: true}],
        AI_Detect_Version: [{ required: true}],
      },
        ModelOptions: ['modelAI', 'Test1', 'Test2'],
        EngineOptions: ['Yolo', 'Test1'],
        VersionOptions: ['1.0', '2.0']
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