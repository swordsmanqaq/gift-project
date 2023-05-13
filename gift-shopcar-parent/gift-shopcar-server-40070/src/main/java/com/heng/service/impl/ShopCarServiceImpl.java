package com.heng.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.heng.clients.GiftClient;
import com.heng.constants.Constant;
import com.heng.domain.ShopCar;
import com.heng.domain.Sku;
import com.heng.dto.ShopCarDTO;
import com.heng.dto.TenantIdName;
import com.heng.exception.Assertion;
import com.heng.exception.ResponseCode;
import com.heng.mapper.ShopCarMapper;
import com.heng.service.IShopCarService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.heng.util.AjaxResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-13
 */
@Service
public class ShopCarServiceImpl extends ServiceImpl<ShopCarMapper, ShopCar> implements IShopCarService {

    @Autowired
    private GiftClient giftClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private ShopCarMapper shopCarMapper;

    /**
     * 添加购物车
     * @param shopCarDTO
     * @return
     */
    @Override
    public AjaxResult insertShopCar(ShopCarDTO shopCarDTO) {
        //根据skuId去礼物中心服务中查找相对应的sku
        AjaxResult ajaxResult = giftClient.get(shopCarDTO.getSkuId());
        //非空校验
        if (!ajaxResult.isSuccess()){
            return ajaxResult;
        }
        Sku sku = JSONObject.parseObject((String) ajaxResult.getResultObj(), Sku.class);
        //将sku的信息赋值到购物车中，并存入redis
        ShopCar shopCar = createShopCar(shopCarDTO, sku);
        //存入redis缓存中
        BoundHashOperations boundHashOps = redisTemplate.boundHashOps(Constant.USER_SHOP_CAR_PREFIX + shopCarDTO.getUserId());
        //根据小key看购物车中是否已经有该sku商品了，如果就直接更改数量，没有则加入
        String key = shopCarDTO.getSkuId().toString();
        Boolean hasKey = boundHashOps.hasKey(key);
        if (!hasKey){
            //购物车中没有该sku，添加sku到购物车中
            boundHashOps.put(key,JSONObject.toJSONString(shopCar));
        }else {
            //购物车中已经存在，则更改sku数量
            String result = (String) boundHashOps.get(key);
            ShopCar shopCarResult = JSONObject.parseObject(result, ShopCar.class);
            shopCarResult.setNum(shopCar.getNum() + shopCarDTO.getNumber());
            boundHashOps.put(key,JSONObject.toJSONString(shopCarResult));
        }
        return AjaxResult.me();
    }


    /**
     * 通过发送事务消息 删除购物车
     * @param shopCarDTO
     * @return
     */
    @Override
    public AjaxResult deleteShopCar(ShopCarDTO shopCarDTO) {
        //发送事务消息到rocketMQ中
        Message<String> message = MessageBuilder.withPayload(JSONObject.toJSONString(shopCarDTO)).build();
        rocketMQTemplate.sendMessageInTransaction(Constant.SHOP_CAR_TX_PRODUCER_GROUP,
                Constant.SHOP_CAR_TX_TOPIC + ":" + Constant.SHOP_CAR_TX_TOPIC_TAG,
                message,null);
        return AjaxResult.me();
    }

    /**
     * 消费消息执行删除操作
     * @param shopCarDTO
     */
    @Override
    public void deleteShopCarFromRedisAndDatabase(ShopCarDTO shopCarDTO) {
        //删除数据库中的数据
        shopCarMapper.delete(new EntityWrapper<ShopCar>()
                .eq("user_id",shopCarDTO.getUserId())
                .and().eq("sku_id",shopCarDTO.getSkuId()));
        //删除redis中的数据
        BoundHashOperations boundHashOps = redisTemplate.boundHashOps(Constant.USER_SHOP_CAR_PREFIX + shopCarDTO.getUserId());
        String key = shopCarDTO.getSkuId().toString();
        boundHashOps.delete(key);

    }


    /**
     * 修改数量
     * @param shopCarDTO
     */
    @Override
    public void modifyNumber(ShopCarDTO shopCarDTO) {
        //参数校验
        Assertion.isFalse(shopCarDTO.getNumber().intValue() < 1, ResponseCode.RESPONSE_CODE_400);
        //操作redis
        BoundHashOperations boundHashOps = redisTemplate.boundHashOps(Constant.USER_SHOP_CAR_PREFIX + shopCarDTO.getUserId());
        String key = shopCarDTO.getSkuId().toString();
        Boolean hasKey = boundHashOps.hasKey(key);
        if (hasKey){
            String result = (String) boundHashOps.get(key);
            ShopCar shopCarResult = JSONObject.parseObject(result, ShopCar.class);
            //修改数量
            shopCarResult.setNum(shopCarDTO.getNumber());
            boundHashOps.put(key,JSONObject.toJSONString(shopCarResult));
        }else {
            throw new RuntimeException("系统繁忙，请稍后再试");
        }

    }


    /**
     * 选中状态
     * @param shopCarDTO
     */
    @Override
    public void isSelect(ShopCarDTO shopCarDTO) {
        //参数校验
        Integer isSelected = shopCarDTO.getIsSelected();
        Assertion.isTrue(isSelected == 0 || isSelected == 1, ResponseCode.RESPONSE_CODE_400);
        //操作redis
        BoundHashOperations boundHashOps = redisTemplate.boundHashOps(Constant.USER_SHOP_CAR_PREFIX + shopCarDTO.getUserId());
        String key = shopCarDTO.getSkuId().toString();
        Boolean hasKey = boundHashOps.hasKey(key);
        if (hasKey){
            String result = (String) boundHashOps.get(key);
            ShopCar shopCarResult = JSONObject.parseObject(result, ShopCar.class);
            //修改状态
            boolean choose = isSelected == 0 ? false : true;
            shopCarResult.setSelect(choose);
            boundHashOps.put(key,JSONObject.toJSONString(shopCarResult));
        }

    }

    /**
     * 根据userId查询该用户所有的购物车商品
     * @param userId
     * @return
     */
    @Override
    public Map<TenantIdName, List<ShopCar>> getShopCarList(Long userId) {
        //先从redis中查询数据，如果有，直接返回
        BoundHashOperations boundHashOps = redisTemplate.boundHashOps(Constant.USER_SHOP_CAR_PREFIX + userId);
        //获取userId下所有的key
        Set keys = boundHashOps.keys();
        List<ShopCar> shopCars = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(keys)){
            //如果keys不为空，则进行循环操作
            List<ShopCar> finalShopCars = shopCars;
            keys.forEach(key -> {
                String result = (String) boundHashOps.get(key);
                ShopCar shopCar = JSONObject.parseObject(result, ShopCar.class);
                //被标记逻辑删除的数据不需要放入集合中
                if(!shopCar.getDelete()){
                    finalShopCars.add(shopCar);
                }
            });
        }else {
            //如果为空，则redis中没有数据，去数据库中查询
            shopCars = shopCarMapper.selectList(new EntityWrapper<ShopCar>().eq("user_id", userId));
            Map<String,String> map = new HashMap<>();
            shopCars.forEach(shopCar -> {
                map.put(shopCar.getSkuId().toString(),JSONObject.toJSONString(shopCar));
            });
            //将数据放入redis中
            boundHashOps.putAll(map);
        }

        //根据店铺进行购物车商品分组，并将数据返回
        Map<TenantIdName,List<ShopCar>> resultMap = new HashMap<>();
        //判断该用户是否有购物车数据
        if (CollectionUtil.isEmpty(shopCars)){
            return resultMap;
        }
        shopCars.forEach(shopCar -> {
            Long tenantId = shopCar.getTenantId();
            String tenantName = shopCar.getTenantName();
            TenantIdName tenantIdName = new TenantIdName(tenantId, tenantName);
            //根据map 的key获取sku
            List<ShopCar> shopCarList = resultMap.get(tenantIdName);
            if (CollectionUtil.isNotEmpty(shopCarList)){
                //如果不为空，说明已经有该店铺的商品了，直接加在该店铺下
                shopCarList.add(shopCar);
            }else {
                //如果为空，说明还没有该店铺，则创建后在放入
                List<ShopCar> cars = new ArrayList<>();
                cars.add(shopCar);
                resultMap.put(tenantIdName,cars);
            }
        });

        return resultMap;
    }


    /**
     * 创建ShopCar对象
     * @param shopCarDTO
     * @param sku
     * @return
     */
    private ShopCar createShopCar(ShopCarDTO shopCarDTO, Sku sku) {
        ShopCar shopCar = new ShopCar();
        shopCar.setSpuId(sku.getSpuId());
        shopCar.setSpuName(sku.getSpuName());
        shopCar.setSkuId(sku.getId());
        shopCar.setSkuInfo(sku.getSkuName());
        shopCar.setAddPrice(sku.getPrice());
        shopCar.setPrice(sku.getPrice());
        shopCar.setUserId(shopCarDTO.getUserId());
        shopCar.setUsername(shopCarDTO.getUsername());
        shopCar.setTenantId(sku.getTenantId());
        shopCar.setTenantName(sku.getTenantName());
        shopCar.setSelect(false);
        shopCar.setDelete(false);
        shopCar.setCreateTime(new Date());
        shopCar.setNum(shopCarDTO.getNumber());
        return shopCar;
    }
}
