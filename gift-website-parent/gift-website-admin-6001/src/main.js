import babelpolyfill from 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
//import './assets/theme/theme-green/index.css'
import VueRouter from 'vue-router'
import store from './vuex/store'
import Vuex from 'vuex'
//import NProgress from 'nprogress'
//import 'nprogress/nprogress.css'
import routes from './routes.js'
//import Mock from './mock'
//Mock.bootstrap();

// 导入axios
import axios from "axios"
// 给axios设置一个默认的前缀
axios.defaults.baseURL = "http://localhost:30010/service/";  //访问统一入口网关
Vue.prototype.$http = axios;

import 'font-awesome/css/font-awesome.min.css'

Vue.use(ElementUI)
Vue.use(VueRouter)
Vue.use(Vuex)

//NProgress.configure({ showSpinner: false });

const router = new VueRouter({
  routes
})

/*
router.beforeEach((to, from, next) => {
  // 如果当前要访问前端 /login这个路径的话,下次如果想访问首页,是需要重新登录的
  if (to.path == '/login') {
    localStorage.removeItem('loginUser');
    localStorage.removeItem('token');
  }6
  debugger;
  let user = JSON.parse(localStorage.getItem('loginUser'));
  if (!user && to.path != '/login') {
    // 如果localStorage中没有存放loginUser,并且要访问的页面不是登录页面的话,就说明未登录,就要跳转回登录页面
    next({ path: '/login' })
  } else {
    next()
  }
})*/

//router.afterEach(transition => {
//NProgress.done();
//});

// axios请求拦截器:添加请求拦截器，在请求头中加token
// 前端调用后端接口,都是通过axios发起请求的,所以在axios调用后端接口之前,都会先经过axios的前置拦截器
/*
axios.interceptors.request.use(
    config => {
      // 从localStorage中获取token
      let token = localStorage.getItem("token");

      // 如果token有值,放到请求头里面
      if (token) {
        config.headers.token = token;
      }
      return config
    },
    error => {
      return Promise.reject(error)
    })*/
// 响应拦截器:后端响应结果给前端时,都要先经过响应拦截器
/*
axios.interceptors.response.use(function(response){
  //对返回的数据进行操作
  let result = response.data;
  if(!result.success && result.message == "noLogin"){ // 未登录被拦截,跳回到登陆页面
    router.push({ path: '/login' });  // 跳转回登陆页面,让用户登陆
    alert("请登录后再次访问!")
  }else if(!result.success && result.message == "noPermission"){
      router.push({ path: '/403' });
  }else{
    // 如果登录过的话,就继续往下运行
    return response;
  }
},function(err){
  return Promise.reject(err)
})*/


new Vue({
  //el: '#app',
  //template: '<App/>',
  router,
  store,
  //components: { App }
  render: h => h(App)
}).$mount('#app')

