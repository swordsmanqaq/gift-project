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
        <el-form-item>
          <el-button type="primary" @click="handleAdd">Add</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表-->
    <el-table :data="pageInfo.rows" highlight-current-row v-loading="listLoading" @selection-change="selsChange"
              style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column type="index" width="100">
      </el-table-column>
      <el-table-column prop="name" label="菜单名称" width="180" sortable>
      </el-table-column>
      <el-table-column prop="url" label="路径" width="200" sortable>
      </el-table-column>
      <el-table-column prop="icon" label="图标" width="200" sortable>
      </el-table-column>
      <el-table-column prop="menu.name" label="父菜单" width="200" sortable>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">Batch-Delete</el-button>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="query.pageSize" layout="total, sizes, prev, pager, next, jumper"
          :total="pageInfo.total"
          :current-page="query.currentPage"
          style="float: right">
      </el-pagination>
    </el-col>

    <!--新增/修改 编辑界面-->
    <el-dialog title="新增/修改" :visible.sync="saveFormVisible" :close-on-click-modal="false">
      <el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="addForm">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="saveForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input type="text" v-model="saveForm.url" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input type="text" v-model="saveForm.icon" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="父菜单">
          <el-select v-model="saveForm.menu" clearable value-key="id" placeholder="请选择菜单">
            <el-option
                v-for="item in menus"
                :key="item.id"
                :label="item.name"
                :value="item">
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.url }}</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>

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
        pageSize: 10,
        keyword: ""
      },

      listLoading: false,
      sels: [],//列表选中列

      saveFormVisible: false, //编辑界面是否显示
      addLoading: false,
      saveFormRules: {
        name: [
          {required: true, message: '请输入菜单名称', trigger: 'blur'}
        ],
      },

      //编辑界面数据
      saveForm: {
        id: null,
        name: '',
        url: '',
        icon: '',
        menu:{
          id: null,
          name: ''
        }
      },

      menus:[]
    }
  },

  methods: {

    handleCheckAllChange(val) {
      this.rolePermission.permissionSns = val ? this.allPermissionSns : [];
      //改变全选框状态
      this.isIndeterminate = false;

    },

    //设置权限弹框设置
    handlePermission(index, row) {
      this.setPermissionVisible = true;
      //获取所有权限展示数据
      this.getPermissionTree();
      //获取所有权限标识
      this.getAllPermissionSns();
      this.rolePermission.roleId = row.id;
      //调用getPermissionByRoleId，将后端返回来的数据赋值给rolePermission.permissionSns做数据回显
      this.getPermissionByRoleId(row.id);
    },

    search() {
      this.query.currentPage = 1;
      this.getMenus();
    },

    handleSizeChange(val) {
      this.query.pageSize = val;
      this.query.currentPage = 1;
      this.getMenus();
    },

    handleCurrentChange(val) {
      this.query.currentPage = val;
      this.getMenus();
    },

    selsChange: function (sels) {
      this.sels = sels;
    },

    //获取菜单
    getMenus() {
      this.$http.post("/menu/page", this.query)
          .then(result => {
            result = result.data;
            if (result.success) {
              this.pageInfo = result.resultObj;
            } else {
              this.$message({message: '分页查询失败' + result.message, type: 'error'});
            }
          })
          .catch(result => {
            this.$message({message: '请登录后访问', type: 'error'});
          })
    },

    //获取全部菜单
    getAllMenus(){
      this.$http.get("/menu")
          .then( result => {
            result = result.data;
            if (result.success){
              this.menus = result.resultObj;
            }else {
              this.$message({message: '查询失败', type: 'error'});
            }
          }).catch( result => {
            this.$message({message: '网络错误', type: 'error'});
      })
    },

    //根据id删除
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.delete("/menu/" + row.id)
            .then(result => {
              result = result.data;
              this.listLoading = false;
              if (result.success) {
                this.$message({message: '删除成功', type: 'success'});
                this.query.currentPage = 1;
                this.getMenus();
              } else {
                this.$message({message: result.message, type: 'error'});
              }
            })
            .catch(result => {
              this.$message({message: '很抱歉，网络有误', type: 'error'});
            })
      }).catch(() => {
        this.$message({message: '已取消删除', type: 'info'});
      });
    },

    //批量删除
    batchRemove: function () {
      //遍历选中的sels数组的id值给ids
      var ids = this.sels.map(item => item.id);
      this.$confirm('确认删除选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        // 调用批量删除接口
        this.$http.patch("/menu", ids)
            .then(result => {
              result = result.data;
              this.listLoading = false;
              if (result.success) {
                this.$message({message: '批量删除成功!', type: 'success'});
                this.query.currentPage = 1;
                this.getMenus();
              } else {
                this.$message({message: result.message, type: 'error'});
              }
            })
      }).catch(() => {

      });
    },

    //显示修改界面
    handleEdit: function (index, row) {
      this.saveFormVisible = true;
      this.saveForm = Object.assign({}, row);
      //渲染下拉框
      this.getAllMenus();
    },

    //显示新增界面
    handleAdd: function () {
      this.saveFormVisible = true;
      this.getAllMenus();
      this.saveForm = {
        id: null,
        name: '',
        url: '',
        icon: '',
        menu:{
          id: null,
          name: ''
        }
      };
    },

    //提交
    saveSubmit: function () {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.addLoading = true;
            this.$http.post("/menu/save", this.saveForm)
                .then(result => {
                  result = result.data;
                  if (result.success) {
                    this.query.currentPage = 1;
                    this.getMenus();
                    this.addLoading = false;
                    this.saveFormVisible = false;
                    this.$message({message: '保存成功', type: 'success'});
                  }
                }).catch(result => {
              this.$message({message: '网络错误', type: 'error'});
            })
          });
        }
      });
    }

  },

  mounted() {
    // 钩子函数，页面加载后，调用此方法
    this.getMenus();
  }
}


</script>

<style scoped>

</style>
