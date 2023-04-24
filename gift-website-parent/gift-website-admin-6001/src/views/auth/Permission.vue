<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;text-align: center">
      <el-form :inline="true" :model="query">
        <el-form-item>
          <el-input v-model="query.keyword" placeholder="Keyword" style="width: 500px"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="search">Search</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表-->
    <el-table :data="pageInfo.rows" highlight-current-row v-loading="listLoading" style="width: 100%;">
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="name" label="权限名称" width="200" sortable>
      </el-table-column>
      <el-table-column prop="url" label="路径" width="240" sortable>
      </el-table-column>
      <el-table-column prop="sn" label="唯一标识" width="280" sortable>
      </el-table-column>
      <el-table-column prop="parent.name" label="父权限" width="180" sortable>
      </el-table-column>
      <el-table-column prop="descs" label="描述" min-width="250" sortable>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[5, 15, 25, 35]"
          :page-size="query.pageSize" layout="total, sizes, prev, pager, next, jumper"
          :total="pageInfo.total"
          :current-page="query.currentPage"
          style="float: right">
      </el-pagination>
    </el-col>

  </section>
</template>

<script>


export default {
  data() {
    return {
      pageInfo: {
        rows: [],
        total: 0
      },
      query: {
        currentPage: 1,
        pageSize: 15,
        keyword: ""
      },
      listLoading: false,

    }
  },
  methods: {
    search() {
      // 设置query.currengPage为1,当进行搜索时,从第一页开始重新进行搜索
      this.query.currentPage = 1;
      //调用查询
      this.getPermission();
    },

    //当前页展示数据大小改变
    handleSizeChange(val) {
      this.query.pageSize = val;
      this.query.currentPage = 1;
      //查询结果
      this.getPermission();
    },

    //当前页改变
    handleCurrentChange(val) {
      // val为当前页，赋值后重新进行查询
      this.query.currentPage = val;
      // 调用查询方法
      this.getPermission();
    },

    //获取权限列表
    getPermission() {
      this.$http.post("/permission", this.query)
          .then(result => {
            result = result.data;
            if (result.success) {
              this.pageInfo = result.resultObj;
              console.log(result.resultObj);
            } else {
              this.$message({message: '权限分页查询失败' + result.message, type: 'error'});
            }
          })
          .catch(result => {

          })
    }
  },
  mounted() {
    //页面加载后，执行
    this.getPermission();
  }
}

</script>

<style scoped>

</style>
