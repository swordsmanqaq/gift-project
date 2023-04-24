package com.heng.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.heng.service.IConfigService;
import com.heng.domain.Config;
import com.heng.query.ConfigQuery;
import com.heng.util.AjaxResult;
import com.heng.util.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config")
@Api(value = "系统配置中心", tags = "配置中心")
public class ConfigController {
    @Autowired
    public IConfigService configService;


    /**
     * 保存和修改公用的
     * @param config 传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    @ApiOperation("新增和修改方法")
    public AjaxResult addOrUpdate(@RequestBody Config config) {
        try {
            if ( config.getConfigId() != null)
                configService.updateById(config);
            else
                configService.insert(config);
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
    @ApiOperation("根据id删除")
    public AjaxResult delete(@PathVariable("id") Long id) {
        try {
            configService.deleteById(id);
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
    @ApiOperation("根据id获取")
    public AjaxResult get(@PathVariable("id") Long id) {
        try {
            Config config =configService.selectById(id);
            return AjaxResult.me().setResultObj(config);
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
    @ApiOperation("获取所有")
    public AjaxResult list() {

        try {
            List< Config> list = configService.selectList(null);
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
    public AjaxResult json(@RequestBody ConfigQuery query) {
        Page<Config> page = new Page<Config>(query.getPage(), query.getRows());
        page = configService.selectPage(page);
        PageList pageList = new PageList<Config>(page.getTotal(), page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
