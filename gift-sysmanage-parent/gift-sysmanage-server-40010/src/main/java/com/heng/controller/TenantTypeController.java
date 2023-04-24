package com.heng.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.heng.service.ITenantTypeService;
import com.heng.domain.TenantType;
import com.heng.query.TenantTypeQuery;
import com.heng.util.AjaxResult;
import com.heng.util.PageList;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenantType")
public class TenantTypeController {
    @Autowired
    public ITenantTypeService tenantTypeService;


    /**
     * 保存和修改公用的
     *
     * @param tenantType 传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody TenantType tenantType) {
        try {
            if (tenantType.getId() != null)
                tenantTypeService.updateById(tenantType);
            else
                tenantTypeService.insert(tenantType);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！" + e.getMessage());
        }
    }

    /**
     * 删除对象信息
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        try {
            tenantTypeService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！" + e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        try {
            TenantType tenantType = tenantTypeService.selectById(id);
            return AjaxResult.me().setResultObj(tenantType);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("获取一个失败！" + e.getMessage());
        }
    }


    /**
     * 查看所有的员工信息
     *
     * @return
     */
    @GetMapping
    public AjaxResult list() {

        try {
            List<TenantType> list = tenantTypeService.selectList(null);
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
    public AjaxResult json(@RequestBody TenantTypeQuery query) {

        Page<TenantType> page = new Page<TenantType>(query.getPage(), query.getRows());
        EntityWrapper<TenantType> wrapper = new EntityWrapper<TenantType>();
        //获取关键词
        String keyword = query.getKeyword();
        //判断是否为空，不为空在进行操作
        if (StringUtils.isNotEmpty(keyword)) {
            wrapper.like("name", keyword).or().like("description", keyword);
        }
        page = tenantTypeService.selectPage(page, wrapper);
        //封装page
        PageList pageList = new PageList<TenantType>(page.getTotal(), page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
