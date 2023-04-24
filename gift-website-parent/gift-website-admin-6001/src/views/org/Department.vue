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
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="name" label="部门名称" width="110" sortable>
      </el-table-column>
      <el-table-column prop="create_time" label="创建时间" width="170" sortable>
      </el-table-column>
      <el-table-column prop="update_time" label="更新时间" width="170" sortable>
      </el-table-column>
      <el-table-column prop="employee.username" label="部门经理" width="110" sortable>
      </el-table-column>
      <el-table-column prop="department.name" label="父部门" width="110" sortable>
      </el-table-column>
      <el-table-column prop="state" label="状态" width="80" sortable>
        <template slot-scope="scope">
          <span v-if="scope.row.state == 1" style="color: green;">启动</span>
          <span v-else style="color: red;">禁用</span>
        </template>
      </el-table-column>
      <el-table-column prop="intro" label="介绍" min-width="180" sortable>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">Batch-Delete</el-button>
      <!--      @size-change 当每页显示条数的值发生改变时会触发-->
      <!--      @current-change 当 当前页的值发生改变时会触发-->
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

    <!--编辑界面-->
    <el-dialog title="新增/修改" :visible.sync="saveFormVisible" :close-on-click-modal="false">
      <el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="addForm">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="saveForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="部门经理">
          <el-select v-model="saveForm.employee" clearable value-key="id" placeholder="请选择">
            <el-option
                v-for="item in employees"
                :key="item.id"
                :label="item.username"
                :value="item">
              <span style="float: left">{{ item.username }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.email }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="父部门">
          <el-cascader v-model="saveForm.department" placeholder="请选择"
                       :options="deptTree"
                       :props="{
                                checkStrictly: true,
                                label: 'name',
                                value: 'id'
                              }"
                       clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="saveForm.state">
            <el-radio class="radio" :label="1">启动</el-radio>
            <el-radio class="radio" :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="介绍">
          <el-input type="textarea" v-model="saveForm.intro"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click.native="saveSubmit" :loading="addLoading">Submit</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>

import employee from "./Employee";

export default {
  data() {
    return {
      pageInfo: {
        rows: [],
        total: 0
      },
      query: {
        currentPage: 1,
        pageSize: 20,
        keyword: ""
      },

      listLoading: false,
      sels: [],//列表选中列

      saveFormVisible: false, //编辑界面是否显示
      addLoading: false,
      saveFormRules: {
        name: [
          {required: true, message: '请输入部门名称', trigger: 'blur'}
        ]
      },

      //编辑界面数据
      saveForm: {
        id: null,
        name: '',
        employee: {
          id: null,
          username: ""
        },
        department: {
          id: null,
          name: ""
        },
        state: null,
        intro: ""
      },

      employees: [],
      deptTree: []

    }
  },
  methods: {
    search() {
      // 设置query.currengPage为1,当进行搜索时,从第一页开始重新进行搜索
      this.query.currentPage = 1;
      //调用查询
      this.getDepartments();
    },

    handleSizeChange(val) {
      this.query.pageSize = val;
      this.query.currentPage = 1;
      //查询结果
      this.getDepartments();
    },

    handleCurrentChange(val) {
      // val为当前页，赋值后重新进行查询
      this.query.currentPage = val;
      // 调用查询方法
      this.getDepartments();
    },

    //获取部门列表列表
    getDepartments() {
      this.$http.post("/department/page", this.query)
          .then(result => {
            result = result.data;
            if (result.success) {
              this.pageInfo = result.resultObj;
            } else {
              this.$message({message: '分页查询失败' + result.message, type: 'error'});
            }
          })
          .catch(result => {

          })
    },

    //删除
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.delete("/department/" + row.id)
            .then(result => {
              result = result.data;
              this.listLoading = false;
              if (result.success) {
                this.$message({message: '删除成功', type: 'success'});
                //删除成功后回到第一页
                this.query.currentPage = 1;
                // 删除成功,要重新刷新该页面，调用方法查询
                this.getDepartments();
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

    //获取所有员工
    getAllEmployees() {
      this.$http.get("/employee")
          .then(result => {
            result = result.data;
            if (result.success){
              this.employees = result.resultObj;
            }else {
              this.$message({message: '查询失败' + result.message, type: 'error'});
            }
          }).catch(result => {
            this.$message({message: result.message, type: 'error'});
      })
    },

    //获取部门树
    getDeptTree(){
      this.$http.get("/department/tree")
          .then(result => {
            result = result.data;
            if (result.success){
              this.deptTree = result.resultObj;
            }else {
              this.$message({message: '查询部门树失败' + result.message, type: 'error'});
            }
          }).catch(result => {
            this.$message({message: '网络错误' + result.message, type: 'error'});
      })
    },

    //显示修改界面
    handleEdit: function (index, row) {
      this.saveFormVisible = true;
      this.saveForm = Object.assign({}, row);
      console.log(row)
      //如果没有部门经理就为空
      if (!row.employee){
        this.saveForm.employee = {
          id: null,
          username: ""
        }
      }
      if (!row.department){
        this.saveForm.department = {
          id: null,
          name: ""
        }
      }
      var split = row.path.split("/");
      var list = [];
      for (var i = 1; i < split.length - 1; i++){
        list.push(parseInt(split[i]));
      }
      this.saveForm.department = list;
      console.log(list);
      //调用方法让下拉框有值
      this.getAllEmployees();
      this.getDeptTree();
    },

    //显示新增界面
    handleAdd: function () {
      this.saveFormVisible = true;
      //调用员工方法，给下拉框增加数据
      this.getAllEmployees();
      //增加部门数据
      this.getDeptTree();
      this.saveForm = {
        id: null,
        name: '',
        employee: {
          id: null,
          username: ""
        },
        department: {
          id: null,
          name: ""
        },
        state: null,
        intro: ""
      };
    },

    //提交
    saveSubmit: function () {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.addLoading = true;
            //NProgress.start();
            var para = this.saveForm
            var arrays = para.department;
            if (arrays && arrays.length){
              para.department = {id:arrays[arrays.length - 1]};
            }else {
              para.department = {id: null};
            }
            this.$http.post("/department/save",para)
                .then( result => {
                  result = result.data;
                  if (result.success){
                    //从第一页开始展示
                    this.query.currentPage = 1;
                    this.getDepartments();
                    //关闭加载框
                    this.addLoading = false;
                    //关闭弹框
                    this.saveFormVisible = false;
                    this.$message({message: '保存成功', type: 'success'});
                  }
                }).catch( result => {
                  this.$message({message: '网络错误', type: 'error'});
            })
          });
        }
      });
    },

    selsChange: function (sels) {
      this.sels = sels;
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
        this.$http.patch("/department", ids)
            .then(result => {
              result = result.data;
              this.listLoading = false;
              if (result.success) {
                this.$message({message: '批量删除成功!', type: 'success'});
                //删除后回到第一页
                this.query.currentPage = 1;
                //调用查询
                this.getDepartments();
              } else {
                this.$message({message: result.message, type: 'error'});
              }
            })
      }).catch(() => {

      });
    }
  },
  mounted() {
    // 钩子函数，页面加载后，调用此方法，加载部门
    this.getDepartments();
  }
}

</script>

<style scoped>

</style>
