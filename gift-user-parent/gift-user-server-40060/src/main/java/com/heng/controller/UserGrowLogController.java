package com.heng.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.heng.service.IUserGrowLogService;
import com.heng.domain.UserGrowLog;
import com.heng.query.UserGrowLogQuery;
import com.heng.util.AjaxResult;
import com.heng.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userGrowLog")
public class UserGrowLogController {
    @Autowired
    public IUserGrowLogService userGrowLogService;


    /**
     * 保存和修改公用的
     * @param userGrowLog 传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody UserGrowLog userGrowLog) {
        try {
            if ( userGrowLog.getId() != null)
                userGrowLogService.updateById(userGrowLog);
            else
                userGrowLogService.insert(userGrowLog);
            return AjaxResult.me();
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
    public AjaxResult delete(@PathVariable("id") Long id) {
        try {
            userGrowLogService.deleteById(id);
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
    public AjaxResult get(@PathVariable("id") Long id) {
        try {
            UserGrowLog userGrowLog =userGrowLogService.selectById(id);
            return AjaxResult.me().setResultObj(userGrowLog);
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
    public AjaxResult list() {

        try {
            List< UserGrowLog> list = userGrowLogService.selectList(null);
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
    public AjaxResult json(@RequestBody UserGrowLogQuery query) {
        Page<UserGrowLog> page = new Page<UserGrowLog>(query.getPage(), query.getRows());
        page = userGrowLogService.selectPage(page);
        PageList pageList = new PageList<UserGrowLog>(page.getTotal(), page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
