// import Login from './views/Login.vue'
import ShopRegister from "./views/ShopRegister";
import Login from "./views/Login";
import NotFound from './views/404.vue'
import NoPermission from './views/403.vue'
import Home from './views/Home.vue'
import Form from './views/nav1/Form.vue'
import user from './views/nav1/user.vue'
import echarts from './views/charts/echarts.vue'
//==================导入组件================
import Department from "./views/org/Department.vue";
import Employee from "./views/org/Employee";
import Permission from "./views/auth/Permission";
import Role from "./views/auth/Role";
import Menus from "./views/auth/Menus";
import Config from "./views/sys/Config";
import TenantType from './views/sys/TenantType'
import Tenant from './views/sys/Tenant'


let routes = [
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/shopRegister',
        component: ShopRegister,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    {
        path: '/',
        component: Home,
        name: '',
        hidden: true,
        children: [
            { path: '/403', component: NoPermission, name: '' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: 'Charts',
        iconCls: 'fa fa-bar-chart',
        leaf: true, // leaf表示的是叶子 如果是true的话,就表示当前是一个叶子节点
        children: [
            { path: '/echarts', component: echarts, name: '首页' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '用户中心管理',
        iconCls: 'el-icon-message',//图标样式class
        children: [
            { path: '/department', component: Department, name: '部门管理' },
            { path: '/employee', component: Employee, name: '员工管理' },
            { path: '/form', component: Form, name: 'Form' },
            { path: '/user', component: user, name: '列表' },
        ]
    },
    {
        path: '/',
        component: Home,
        name: '授权中心管理',
        iconCls: 'el-icon-message',//图标样式class
        children: [
            { path: '/permission', component: Permission, name: '权限列表'},
            { path: '/role', component: Role, name: '角色管理'},
            { path: '/menu', component: Menus, name: '菜单管理'},
        ]
    },
    {
        path: '/',
        component: Home,
        name: '系统中心',
        iconCls: 'el-icon-message',//图标样式class
        children: [
            { path: '/config', component: Config, name: '系统配置'},
            { path: '/department', component: Department, name: '部门管理'},
            { path: '/employee', component: Employee, name: '员工管理'},
            { path: '/tenantType', component: TenantType, name: '租户类型'},
            { path: '/tenant', component: Tenant, name: '租户管理'},
            { path: '/meal', component: Tenant, name: '套餐管理'}
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;