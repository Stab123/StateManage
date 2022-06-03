<template>
  <a-modal
    :title="title"
    :visible="visible"
    :width="900"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form-model ref="form" :model="model" :rules="validatorRules">
      <a-row style="width: 100%;">
        <a-col :span="20/2">
          <a-form-model-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            prop="batch_size"
            label="batch_size">
            <a-input v-model="model.batch_size"/>
          </a-form-model-item>
        </a-col>
        <a-col :span="20/2">
          <a-form-model-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            prop="iter_times"
            label="迭代次数">
            <a-input v-model="model.iter_times"/>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row style="width: 100%;">
        <a-col :span="20/2">
          <a-form-model-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            prop="train_set_path"
            label="训练集路径">
            <a-input v-model="model.train_set_path"/>
          </a-form-model-item>
        </a-col>
        <a-col :span="20/2">
          <a-form-model-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            prop="validation_set_path"
            label="验证集路径">
            <a-input v-model="model.validation_set_path"/>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row style="width: 100%;">
        <a-col :span="20">
          <a-form-model-item
            :labelCol="{span: 4}"
            :wrapperCol="{span: 20}"
            prop="pretrain_model_path"
            label="预训练模型路径">
            <a-input v-model="model.pretrain_model_path"/>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
  </a-modal>
</template>

<script>
function toNumber(v) {
  if (!v || !v.trim()) {
    return undefined;
  }
  let num = Number(v);
  // num === ' '
  if (!isNaN(num)) {
    num = parseInt(v, 10);
  }
  return isNaN(num) ? v : num;
}
export default {
  name: "TrainParamModal",
  data(){
    return{
      modelName:"",
      visible: false,
      model:{},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      validatorRules:{
        batch_size: [{ required: true},{type: 'number', transform: (value)=>toNumber(value)}],
        iter_times: [{ required: true},{type: 'number', transform: (value)=>toNumber(value)}],
        train_set_path: [{ required: true}],
        validation_set_path: [{ required: true}],
        pretrain_model_path: [{ required: true}],
      },
    }
  },
  computed:{
    title(){
      return this.modelName + '-超参数设置'
    }
  },
  methods:{
    paramSet(modelName){
      this.modelName = modelName
      this.visible = true
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