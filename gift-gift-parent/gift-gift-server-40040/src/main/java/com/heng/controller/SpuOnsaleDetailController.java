package com.heng.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.heng.domain.SpuOnsaleDetail;
import com.heng.query.SpuOnsaleDetailQuery;
import com.heng.service.ISpuOnsaleDetailService;
import com.heng.util.AjaxResult;
import com.heng.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spuOnsaleDetail")
public class SpuOnsaleDetailController {
    @Autowired
    public ISpuOnsaleDetailService spuOnsaleDetailService;


    /**
     * 保存和修改公用的
     * @param spuOnsaleDetail 传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody SpuOnsaleDetail spuOnsaleDetail) {
        try {
            if ( spuOnsaleDetail.getId() != null)
                spuOnsaleDetailService.updateById(spuOnsaleDetail);
            else
                spuOnsaleDetailService.insert(spuOnsaleDetail);
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
            spuOnsaleDetailService.deleteById(id);
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
            SpuOnsaleDetail spuOnsaleDetail =spuOnsaleDetailService.selectById(id);
            return AjaxResult.me().setResultObj(spuOnsaleDetail);
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
            List< SpuOnsaleDetail> list = spuOnsaleDetailService.selectList(null);
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
    public AjaxResult json(@RequestBody SpuOnsaleDetailQuery query) {
        Page<SpuOnsaleDetail> page = new Page<SpuOnsaleDetail>(query.getPage(), query.getRows());
        page = spuOnsaleDetailService.selectPage(page);
        PageList pageList = new PageList<SpuOnsaleDetail>(page.getTotal(), page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
