<template>
  <a-breadcrumb separator=">">
    <a-breadcrumb-item>所有框架</a-breadcrumb-item>
  </a-breadcrumb>
</template>

<script>
export default {
  name: 'BreadCrumb',
  data(){
    return{
      router:[]
    }
  },
  created() {
    const routerArray = this.$route.fullPath.split('/')
    console.log(routerArray)
    for(let {path, index} in routerArray) {
      console.log(path, '@', index)
    }
  }
}
</script>

<style scoped>

</style>