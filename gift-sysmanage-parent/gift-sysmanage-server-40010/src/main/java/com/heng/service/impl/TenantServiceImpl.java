package com.heng.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.heng.domain.Tenant;
import com.heng.mapper.TenantMapper;
import com.heng.query.TenantQuery;
import com.heng.service.ITenantService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.heng.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-04-24
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

    @Autowired
    private TenantMapper tenantMapper;

    @Override
    public PageList selectPageList(TenantQuery query) {
        Page<Tenant> page = new Page<Tenant>(query.getPage(), query.getRows());
        List<Tenant> result = tenantMapper.selectPageList(page,query);
        PageList<Tenant> pageList = new PageList<>(page.getTotal(), result);
        return pageList;
    }
}
