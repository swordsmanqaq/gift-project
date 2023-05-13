package com.heng.controller;


import com.heng.dto.ShopCarDTO;
import com.heng.dto.TenantIdName;
import com.heng.service.IShopCarService;
import com.heng.domain.ShopCar;
import com.heng.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopCar")
public class ShopCarController {

    @Autowired
    public IShopCarService shopCarService;


    /**
     * 添加购物车
     * @return
     */
    @PutMapping
    public AjaxResult insertShopCar(@Valid @RequestBody ShopCarDTO shopCarDTO){
        try {
            Long userId = 1L;
            String username = "jarvis";
            shopCarDTO.setUserId(userId);
            shopCarDTO.setUsername(username);
            return shopCarService.insertShopCar(shopCarDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("添加购物车失败！" + e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param skuId
    * @return
    */
    @DeleteMapping(value = "/{skuId}")
    public AjaxResult delete(@PathVariable("skuId") Long skuId) {
        try {
            Long userId = 1L;
            String username = "jarvis";
            ShopCarDTO shopCarDTO = new ShopCarDTO();
            shopCarDTO.setSkuId(skuId);
            shopCarDTO.setUserId(userId);
            shopCarDTO.setUsername(username);
            return shopCarService.deleteShopCar(shopCarDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！" + e.getMessage());
        }
    }

    /**
     * 修改购物车数量
     * @param shopCarDTO
     * @return
     */
    @PostMapping("/modify/number")
    public AjaxResult modifyNumber(@RequestBody ShopCarDTO shopCarDTO){
        try {
            Long userId = 1L;
            String username = "jarvis";
            shopCarDTO.setUserId(userId);
            shopCarDTO.setUsername(username);
            shopCarService.modifyNumber(shopCarDTO);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("修改失败！" + e.getMessage());
        }
    }

    /**
     * 购物车商品的勾选状态
     * @param shopCarDTO
     * @return
     */
    @PostMapping("/isSelect")
    public AjaxResult isSelect(@RequestBody ShopCarDTO shopCarDTO){
        try {
            Long userId = 1L;
            String username = "jarvis";
            shopCarDTO.setUserId(userId);
            shopCarDTO.setUsername(username);
            shopCarService.isSelect(shopCarDTO);
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("选中失败！" + e.getMessage());
        }
    }

    /**
    * 查看所有的员工信息
    * @return
    */
    @GetMapping
    public AjaxResult list() {
        Long userId = 1L;
        String username = "jarvis";
        try {
            Map<TenantIdName,List<ShopCar>> map = shopCarService.getShopCarList(userId);
            return AjaxResult.me().setResultObj(map);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("获取所有失败！" + e.getMessage());
        }
    }

}
