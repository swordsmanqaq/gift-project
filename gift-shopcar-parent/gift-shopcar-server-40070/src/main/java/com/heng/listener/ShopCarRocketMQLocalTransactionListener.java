package com.heng.listener;

import com.alibaba.fastjson.JSONObject;
import com.heng.constants.Constant;
import com.heng.domain.ShopCar;
import com.heng.dto.ShopCarDTO;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component
@RocketMQTransactionListener(txProducerGroup = Constant.SHOP_CAR_TX_PRODUCER_GROUP)
public class ShopCarRocketMQLocalTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    private RedisTemplate redisTemplate;

    //执行本地事务
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            byte[] bytes = (byte[]) message.getPayload();
            String payload = new String(bytes, "utf-8");
            ShopCarDTO shopCarDTO = JSONObject.parseObject(payload, ShopCarDTO.class);
            //修改redis中shopcar数据的状态，意为标记逻辑删除
            BoundHashOperations boundHashOps = redisTemplate.boundHashOps(Constant.USER_SHOP_CAR_PREFIX + shopCarDTO.getUserId());
            String key = shopCarDTO.getSkuId().toString();
            Boolean hasKey = boundHashOps.hasKey(key);
            if (hasKey){
                String result = (String) boundHashOps.get(key);
                ShopCar shopCarResult = JSONObject.parseObject(result, ShopCar.class);
                shopCarResult.setDelete(true);
                boundHashOps.put(key,JSONObject.toJSONString(shopCarResult));
                return  RocketMQLocalTransactionState.COMMIT;
            } else{
                return  RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    //回查本地事务
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        try {
            byte[] bytes = (byte[]) message.getPayload();
            String payload = new String(bytes, "utf-8");
            ShopCarDTO shopCarDTO = JSONObject.parseObject(payload, ShopCarDTO.class);
            //操作redis
            BoundHashOperations boundHashOps = redisTemplate.boundHashOps(Constant.USER_SHOP_CAR_PREFIX + shopCarDTO.getUserId());
            String key = shopCarDTO.getSkuId().toString();
            Boolean hasKey = boundHashOps.hasKey(key);
            if (hasKey){
                String result = (String) boundHashOps.get(key);
                ShopCar shopCarResult = JSONObject.parseObject(result, ShopCar.class);
                //查看redis中数据的删除状态是否为true
                Boolean deleteState = shopCarResult.getDelete();
                if (deleteState){
                    return  RocketMQLocalTransactionState.COMMIT;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  RocketMQLocalTransactionState.ROLLBACK;
        }
        return  RocketMQLocalTransactionState.COMMIT;
    }

}