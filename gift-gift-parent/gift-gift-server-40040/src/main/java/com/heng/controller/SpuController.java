package com.heng.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.heng.DTO.SpuOnsaleDTO;
import com.heng.service.ISpuService;
import com.heng.domain.Spu;
import com.heng.query.SpuQuery;
import com.heng.util.AjaxResult;
import com.heng.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spu")
public class SpuController {
    @Autowired
    public ISpuService spuService;


    /**
     * 保存和修改公用的
     * @param spu 传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    public AjaxResult addOrUpdate(@RequestBody Spu spu) {
        try {
            if ( spu.getId() != null)
                spuService.updateById(spu);
            else
                spuService.insert(spu);
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
            spuService.deleteById(id);
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
            Spu spu =spuService.selectById(id);
            return AjaxResult.me().setResultObj(spu);
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
            List< Spu> list = spuService.selectList(null);
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
    public AjaxResult json(@RequestBody SpuQuery query) {
        Page<Spu> page = new Page<Spu>(query.getPage(), query.getRows());
        page = spuService.selectPage(page);
        PageList pageList = new PageList<Spu>(page.getTotal(), page.getRecords());
        return AjaxResult.me().setResultObj(pageList);
    }

    /**
     * 上架
     * @param ids
     * @return
     */
    @PostMapping("/onsale")
    public AjaxResult onsale(@RequestBody List<Long> ids) {
        return spuService.onsale(ids);
    }

    /**
     * 下架
     * @param ids
     * @return
     */
    @PostMapping("/offsale")
    public AjaxResult offsale(@RequestBody List<Long> ids) {
        return spuService.offsale(ids);
    }
}
