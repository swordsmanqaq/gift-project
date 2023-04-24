package com.heng.service;

import com.heng.domain.Tenant;
import com.baomidou.mybatisplus.service.IService;
import com.heng.query.TenantQuery;
import com.heng.util.PageList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-04-24
 */
public interface ITenantService extends IService<Tenant> {

    PageList selectPageList(TenantQuery query);

}
