package com.heng.mapper;

import com.heng.domain.Spu;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * spu表 Mapper 接口
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
public interface SpuMapper extends BaseMapper<Spu> {

    void onsaleUpdate(@Param("spuIds") List<Long> spuIds, @Param("state") int state);

}
