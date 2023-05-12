package com.heng.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.heng.service.ISkuSpecValueService;
import com.heng.domain.SkuSpecValue;
import com.heng.query.SkuSpecValueQuery;
import com.heng.util.AjaxResult;
import com.heng.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skuSpecValue")
public class SkuSpecValueController {
    @Autowired
    public ISkuSpecValueService skuSpecValueService;


    /**
     * 保存和修改公用的
     * @param skuSpecValue 传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody SkuSpecValue skuSpecValue) {
        try {
            if ( skuSpecValue.getId() != null)
                skuSpecValueService.updateById(skuSpecValue);
            else
                skuSpecValueService.insert(skuSpecValue);
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
            skuSpecValueService.deleteById(id);
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
            SkuSpecValue skuSpecValue =skuSpecValueService.selectById(id);
            return AjaxResult.me().setResultObj(skuSpecValue);
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
            List< SkuSpecValue> list = skuSpecValueService.selectList(null);
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
    public AjaxResult json(@RequestBody SkuSpecValueQuery query) {
        Page<SkuSpecValue> page = new Page<SkuSpecValue>(query.getPage(), query.getRows());
        page = skuSpecValueService.selectPage(page);
        PageList pageList = new PageList<SkuSpecValue>(page.getTotal(), page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
