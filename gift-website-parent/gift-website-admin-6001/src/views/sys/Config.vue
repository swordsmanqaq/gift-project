<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="query">
				<el-form-item>
					<el-input v-model="query.keyword" placeholder="关键字"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" v-on:click="search">查询</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">新增</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="pageInfo.rows" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>
			<el-table-column prop="key" label="键" width="180" sortable>
			</el-table-column>
      <!--:formatter 绑定该列的格式化规则-->
			<el-table-column prop="value" label="值" width="180" sortable>
			</el-table-column>
      <el-table-column prop="intro" label="描述" width="240" sortable>
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
          :current-page="query.currentPage"
          @current-change="handleCurrentChange"
          :page-size="query.pageSize"
          :total="pageInfo.total"
          style="float:right;">
			</el-pagination>
		</el-col>

		<!--新增/修改界面-->
		<el-dialog title="新增/修改" :visible.sync="saveFormVisible" :close-on-click-modal="false">
			<el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="saveForm">
				<el-form-item label="键">
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
					],
          sn: [
            { required: true, message: '请输入标识', trigger: 'blur' }
          ]
				},
				//新增界面数据
				saveForm: {
					id:null,
          key:"",
          value:"",
          intro:""
				},
        query:{
				  currentPage:1,
          pageSize:5,
          keyword:""
        },
        pageInfo:{
				  rows:[],
          total:0
        }
			}
		},
		methods: {
      search(){
        // 点击 查询时,应该重新从第一页开始查询
        this.query.currentPage = 1;
        this.getConfigs();
      },
			handleCurrentChange(val) {
			  // val 当前跳转的页码
				this.query.currentPage = val;
				this.getConfigs();
			},
			//获取角色列表
			getConfigs() {
				// 通过axios调用后端分页接口
        this.$http.post("/config",this.query)
            .then(result => {
                result = result.data;
                if(result.success){
                   // 要展示table的数据,渲染分页条
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
					this.$http.delete("/config/"+row.id)
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
              this.$http.put("/config",this.saveForm)
                  .then(result => {
                    result = result.data;
                    if(result.success){
                      // 操作成功:要关闭加载框 刷新当前数据  提示删除成功
                      this.addLoading = false;
                      // 关闭弹框
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
			},
			selsChange: function (sels) {
				this.sels = sels;
			},
			//批量删除
			batchRemove: function () {
        // 拿到要删除行的ID  this.sels是当前被勾选中的多选框的所有行的对象
				var ids = this.sels.map(item => item.id);
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
          this.$http.patch("/config",ids)
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
			}
		},
		mounted() {
			this.getConfigs();
		}
	}

</script>

<style scoped>

</style>