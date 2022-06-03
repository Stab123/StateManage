<template>
  <div>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="模型训练">
        <a-row>
          <a-col :span="2">
            训练进度
          </a-col>
          <a-col :span="6">
            <a-progress
              :percent="trainProgress"
              :stroke-color="{
                '0%': '#108ee9',
                '100%': '#87d068',
                }"
            />
          </a-col>
        </a-row>
        <a-divider/>
        <div
          style="margin-top: 20px"
        >
          最近一次训练结果：
          <br/>
          <br/>
          <figure>
              <img src="~@/views/ai/demo1.jpg"/>
              <figcaption>损失函数曲线</figcaption>
            </figure>
        </div>
      </a-tab-pane >
      <a-tab-pane key="2" tab="模型评估">
        <a-row>
          <a-col :span="2">
            评估进度
          </a-col>
          <a-col :span="6">
            <a-progress
              :percent="evaluateProgress"
              :stroke-color="{
                '0%': '#108ee9',
                '100%': '#87d068',
                }"
            />
          </a-col>
        </a-row>
        <a-divider/>
        <div
          style="margin-top: 20px"
        >
          最近一次评估结果：
          <br/>
          <br/>
          <figure>
            <img src="~@/views/ai/demo1.jpg"/>
            <figcaption>损失函数曲线</figcaption>
          </figure>
          <br/>
          <br/>
          <figure>
            <img src="~@/views/ai/demo2.png"/>
            <figcaption>准确率曲线</figcaption>
          </figure>
        </div>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
export default {
  name: "AIModelDetail",
  data(){
    return {
      trainProgress: 0,
      evaluateProgress: 0
    }
  },
  created() {
    this.trainProgress = 67
    this.evaluateProgress = 75
  }
}
</script>

<style scoped>
figure {
  border: thin #c0c0c0 solid;
  display: flex;
  flex-flow: column;
  padding: 5px;
  max-width: 600px;
  margin: auto;
}
figcaption {
  background-color: #222;
  color: #fff;
  font: italic smaller sans-serif;
  padding: 3px;
  text-align: center;
}
</style>