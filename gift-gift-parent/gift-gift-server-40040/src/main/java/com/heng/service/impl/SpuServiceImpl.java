package com.heng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.heng.DTO.SpuOnsaleDTO;
import com.heng.constants.Constant;
import com.heng.domain.Spu;
import com.heng.exception.Assertion;
import com.heng.exception.ResponseCode;
import com.heng.mapper.SpuMapper;
import com.heng.service.ISpuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.heng.util.AjaxResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * spu表 服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements ISpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public AjaxResult onsale(List<Long> ids) {
        // 参数校验
        Assertion.isNotNull(ids, ResponseCode.RESPONSE_CODE_400);
        // 查询选择的ids对象是否满足上架条件
        List<Spu> spus = spuMapper.selectList(new EntityWrapper<Spu>().in("id", ids).and().eq("state", Constant.SPU_STATE_APPROVED));
        Assertion.isFalse(spus.size() < ids.size(), ResponseCode.RESPONSE_CODE_400_ONSALE_ERROR);
        // 发送事务消息
         List<SpuOnsaleDTO> spuDtos = new ArrayList<>();
//        List<SpuOnsaleDTO> spuDtos = BeanUtil.copyToList(spus, SpuOnsaleDTO.class);
        for (Spu spu : spus){
            SpuOnsaleDTO spuOnsaleDTO = new SpuOnsaleDTO();
            spuOnsaleDTO.setId(spu.getId());
            spuOnsaleDTO.setSpuId(spu.getId());
            spuOnsaleDTO.setCover(spu.getCover());
            spuOnsaleDTO.setName(spu.getGoodsName());
            spuOnsaleDTO.setTenantId(spu.getTenantId());
            spuOnsaleDTO.setTenantName(spu.getTenantName());
            spuOnsaleDTO.setOnsaleTime(new Date());
            spuOnsaleDTO.setTypeId(spu.getCategoryId());
            spuOnsaleDTO.setTypeName("口红");
            spuOnsaleDTO.setBrandId(spu.getBrandId());
            spuOnsaleDTO.setBrandName("香奈儿");
            spuOnsaleDTO.setCommentCount(0);
            spuOnsaleDTO.setSaleCount(0);
            spuOnsaleDTO.setSkuProperties("");
            spuOnsaleDTO.setSkuProperties("");
            spuOnsaleDTO.setMaxPrice(spu.getHighPrice());
            spuOnsaleDTO.setMinPrice(spu.getLowPrice());

            spuDtos.add(spuOnsaleDTO);
        }
        String jsonString = JSONArray.toJSONString(spuDtos);
        Message<String> message = MessageBuilder.withPayload(jsonString).build();
        rocketMQTemplate.sendMessageInTransaction(Constant.SPU_ONSALE_TX_PRODUCER_GROUP, Constant.SPU_ONSALE_TX_TOPIC + ":" + Constant.SPU_ONSALE_TX_TOPIC_TAG, message, null);
        return AjaxResult.me();
    }

    @Override
    public AjaxResult offsale(List<Long> ids) {
        return null;
    }

    @Override
    public AjaxResult executeTransaction(List<SpuOnsaleDTO> spuOnsaleDTOs) {
        // 修改数据库状态为上架
        List<Long> spuIds = spuOnsaleDTOs.stream().map(spuOnsaleDTO -> spuOnsaleDTO.getSpuId()).collect(Collectors.toList());
        spuMapper.onsaleUpdate(spuIds, Constant.SPU_STATE_ONSALE);
        // 保存上架日志
        return AjaxResult.me();
    }

    @Override
    public boolean checkTransaction(List<Long> spuIds) {
        List<Spu> spuList = spuMapper.selectList(new EntityWrapper<Spu>().in("id", spuIds).and().eq("state", Constant.SPU_STATE_ONSALE));
        return spuList.size() > 0 ? true : false;
    }
}
