<template>
  <div>
    <!--:model="tenant" 数据双向绑定-->
    <!--ref="tenantForm" id="tenantForm",给form去一个名字-->
    <!--:rules="formRules" 校验规则-->
    <el-form :model="tenant" ref="tenantForm" :rules="formRules" label-position="left" label-width="100px"
             class="demo-ruleForm login-container">
      <h3 class="title">租户入驻</h3>
      <el-form-item prop="companyName" label="名称">
        <el-input type="text" v-model="tenant.companyName" auto-complete="off" placeholder="请输入租户名称！"></el-input>
      </el-form-item>
      <el-form-item prop="mealId" label="套餐">
        <el-select v-model="tenant.mealId" placeholder="请选择">
          <el-option
              v-for="meal in meals"
              :key="meal.id"
              :label="meal.mealName"
              :value="meal.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="companyNum" label="座机">
        <el-input type="text" v-model="tenant.companyNum" auto-complete="off" placeholder="请输入座机！"></el-input>
      </el-form-item>
      <el-form-item prop="address" label="租户地址">
        <el-input type="text" v-model="tenant.address" auto-complete="off" placeholder="请输入地址！"></el-input>
        <el-button size="small" type="primary" @click="selectAdrress">选择</el-button>
      </el-form-item>
      <el-form-item prop="logo" label="租户Logo">
        <!--
        action 后端上传文件的controller接口地址  默认是post提交
        on-preview   点击预览图片时,触发的事件
        on-remove    点击删除按钮时,触发的事件
        on-success   上传成功以后触发的事件
        -->
        <el-upload
            class="upload-demo"
            action="https://bucket-gift.oss-cn-chengdu.aliyuncs.com"
            :data="uploadData"
            :before-upload="beforeUpload"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-success="handleSuccess"
            :file-list="fileList"
            list-type="picture">
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
      </el-form-item>
      <h3 class="title">管理员信息设置</h3>
      <el-form-item prop="admin.username" label="账号">
        <el-input type="text" v-model="tenant.admin.username" auto-complete="off" placeholder="请输入账号！"></el-input>
      </el-form-item>
      <el-form-item prop="admin.tel" label="手机号码">
        <el-input type="text" v-model="tenant.admin.tel" auto-complete="off" placeholder="请输入电话！"></el-input>
      </el-form-item>
      <el-form-item prop="admin.email" label="电子邮件">
        <el-input type="text" v-model="tenant.admin.email" auto-complete="off" placeholder="请输入邮件！"></el-input>
      </el-form-item>
      <el-form-item prop="admin.password" label="密码">
        <el-input type="password" v-model="tenant.admin.password" auto-complete="off" placeholder="请输入密码！"></el-input>
      </el-form-item>
      <el-form-item prop="admin.confirmPassword" label="确认密码">
        <el-input type="password" v-model="tenant.admin.confirmPassword" auto-complete="off"
                  placeholder="请输入确认密码！"></el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button type="primary" style="width:100%;" @click.native.prevent="settledIn">入驻</el-button>
      </el-form-item>
    </el-form>
    <el-dialog
        title="选择地址"
        :visible.sync="dialogVisable"
        width="30%">
      <baidu-map :center="{lng: 116.403765, lat: 39.914850}" :zoom="11">
        <bm-view class="map"></bm-view>
        <bm-control :offset="{width: '10px', height: '10px'}">
          <!--:sugStyle="{zIndex: 2100} 让搜索提示上浮-->
          <bm-auto-complete v-model="keyword" :sugStyle="{zIndex: 2100}">
            <div style="margin-bottom:10px">
              <input id="searchInput" type="text" placeholder="请输入关键字" class="searchinput"/>
              <el-button type="success" @click="selectAdrressConfirm">确定</el-button>
            </div>
          </bm-auto-complete>
        </bm-control>
        <bm-local-search :keyword="keyword" :auto-viewport="true"></bm-local-search>
      </baidu-map>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisable=false">取 消</el-button>
        <el-button type="primary" @click="dialogVisable=false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Vue from 'vue';
import BaiduMap from 'vue-baidu-map';

Vue.use(BaiduMap, {
  // ak 百度地图开发者平台申请的密钥
  ak: 'UMEIVdmcIxcVUYtPAbGG2E4V7pYqkP2h'
});
export default {
  data() {
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.tenant.admin.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      //提交到OSS的参数
      uploadData: {
        policy: '',
        signature: '',
        key: '',
        ossaccessKeyId: '',
        dir: '',
        host: ''
      },
      imageName: "",

      keyword: '',
      dialogVisable: false,
      fileList: [],
      //tenant:tenant 为了做数据表单校验不要嵌套对象
      meals: [],
      tenant: {
        admin: {
          username: "",
          tel: "",
          email: "",
          password: "",
          confirmPassword: ""
        },
        companyName: '',
        companyNum: '',
        address: '',
        logo: '',
        mealId: null
      },

      formRules: {
        mealId: [
          {required: true, message: '请输入套餐!', trigger: 'blur'}
        ],
        // companyName: [
        //   { required: true, message: '请输入租户名称!', trigger: 'blur' }
        // ],
        // companyNum: [
        //   {required: true, message: '请输入租户座机!', trigger: 'blur'}
        // ],
        address: [
          {required: true, message: '请输入租户地址!', trigger: 'blur'}
        ],
        'admin.tel': [
          {required: true, message: '请输入手机号!', trigger: 'blur'}
        ],
        // logo: [
        //     { required: true, message: '请输入租户logo!', trigger: 'blur' }
        // ],
        'admin.email': [
          {type: 'email', required: true, message: '请输入邮箱!', trigger: 'blur'}
        ],
        'admin.username': [
          {required: true, message: '请输入账号!', trigger: 'blur'}
        ],
        'admin.password': [
          {required: true, message: '请输入密码!', trigger: 'blur'}
        ],
        'admin.confirmPassword': [
          {required: true, validator: validatePass2, trigger: 'blur'} //自定义校验规则
        ]
      }
    };
  },
  methods: {

    //获取UUID
    getUUID() {
      var s = [];
      var hexDigits = "0123456789abcdef";
      for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
      }
      s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
      s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
      s[8] = s[13] = s[18] = s[23] = "-";
      var uuid = s.join("");
      return uuid;

    },

    //上传钱调用的方法
    async beforeUpload(){
      await this.$http.post("/resources/alioss/sign").then(response=>{
        //设置相关的参数
        var resultObj = response.data.resultObj;
        this.uploadData.policy = resultObj.policy;
        this.uploadData.signature = resultObj.signature;
        this.uploadData.ossaccessKeyId = resultObj.accessid;
        //上传的文件名，使用UUID处理一下
        this.imageName = this.getUUID()+'_${filename}'
        // this.uploadData.key = resultObj.dir + '/' + this.getUUID()+'_${filename}';
        this.uploadData.key = resultObj.dir + '/' + this.imageName;
        this.uploadData.dir = resultObj.dir;
        this.uploadData.host = resultObj.host;
      });
    },

    //文件上传成功回调
    handleSuccess(response, file, fileList){
      //上传的完整的文件地址
      var urlPath = this.uploadData.host + '/' + this.uploadData.key.replace("${filename}",file.name);
      this.tenant.logo = urlPath;
      this.$message({message: '上传成功，图片地址：' + this.tenant.logo, type: 'success' });
    },

    //文件删除
    handleRemove(file, fileList) {
      // var filePath = file.response.resultObj;
      var imageName = this.imageName.replace("${filename}",file.name);
      console.log(imageName);
      this.$http.delete("/resources/alioss/delete/" + imageName)
          .then(res => {
            if (res.data.success) {
              this.$message({
                message: '删除文件成功!',
                type: 'success'
              });
              this.tenant.logo = "";    //置空，避免删除文件后logo里面仍有值会导致数据库还是可以存到url地址
            } else {
              this.$message({
                message: '删除文件失败!',
                type: 'error'
              });
            }
          })
    },

    //图片预览
    handlePreview(file) {
      console.log(file);
    },

    //获取套餐
    getMeals() {
      this.$http.get("/sysmanage/meal")
          .then(result => {
            result = result.data;
            if (result.success) {
              this.meals = result.resultObj;
            } else {
              this.$message({
                message: result.message,
                type: 'error'
              });
            }
          }).catch(result => {
        this.$message({
          message: '系统错误!',
          type: 'error'
        });
      })
    },

    selectAdrressConfirm() {
      //获取值搜索框值,设置给地址
      var searchInputV = document.getElementById("searchInput").value;
      this.tenant.address = searchInputV;
      //关闭对话框
      this.dialogVisable = false;
    },

    selectAdrress() {
      this.dialogVisable = true;
    },

    //提交入驻
    settledIn() {
      this.$refs.tenantForm.validate((valid) => {
        //校验表单成功后才做一下操作
        if (valid) {
          this.$confirm('确认入驻吗？', '提示', {}).then(() => {
            //拷贝后面对象的值到新对象,防止后面代码改动引起模型变化
            let para = Object.assign({}, this.tenant); //tenant 本身这个参数里面就有租户和管理员信息
            // 为了后台好接收，封装一个对象放到里面
            //判断是否有id有就是修改,否则就是添加
            this.$http.post("/sysmanage/tenant/settlement", para).then((res) => {
              if (res.data.success) {
                this.$message({
                  message: '操作成功!',
                  type: 'success'
                });
                //重置表单
                this.$refs['tenantForm'].resetFields();
                //跳转登录页面
                this.$router.push({path: '/login'});
              } else {
                this.$message({
                  message: res.data.message,
                  type: 'error'
                });
              }

            });
          });
        }
      })
    }
  },
  mounted() {
    this.getMeals();
  }
}

</script>
<style lang="scss" scoped>
.login-container {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  -moz-border-radius: 5px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 500px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;

  .title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }

  .remember {
    margin: 0px 0px 35px 0px;
  }
}

.map {
  width: 100%;
  height: 300px;
}

.searchinput {
  width: 300px;
  box-sizing: border-box;
  padding: 9px;
  border: 1px solid #dddee1;
  line-height: 20px;
  font-size: 16px;
  height: 38px;
  color: #333;
  position: relative;
  border-radius: 4px;
}
</style>