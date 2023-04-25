package com.heng.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.heng.clients.AuthorizationClient;
import com.heng.domain.Employee;
import com.heng.domain.LoginUser;
import com.heng.domain.Tenant;
import com.heng.exception.Assertion;
import com.heng.exception.BusinessException;
import com.heng.exception.ResponseCode;
import com.heng.mapper.EmployeeMapper;
import com.heng.mapper.TenantMapper;
import com.heng.query.TenantQuery;
import com.heng.service.ITenantService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.heng.util.AjaxResult;
import com.heng.util.PageList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-04-24
 */
@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private AuthorizationClient authorizationClient;

    /**
     * 高级查询
     * @param query
     * @return
     */
    @Override
    public PageList selectPageList(TenantQuery query) {
        Page<Tenant> page = new Page<Tenant>(query.getPage(), query.getRows());
        List<Tenant> result = tenantMapper.selectPageList(page,query);
        PageList<Tenant> pageList = new PageList<>(page.getTotal(), result);
        return pageList;
    }

    /**
     * 入驻
     * @param tenant
     */
    @Override
    @Transactional
    public AjaxResult Occupancy(Tenant tenant) {
        //1、参数校验
        parameterCheck(tenant);
        //2、百度审核
        boolean auditResult = true;
        String message = "审核通过!";
        //3、调用授权中心存放login_user信息，并返回login
        //存放信息
        LoginUser loginUser = new LoginUser();
        Employee admin = tenant.getAdmin();
        BeanUtils.copyProperties(admin,loginUser);
        loginUser.setId(null);
        loginUser.setType(0);
        //调用授权中心的新增方法
        AjaxResult ajaxResult = authorizationClient.addOrUpdate(loginUser);
        if (!ajaxResult.isSuccess()){
            return ajaxResult;
        }
        Long loginId = Long.valueOf(ajaxResult.getResultObj().toString());

        //4、保存信息到租户管理员表
        admin.setRealName(admin.getUsername());
        admin.setInputTime(new Date());
        admin.setState(0);
        admin.setType(1);
        admin.setLoginId(loginId);
        employeeMapper.insert(admin);
        //5、保存租户信息
        tenant.setAdminId(admin.getId());
        tenant.setAdmin(admin);
        tenant.setRegisterTime(new Date());
        tenant.setState(auditResult? 1 : 2);
        tenantMapper.insert(tenant);
        //6、保存租户套餐中间表
        Integer state = 0;
        tenantMapper.saveTenantMeal(tenant.getMealId(),tenant.getId(),state);
        //7、添加审核日志表
        //8、发邮件激活
        return AjaxResult.me();
    }


    /**
     * 参数校验
     * @param tenant
     */
    private void parameterCheck(Tenant tenant) {
        //参数的非空校验
        //两次密码校验
        Employee admin = tenant.getAdmin();
        //断言
        Assertion.isEquals(admin.getPassword(),admin.getConfirmPassword(), ResponseCode.RESPONSE_CODE_400_TENANT_ADMIN_PASSWORD_NOT_EQUALS);
        //店铺名查重
        List<Tenant> company_names = tenantMapper.selectList(new EntityWrapper<Tenant>().eq("company_name", tenant.getCompanyName()));
        //断言
        Assertion.isNull(company_names,ResponseCode.RESPONSE_CODE_400_TENANT_EXIST);
        //店铺管理员查重
        List<Employee> usernames = employeeMapper.selectList(new EntityWrapper<Employee>().eq("username", admin.getUsername()));
        Assertion.isNull(usernames,ResponseCode.RESPONSE_CODE_400_TENANT_ADMIN_EXIST);

    }

}
