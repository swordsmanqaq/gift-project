<template>
	<el-row class="container">
		<el-col :span="24" class="header">
			<el-col :span="10" class="logo" :class="collapsed?'logo-collapse-width':'logo-width'">
				{{collapsed?'':sysName}}
			</el-col>
			<el-col :span="10">
				<div class="tools" @click.prevent="collapse">
					<i class="fa fa-align-justify"></i>
				</div>
			</el-col>
			<el-col :span="4" class="userinfo">
				<el-dropdown trigger="hover">
					<span class="el-dropdown-link userinfo-inner"><img :src="this.sysUserAvatar" /> {{sysUserName}}</span>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item>我的消息</el-dropdown-item>
						<el-dropdown-item>设置</el-dropdown-item>
						<el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</el-col>
		</el-col>
		<el-col :span="24" class="main">
			<aside :class="collapsed?'menu-collapsed':'menu-expanded'">
				<!--导航菜单-->
				<el-menu :default-active="$route.path" class="el-menu-vertical-demo" @open="handleopen" @close="handleclose"
                 @select="handleselect"
					 unique-opened router v-show="!collapsed">
          <!--首页应该是每个员工进来之后都能看到的,并且首页应该在最上面,所以就将首页这个菜单在上面写死-->
          <!--<el-menu-item index="/echarts">
            <i class="fa fa-bar-chart"></i>
             首页
          </el-menu-item>
          &lt;!&ndash;动态展示菜单&ndash;&gt;
          <template v-for="(item,index) in menuTree">
            <el-submenu :index="index+''">
              &lt;!&ndash;一级菜单&ndash;&gt;
              <template slot="title">
                <i :class="item.icon"></i>
                {{item.name}}
              </template>
              &lt;!&ndash;循环二级菜单&ndash;&gt;
              <el-menu-item v-for="child in item.children"
                            :index="child.url" :key="child.url">
                {{child.name}}
              </el-menu-item>
            </el-submenu>
          </template>-->
          <!--v-if="!item.hidden" 如果当前hidden=false 才展示-->
					<template v-for="(item,index) in $router.options.routes" v-if="!item.hidden">
            <!--:index="index+''" 相当于给当前这个菜单一个唯一标识
            v-if="!item.leaf" 只展示不是叶子节点的菜单-->
						<el-submenu :index="index+''" v-if="!item.leaf">
              <!--一级菜单-->
							<template slot="title">
                <i :class="item.iconCls"></i>
                {{item.name}}
              </template>
              <!--循环二级菜单-->
							<el-menu-item v-for="child in item.children"
                            :index="child.path" :key="child.path"
                            v-if="!child.hidden">
                {{child.name}}
              </el-menu-item>
						</el-submenu>
            <!--展示是叶子节点的情况-->
						<el-menu-item v-if="item.leaf&&item.children.length>0"
                          :index="item.children[0].path">
              <i :class="item.iconCls"></i>
              {{item.children[0].name}}
            </el-menu-item>
					</template>
				</el-menu>
			</aside>
			<section class="content-container">
				<div class="grid-content bg-purple-light">
					<el-col :span="24" class="breadcrumb-container">
						<strong class="title">{{$route.name}}</strong>
						<el-breadcrumb separator="/" class="breadcrumb-inner">
							<el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
								{{ item.name }}
							</el-breadcrumb-item>
						</el-breadcrumb>
					</el-col>
					<el-col :span="24" class="content-wrapper">
						<transition name="fade" mode="out-in">
							<router-view></router-view>
						</transition>
					</el-col>
				</div>
			</section>
		</el-col>
	</el-row>
</template>

<script>
	export default {
		data() {
			return {
				sysName:'蓉礼购平台',
				collapsed:false,
				sysUserName: '',
				sysUserAvatar: '',
				form: {
					name: '',
					region: '',
					date1: '',
					date2: '',
					delivery: false,
					type: [],
					resource: '',
					desc: ''
				},
        menuTree:[]
			}
		},
		methods: {
			onSubmit() {
				console.log('submit!');
			},
			handleopen() {
				//console.log('handleopen');
			},
			handleclose() {
				//console.log('handleclose');
			},
			handleselect: function (a, b) {
			},
			//退出登录
			logout: function () {
				var _this = this;
				this.$confirm('确认退出吗?', '提示', {
					//type: 'warning'
				}).then(() => {
				  // 调用后端 退出登录接口
          this.$http.post("/logout")
              .then(result => {
                  result = result.data;
                  if(result.success){
                    // 删除浏览器中的token和loginUser,并跳转回登录页面
                    localStorage.removeItem('loginUser');
                    localStorage.removeItem('token');
                    _this.$router.push('/login');
                  }else{
                    // 如果退出登录失败,就要提示错误信息
                    this.$message({type:"error",message:result.message})
                  }
              })
				}).catch(() => {

				});
			},
			//折叠导航栏
			collapse:function(){
				this.collapsed=!this.collapsed;
			},
			showMenu(i,status){
				this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-'+i)[0].style.display=status?'block':'none';
			},
      getMenuTree(loginUserId){
			  this.$http.get("/menu/tree/"+loginUserId)
            .then(result => {
                result = result.data;
                if(result.success){
                  // 查询成功,将后端返回的菜单数组赋值
                  this.menuTree = result.resultObj;
                }
            })
      }
		},
		mounted() {
			var user = localStorage.getItem('loginUser');
			if (user) {
				user = JSON.parse(user);
				// 展示当前登录用户名和头像
				this.sysUserName = user.username || '';
				this.sysUserAvatar = user.headImage || '';
				// 根据用户ID获取菜单
        this.getMenuTree(user.id);
			}
		}
	}

</script>

<style scoped lang="scss">
	@import '~scss_vars';
	
	.container {
		position: absolute;
		top: 0px;
		bottom: 0px;
		width: 100%;
		.header {
			height: 60px;
			line-height: 60px;
			background: $color-primary;
			color:#fff;
			.userinfo {
				text-align: right;
				padding-right: 35px;
				float: right;
				.userinfo-inner {
					cursor: pointer;
					color:#fff;
					img {
						width: 40px;
						height: 40px;
						border-radius: 20px;
						margin: 10px 0px 10px 10px;
						float: right;
					}
				}
			}
			.logo {
				//width:230px;
				height:60px;
				font-size: 22px;
				padding-left:20px;
				padding-right:20px;
				border-color: rgba(238,241,146,0.3);
				border-right-width: 1px;
				border-right-style: solid;
				img {
					width: 40px;
					float: left;
					margin: 10px 10px 10px 18px;
				}
				.txt {
					color:#fff;
				}
			}
			.logo-width{
				width:230px;
			}
			.logo-collapse-width{
				width:60px
			}
			.tools{
				padding: 0px 23px;
				width:14px;
				height: 60px;
				line-height: 60px;
				cursor: pointer;
			}
		}
		.main {
			display: flex;
			// background: #324057;
			position: absolute;
			top: 60px;
			bottom: 0px;
			overflow: hidden;
			aside {
				flex:0 0 230px;
				width: 230px;
				// position: absolute;
				// top: 0px;
				// bottom: 0px;
				.el-menu{
					height: 100%;
				}
				.collapsed{
					width:60px;
					.item{
						position: relative;
					}
					.submenu{
						position:absolute;
						top:0px;
						left:60px;
						z-index:99999;
						height:auto;
						display:none;
					}

				}
			}
			.menu-collapsed{
				flex:0 0 60px;
				width: 60px;
			}
			.menu-expanded{
				flex:0 0 230px;
				width: 230px;
			}
			.content-container {
				// background: #f1f2f7;
				flex:1;
				// position: absolute;
				// right: 0px;
				// top: 0px;
				// bottom: 0px;
				// left: 230px;
				overflow-y: scroll;
				padding: 20px;
				.breadcrumb-container {
					//margin-bottom: 15px;
					.title {
						width: 200px;
						float: left;
						color: #475669;
					}
					.breadcrumb-inner {
						float: right;
					}
				}
				.content-wrapper {
					background-color: #fff;
					box-sizing: border-box;
				}
			}
		}
	}
</style>