package com.heng.listener;/**
 * @author shkstart
 * @create 2023-05-10 15:33
 */


import com.alibaba.fastjson.JSONObject;
import com.heng.constants.Constant;
import com.heng.dto.ShopCarDTO;
import com.heng.service.IShopCarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/10日15:33
 *@Description:消费者监听消息，并消费消息
 */
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = Constant.SHOP_CAR_TX_CONSUMER_GROUP, topic = Constant.SHOP_CAR_TX_TOPIC, selectorExpression = "*")
public class ShopCarRocketMQConsumerListener implements RocketMQListener<MessageExt> {

    @Autowired
    private IShopCarService shopCarService;

    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            //获取信息
            byte[] body = messageExt.getBody();
            String jsonStr = new String(body,"UTF-8");
            ShopCarDTO shopCarDTO = JSONObject.parseObject(jsonStr, ShopCarDTO.class);
            //消费消息，执行删除操作
            shopCarService.deleteShopCarFromRedisAndDatabase(shopCarDTO);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //不要捕获异常，rocket会有自动的ack处理报错后消息会重新发送，如果捕获异常会影响结果
            throw new RuntimeException("消费消息失败");
        }

    }
}
