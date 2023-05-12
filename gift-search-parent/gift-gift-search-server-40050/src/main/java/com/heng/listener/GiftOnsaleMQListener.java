package com.heng.listener;/**
 * @author shkstart
 * @create 2023-05-10 15:33
 */


import com.alibaba.fastjson.JSONArray;
import com.heng.constants.Constant;
import com.heng.doc.GiftDoc;
import com.heng.repository.GiftDocRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/10日15:33
 *@Description:消费者监听消息，并消费消息，将信息保存到es中
 */
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = Constant.SPU_ONSALE_CONSUMER_GROUP, topic = Constant.SPU_ONSALE_TX_TOPIC, selectorExpression = "*")
public class GiftOnsaleMQListener implements RocketMQListener<MessageExt> {

    @Autowired
    private GiftDocRepository giftDocRepository;

    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            //获取信息
            byte[] body = messageExt.getBody();
            String jsonStr = new String(body,"UTF-8");
            log.info(jsonStr);
            List<GiftDoc> giftDocs = JSONArray.parseArray(jsonStr, GiftDoc.class);
            //保存doc到es中
            giftDocRepository.saveAll(giftDocs);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //不要捕获异常，rocket会有自动的ack处理报错后消息会重新发送，如果捕获异常会影响结果
            throw new RuntimeException("消费消息失败");
        }

    }
}
