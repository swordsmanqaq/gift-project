package com.heng.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.heng.service.ISpuDetailService;
import com.heng.domain.SpuDetail;
import com.heng.query.SpuDetailQuery;
import com.heng.util.AjaxResult;
import com.heng.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spuDetail")
public class SpuDetailController {
    @Autowired
    public ISpuDetailService spuDetailService;


    /**
     * 保存和修改公用的
     * @param spuDetail 传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody SpuDetail spuDetail) {
        try {
            if ( spuDetail.getId() != null)
                spuDetailService.updateById(spuDetail);
            else
                spuDetailService.insert(spuDetail);
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
            spuDetailService.deleteById(id);
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
            SpuDetail spuDetail =spuDetailService.selectById(id);
            return AjaxResult.me().setResultObj(spuDetail);
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
            List< SpuDetail> list = spuDetailService.selectList(null);
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
    public AjaxResult json(@RequestBody SpuDetailQuery query) {
        Page<SpuDetail> page = new Page<SpuDetail>(query.getPage(), query.getRows());
        page = spuDetailService.selectPage(page);
        PageList pageList = new PageList<SpuDetail>(page.getTotal(), page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }
}
