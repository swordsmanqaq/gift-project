package com.heng.service;

import com.heng.domain.ShopCar;
import com.baomidou.mybatisplus.service.IService;
import com.heng.dto.ShopCarDTO;
import com.heng.dto.TenantIdName;
import com.heng.util.AjaxResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-13
 */
public interface IShopCarService extends IService<ShopCar> {

    AjaxResult insertShopCar(ShopCarDTO shopCarDTO);

    AjaxResult deleteShopCar(ShopCarDTO shopCarDTO);

    void modifyNumber(ShopCarDTO shopCarDTO);

    void isSelect(ShopCarDTO shopCarDTO);

    void deleteShopCarFromRedisAndDatabase(ShopCarDTO shopCarDTO);

    Map<TenantIdName, List<ShopCar>> getShopCarList(Long userId);

}
