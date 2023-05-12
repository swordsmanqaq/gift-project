package com.heng.service;

import com.heng.DTO.SpuOnsaleDTO;
import com.heng.domain.Spu;
import com.baomidou.mybatisplus.service.IService;
import com.heng.util.AjaxResult;

import java.util.List;

/**
 * <p>
 * spu表 服务类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
public interface ISpuService extends IService<Spu> {

    AjaxResult onsale(List<Long> ids);

    AjaxResult offsale(List<Long> ids);

    AjaxResult executeTransaction(List<SpuOnsaleDTO> spuOnsaleDTOs);

    boolean checkTransaction(List<Long> spuIds);

}
