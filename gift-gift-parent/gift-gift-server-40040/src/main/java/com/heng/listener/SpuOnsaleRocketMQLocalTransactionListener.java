package com.heng.listener;

import com.alibaba.fastjson.JSONArray;
import com.heng.DTO.SpuOnsaleDTO;
import com.heng.constants.Constant;
import com.heng.service.ISpuService;
import com.heng.util.AjaxResult;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RocketMQTransactionListener(txProducerGroup = Constant.SPU_ONSALE_TX_PRODUCER_GROUP)
public class SpuOnsaleRocketMQLocalTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    private ISpuService spuService;

    //执行本地事务
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            byte[] bytes = (byte[]) message.getPayload();
            String jsonStr = new String(bytes, "utf-8");
            List<SpuOnsaleDTO> spuOnsaleDTOS = JSONArray.parseArray(jsonStr, SpuOnsaleDTO.class);
            AjaxResult ajaxResult = spuService.executeTransaction(spuOnsaleDTOS);
            if (ajaxResult.isSuccess()){
                return RocketMQLocalTransactionState.COMMIT;
            }else{
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
            String jsonStr = new String(bytes, "UTF-8");
            List<SpuOnsaleDTO> spuOnsaleDTOS = JSONArray.parseArray(jsonStr, SpuOnsaleDTO.class);
            List<Long> spuIds = spuOnsaleDTOS.stream().map(spuOnsaleDTO -> spuOnsaleDTO.getSpuId()).collect(Collectors.toList());
            boolean result = spuService.checkTransaction(spuIds);
            return result ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
        } catch (Exception e) {
            e.printStackTrace();
            return  RocketMQLocalTransactionState.ROLLBACK;
        }
    }

}