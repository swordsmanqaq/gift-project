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
      <el-table-column prop="name" label="角色名称" width="300" sortable>
      </el-table-column>
      <el-table-column prop="sn" label="唯一标识" width="300" sortable>
      </el-table-column>
      <el-table-column label="操作" width="450">
        <template scope="scope">
          <el-button type="primary" size="small" @click="handleMenu(scope.$index, scope.row)">Set-Menu</el-button>
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">Delete</el-button>
          <el-button type="success" size="small" @click="handlePermission(scope.$index, scope.row)">Set-Permission</el-button>
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
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="saveForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识" prop="sn">
          <el-input type="text" v-model="saveForm.sn" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>

    <!-- 设置权限的编辑页面 -->
    <el-dialog title="设置权限" :visible.sync="setPermissionVisible" :close-on-click-modal="false">
      <el-form :model="rolePermission" label-width="80px" ref="saveForm">
        <!--indeterminate 属性用以表示 checkbox 的不确定状态，一般用于实现全选的效果 -->
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <div class="checkbox-table" v-for="(p,indexkey) in permissionTree" :key="p.sn">
          <template>
            <!--
            一级权限的复选框
            label:就是当前复选框的值
            -->
            <el-checkbox class="check1" style="font-weight: 600;margin-bottom: 15px "
                         v-model='rolePermission.permissionSns' :label="p.sn"
                         @change='handleCheck(1,indexkey)'>
              {{ p.name }}
            </el-checkbox>
            <div style="margin-bottom: 20px;">
              <!--循环一级权限的children 也就是展示二级权限-->
              <div v-for="c in p.children" class="line-check" :key="c.sn"
                   style="display: inline-block; margin-left: 20px;margin-bottom: 20px;">
                <el-checkbox class="check2" @change='handleCheck(2,indexkey)'
                             v-model="rolePermission.permissionSns" :label="c.sn">
                  {{ c.name }}
                </el-checkbox>
              </div>
            </div>
          </template>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="setPermissionVisible = false">Cancel</el-button>
        <el-button type="primary" @click.native="saveRolePermission" :loading="addLoading">Submit</el-button>
      </div>
    </el-dialog>

    <!-- 设置菜单的编辑页面 -->
    <el-dialog title="设置菜单" :visible.sync="setMenuVisible" :close-on-click-modal="false">
      <el-form :model="roleMenu" label-width="80px" ref="saveForm">
        <el-checkbox :indeterminate="isIndeterminates" v-model="checkAlls" @change="checkAllChanges">全选</el-checkbox>
        <div class="checkbox-table" v-for="(p,indexkey) in menuTree" :key="p.id">
          <template>
            <!--
            label:就是当前复选框的值
            -->
            <el-checkbox class="check1" style="font-weight: 600;margin-bottom: 15px "
                         v-model='roleMenu.menuId' :label="p.id"
                         @change='handleCheckMenu(1,indexkey)'>
              {{ p.name }}
            </el-checkbox>
            <div style="margin-bottom: 20px;">
              <!-- 循环一级菜单的children 展示二级菜单 -->
              <div v-for="c in p.children" class="line-check" :key="c.id"
                   style="display: inline-block; margin-left: 20px;margin-bottom: 20px;">
                <el-checkbox class="check2" @change='handleCheckMenu(2,indexkey)'
                             v-model="roleMenu.menuId" :label="c.id">
                  {{ c.name }}
                </el-checkbox>
              </div>
            </div>
          </template>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="setMenuVisible = false">Cancel</el-button>
        <el-button type="primary" @click.native="saveRoleMenu" :loading="addLoading">Submit</el-button>
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
          {required: true, message: '请输入角色名称', trigger: 'blur'}
        ],
        sn: [
          {required: true, message: '请输入唯一标识', trigger: 'blur'}
        ]
      },

      //编辑界面数据
      saveForm: {
        id: null,
        name: '',
        sn: ''
      },

      //设置权限的相关data
      setPermissionVisible: false,   //控制弹框
      rolePermission: {
        roleId: null,
        permissionSns: []   // 存放的是被勾选中的权限,包括一级权限和二级权限
      },
      isIndeterminate: false,  //多选框的状态
      permissionTree: [],   //存放权限树，弹框显示权限数据
      checkAll: false,      //判断是否被全部选中
      allPermissionSns: [],  //用来装所有的权限的标识

      //设置菜单的相关data
      setMenuVisible: false,  //控制弹框
      roleMenu: {
        roleId: null,
        menuId: []   // 存放的是被勾选中的权限,包括一级权限和二级权限
      },
      isIndeterminates: false,  //多选框的状态
      checkAlls: false,      //判断是否被全部选中
      menuTree: [],   //存放权限树，弹框显示权限数据
      allMenuIds: [],  //用来装所有的权限的标识

    }
  },

  methods: {

    //点击全选按钮出发函数
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

    // 处理选择事件  type 1:一级权限2:二级权限   a：一级权限的索引位置
    handleCheck(type, a = 0) {
      // 在已选中的权限列表中，查看是否存在一级权限 >-1 存在  <0 不存在
      let indexOf = this.rolePermission.permissionSns.indexOf(this.permissionTree[a].sn);
      if (type == 2) { // 二级权限点击
        let index = 0;  //
        this.permissionTree[a].children.map(item => {
          // 已选中的权限列表中，是否包含当前一级权限下的某个子权限
          if (this.rolePermission.permissionSns.indexOf(item.sn) > -1) {
            index += 1;
          }
        })
        if (index > 0) {  // 已选中的权限列表中，包含当前一级权限下的某个子权限
          if (indexOf < 0) {  // 已选中的权限列表中，没有当前一级权限，则添加到已选中的权限列表中
            this.rolePermission.permissionSns.push(this.permissionTree[a].sn);
          }
        } else {  // // 已选中的权限列表中，不包含当前一级权限下的某个子权限
          if (indexOf > -1) {// 已选中的权限列表中，有当前一级权限，则删除
            this.rolePermission.permissionSns.splice(indexOf, 1);
          }
        }
      } else {  // 一级菜单点击
        if (indexOf > -1) { // 已选中的权限中，包含当前一级权限，则将该一级权限下所有的子权限选中
          this.permissionTree[a].children.map(item => {
            if (this.rolePermission.permissionSns.findIndex((n) => n == item.sn) < 0) {
              this.rolePermission.permissionSns.push(item.sn)
            }
          })
        } else {
          // 已选中的权限中，不包含当前一级权限，则将该一级权限下所有的子权限移除
          this.permissionTree[a].children.map(item => {
            if (this.rolePermission.permissionSns.findIndex((n) => n == item.sn) > -1) {
              this.rolePermission.permissionSns.splice(this.rolePermission.permissionSns.findIndex((n) => n == item.sn), 1);
            }
          })
        }
      }
      // 获取已选中的长度
      let checkedCount = this.rolePermission.permissionSns.length;
      // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
      this.checkAll = checkedCount === this.allPermissionSns.length;
      // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.allPermissionSns.length;
      console.log(this.rolePermission);
    },

    //做权限数据回显
    getPermissionByRoleId(roleId) {
      this.$http.get("/role/permission/" + roleId)
          .then(result => {
            result = result.data;
            if (result.success) {
              this.rolePermission.permissionSns = result.resultObj;
              // 获取已选中的长度
              let checkedCount = this.rolePermission.permissionSns.length;
              // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
              this.checkAll = checkedCount === this.allPermissionSns.length;
              // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
              this.isIndeterminate = checkedCount > 0 && checkedCount < this.allPermissionSns.length;
            }
          }).catch(result => {
        this.$message({message: '网络错误', type: 'error'});
      })
    },

    //获取权限树
    getPermissionTree() {
      this.$http.get("permission/tree")
          .then(result => {
            result = result.data;
            if (result.success) {
              this.permissionTree = result.resultObj;
            }
          }).catch(result => {
            this.$message({message: '网络错误' + result.message, type: 'error'});
      })
    },

    //获取所有权限的标识
    getAllPermissionSns() {
      this.$http.get("/permission/sns")
          .then(result => {
            result = result.data;
            if (result.success) {
              this.allPermissionSns = result.resultObj;
            }
          }).catch(result => {
        this.$message({message: '网络错误' + result.message, type: 'error'});
      })
    },

    //设置权限提交
    saveRolePermission() {
      this.$confirm('确认提交列表吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.post("/role/permission", this.rolePermission)
            .then(result => {
              result = result.data;
              if (result.success) {
                //关闭弹框
                this.setPermissionVisible = false;
                this.$message({message: result.message, type: 'success'});
              }
            })
            .catch(result => {
              this.$message({message: '网络错误' + result.message, type: 'error'});
            })
      }).catch(() => {
        this.$message({message: '已取消提交', type: 'info'});
      })

    },



    //设置菜单弹框设置
    handleMenu(index ,row){
      this.setMenuVisible = true;
      this.getMenuTree();
      this.getAllMenuIds();
      //获取当前对象的id
      this.roleMenu.roleId = row.id;
      //调用回显函数
      this.getMenuByRoleId(row.id);

    },

    //菜单弹框点击全选按钮出发函数
    checkAllChanges(val) {
      this.roleMenu.menuId = val ? this.allMenuIds : [];
      //改变全选框状态
      this.isIndeterminates = false;

    },

    //获取菜单树
    getMenuTree(){
      this.$http.get("/menu/tree")
          .then( result => {
            result = result.data;
            if (result.success){
              this.menuTree = result.resultObj;
            }
          }).catch( result => {
            this.$message({message: '网络错误' + result.message, type: 'error'});
      })
    },

    //获取所有的菜单id
    getAllMenuIds(){
      this.$http.get("/menu/ids")
          .then( result => {
            result = result.data;
            if (result.success){
              this.allMenuIds = result.resultObj;
            }
          }).catch( result => {
            this.$message({message: '网络错误' + result.message, type: 'error'});
      })
    },

    //做菜单数据回显
    getMenuByRoleId(roleId){
      this.$http.get("role/menu/" + roleId)
          .then( result => {
            result = result.data;
            if (result.success){
              this.roleMenu.menuId = result.resultObj;
              // 获取已选中的长度
              let checkedNumber = this.roleMenu.menuId.length;
              // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
              this.checkAlls = checkedNumber === this.allMenuIds.length;
              // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
              this.isIndeterminates = checkedNumber > 0 && checkedNumber < this.allMenuIds.length;
            }
          }).catch( result => {
            this.$message({message: '网络错误' + result.message, type: 'error'});
      })
    },

    // 处理选择事件  type  1:一级菜单   2:二级菜单   a：一级菜单的索引位置
    handleCheckMenu(type, a = 0) {
      // 在已选中的菜单列表中，查看是否存在一级菜单 >-1 存在  <0 不存在
      let indexOf = this.roleMenu.menuId.indexOf(this.menuTree[a].id);
      if (type == 2) { // 二级菜单点击
        let index = 0;  //
        this.menuTree[a].children.map(item => {
          // 已选中的菜单列表中，是否包含当前一级菜单下的某个子菜单
          if (this.roleMenu.menuId.indexOf(item.id) > -1) {
            index += 1;
          }
        })
        if (index > 0) {  // 已选中的菜单列表中，包含当前一级菜单下的某个子菜单
          if (indexOf < 0) {  // 已选中的菜单列表中，没有当前一级菜单，则添加到已选中的菜单列表中
            this.roleMenu.menuId.push(this.menuTree[a].id);
          }
        } else {  // // 已选中的菜单列表中，不包含当前一级菜单下的某个子权限
          if (indexOf > -1) {// 已选中的菜单列表中，有当前一级菜单，则删除
            this.roleMenu.menuId.splice(indexOf, 1);
          }
        }
      } else {  // 一级菜单点击
        if (indexOf > -1) { // 已选中的菜单中，包含当前一级菜单，则将该一级菜单下所有的子菜单选中
          this.menuTree[a].children.map(item => {
            if (this.roleMenu.menuId.findIndex((n) => n == item.id) < 0) {
              this.roleMenu.menuId.push(item.id)
            }
          })
        } else {
          // 已选中的菜单中，不包含当前一级菜单，则将该一级菜单下所有的子菜单移除
          this.menuTree[a].children.map(item => {
            if (this.roleMenu.menuId.findIndex((n) => n == item.id) > -1) {
              this.roleMenu.menuId.splice(this.roleMenu.menuId.findIndex((n) => n == item.id), 1);
            }
          })
        }
      }
      // 获取已选中的长度
      let checkedNumber = this.roleMenu.menuId.length;
      // 如果已选中的长度==所有权限的长度，则checkAll=true，也就是全选框被选中
      this.checkAlls = checkedNumber === this.allMenuIds.length;
      // 如果已选中的长度>0并且已选中的长度<所有权限的长度，则全选框是一个横杠
      this.isIndeterminates = checkedNumber > 0 && checkedNumber < this.allMenuIds.length;

    },

    //菜单提交
    saveRoleMenu(){
      this.$confirm('确认提交菜单选择吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http.post("/role/menu" ,this.roleMenu)
            .then( result => {
              result = result.data;
              if (result.success){
                //关闭弹框
                this.setMenuVisible = false;
                this.$message({message: result.message, type: 'success'});
              }
            }).catch(result => {
              this.$message({message: result.message, type: 'error'});
            })
      }).catch(() => {
        this.$message({message: '已取消提交', type: 'info'});
      })
    },


    search() {
      this.query.currentPage = 1;
      this.getRoles();
    },

    handleSizeChange(val) {
      this.query.pageSize = val;
      this.query.currentPage = 1;
      this.getRoles();
    },

    handleCurrentChange(val) {
      this.query.currentPage = val;
      this.getRoles();
    },

    selsChange: function (sels) {
      this.sels = sels;
    },

    //获取角色列表
    getRoles() {
      this.$http.post("/role/page", this.query)
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

    //根据id删除
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.delete("/role/" + row.id)
            .then(result => {
              result = result.data;
              this.listLoading = false;
              if (result.success) {
                this.$message({message: '删除成功', type: 'success'});
                this.query.currentPage = 1;
                this.getRoles();
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
        this.$http.patch("/role", ids)
            .then(result => {
              result = result.data;
              this.listLoading = false;
              if (result.success) {
                this.$message({message: '批量删除成功!', type: 'success'});
                this.query.currentPage = 1;
                this.getRoles();
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
    },

    //显示新增界面
    handleAdd: function () {
      this.saveFormVisible = true;
      this.saveForm = {
        id: null,
        name: '',
        sn: ''
      };
    },

    //提交
    saveSubmit: function () {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.addLoading = true;

            this.$http.post("/role/save", this.saveForm)
                .then(result => {
                  result = result.data;
                  if (result.success) {
                    this.query.currentPage = 1;
                    this.getRoles();
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
    this.getRoles();
  }
}


</script>

<style scoped>

</style>
