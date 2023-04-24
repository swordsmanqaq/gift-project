package com.heng.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.heng.domain.Tenant;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.heng.query.TenantQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-04-24
 */
public interface TenantMapper extends BaseMapper<Tenant> {

    List<Tenant> selectPageList(Page<Tenant> page, TenantQuery query);

}
