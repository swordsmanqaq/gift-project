<template>
	<section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;text-align: center">
      <el-form :inline="true" :model="query">
        <el-form-item>
          <el-input v-model="query.keyword" placeholder="关键字" style="width: 500px"></el-input>
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
		<el-table :data="pageInfo.rows" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>
      <el-table-column prop="companyName" label="名称" width="120" sortable>
			</el-table-column>
      <el-table-column prop="companyNum" label="座机" width="130" sortable>
      </el-table-column>
      <el-table-column prop="registerTime" label="注册时间" width="130" sortable>
      </el-table-column>
      <el-table-column prop="address" label="地址" width="150" sortable>
      </el-table-column>
      <el-table-column prop="state" label="状态" width="100" sortable>
        <template slot-scope="scope">
          <span v-if="scope.row.state == 0" style="color: blue;">待审核</span>
          <span v-else-if="scope.row.state == 1" style="color: orange;">待激活</span>
          <span v-else-if="scope.row.state == 2" style="color: red;">审核失败</span>
          <span v-else-if="scope.row.state == 3" style="color: green;">正常</span>
        </template>
      </el-table-column>
      <el-table-column prop="admin.username" label="管理员" width="120" sortable>
      </el-table-column>
      <el-table-column prop="tenantType.name" label="租户类型" width="120" sortable>
      </el-table-column>
			<el-table-column label="操作">
				<template scope="scope">
					<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					<el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
      <el-pagination
          layout="prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-size="query.rows"
          :total="pageInfo.total"
          :current-page="query.page"
          style="float:right;">
      </el-pagination>
		</el-col>

		<!--新增/修改界面-->
		<el-dialog title="新增/修改" :visible.sync="saveFormVisible" :close-on-click-modal="false">
			<el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="saveForm">
				<el-form-item label="名称">
					<el-input v-model="saveForm.key" auto-complete="off"></el-input>
				</el-form-item>
        <el-form-item label="值">
          <el-input v-model="saveForm.value" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="saveForm.intro" auto-complete="off"></el-input>
        </el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="saveFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="save" :loading="addLoading">提交</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
	export default {
		data() {
			return {
				listLoading: false,
				sels: [],//列表选中列
				saveFormVisible: false,//新增界面是否显示
				addLoading: false,
				saveFormRules: {
					name: [
						{ required: true, message: '请输入名称', trigger: 'blur' }
					]
				},
				//新增界面数据
				saveForm: {
					id:null,
          companyName: "",
          companyNum: "",
          registerTime: "",
          state: null,
          address: "",
          logo: ""
				},
        query:{
				  page:1,
          rows:5,
          keyword:""
        },
        pageInfo:{
				  rows:[],
          total:0
        }
			}
		},
		methods: {

      selsChange: function (sels) {
        this.sels = sels;
      },

      search(){
        // 点击 查询时,应该重新从第一页开始查询
        this.query.page = 1;
        this.getTenants();
      },

      //大小改变
      handleSizeChange(val) {
        this.query.rows = val;
        this.query.page = 1;
        this.getTenants();
      },

			handleCurrentChange(val) {
			  // val 当前跳转的页码
				this.query.page = val;
				this.getTenants();
			},

			//获取列表
			getTenants() {
        this.$http.post("/sysmanage/tenant/list",this.query)
            .then(result => {
                result = result.data;
                if(result.success){
                  this.pageInfo = result.resultObj;
                }
            })
			},

			//删除
			handleDel: function (index, row) {
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					this.$http.delete("/sysmanage/tenant/" + row.id)
              .then(result => {
                  result = result.data;
                  if(result.success){
                     // 操作成功:要关闭加载框 刷新当前数据  提示删除成功
                    this.listLoading = false;
                    // 从第一页开始重新进行查询
                    this.search()
                    this.$message({message: '删除成功!', type: 'success'});
                  }else{
                    this.$message({message: result.message, type: 'error'});
                  }
              })
				}).catch(() => {
          this.$message({message: '已取消删除', type: 'info'});
				});
			},

      //批量删除
      batchRemove: function () {
        // 拿到要删除行的ID  this.sels是当前被勾选中的多选框的所有行的对象
        var ids = this.sels.map(item => item.id);
        this.$confirm('确认删除选中记录吗？', '提示', {
          type: 'warning'
        }).then(() => {
          this.listLoading = true;
          this.$http.patch("/sysmanage/tenant",ids)
              .then(result => {
                result = result.data;
                if(result.success){
                  // 操作成功:要关闭加载框 刷新当前数据  提示删除成功
                  this.listLoading = false;
                  // 从第一页开始重新进行查询
                  this.search();
                  this.$message({message: '删除成功!', type: 'success'});
                }else{
                  this.$message({message: result.message, type: 'error'});
                }
              })
        }).catch(() => {

        });
      },

			//显示编辑界面
			handleEdit: function (index, row) {
				// 显示弹框
        this.saveFormVisible = true;
        // row就是当前行对象,所以直接将row赋值给this.saveForm就可以实现回显
				this.saveForm = Object.assign({}, row);
			},

			//显示新增界面
			handleAdd: function () {
        // 显示弹框
				this.saveFormVisible = true;
				// 将this.saveForm中的值赋空值
        this.saveForm = {
          id:null,
          key:"",
          value:"",
          intro:""
        }
			},
			//新增/修改的保存操作
      save: function () {
				this.$refs.saveForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() => {
							this.addLoading = true;
              this.$http.put("/sysmanage/tenant",this.saveForm)
                  .then(result => {
                    result = result.data;
                    if(result.success){
                      this.addLoading = false;
                      this.saveFormVisible = false;
                      // 从第一页开始重新进行查询
                      this.search();
                      this.$message({message: '保存成功!', type: 'success'});
                    }else{
                      this.$message({message: result.message, type: 'error'});
                    }
                  })
						});
					}
				});
			}

		},
		mounted() {
			this.getTenants();
		}
	}

</script>

<style scoped>

</style>