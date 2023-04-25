package com.heng.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.heng.service.ILoginUserService;
import com.heng.domain.LoginUser;
import com.heng.query.LoginUserQuery;
import com.heng.util.AjaxResult;
import com.heng.util.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loginUser")
@Api(value = "登录管理", tags = "登录管理")
public class LoginUserController {
    @Autowired
    public ILoginUserService loginUserService;


    /**
     * 保存和修改公用的
     * @param loginUser 传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    @ApiOperation("新增或修改")
    public AjaxResult addOrUpdate(@RequestBody LoginUser loginUser) {
        try {
            if ( loginUser.getId() != null)
                loginUserService.updateById(loginUser);
            else
                loginUserService.insert(loginUser);
            return AjaxResult.me().setResultObj(loginUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！" + e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value = "/{id}")
    @ApiOperation("根据id删除")
    public AjaxResult delete(@PathVariable("id") Long id) {
        try {
            loginUserService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！" + e.getMessage());
        }
    }

    /**
    * 根据id获取信息
    * @param id
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询")
    public AjaxResult get(@PathVariable("id") Long id) {
        try {
            LoginUser loginUser =loginUserService.selectById(id);
            return AjaxResult.me().setResultObj(loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("获取一个失败！" + e.getMessage());
        }
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @GetMapping
    @ApiOperation("查询所有信息")
    public AjaxResult list() {

        try {
            List< LoginUser> list = loginUserService.selectList(null);
            return AjaxResult.me().setResultObj(list);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("获取所有失败！" + e.getMessage());
        }
    }

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping("/list")
    @ApiOperation("分页查询")
    public AjaxResult json(@RequestBody LoginUserQuery query) {
        Page<LoginUser> page = new Page<LoginUser>(query.getPage(), query.getRows());
        page = loginUserService.selectPage(page);
        PageList pageList = new PageList<LoginUser>(page.getTotal(), page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
