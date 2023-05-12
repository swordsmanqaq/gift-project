package com.heng.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.heng.domain.TenantType;
import com.heng.mapper.TenantTypeMapper;
import com.heng.service.ITenantTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-04-24
 */
@Service
public class TenantTypeServiceImpl extends ServiceImpl<TenantTypeMapper, TenantType> implements ITenantTypeService {

    private static final String REDIS_TENANT_KEY = "redis_tenant_key";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TenantTypeMapper tenantTypeMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @Transactional
    public boolean insert(TenantType entity) {
        try {
            super.insert(entity);
            redisTemplate.delete(REDIS_TENANT_KEY);
            Thread.sleep(500);
            redisTemplate.delete(REDIS_TENANT_KEY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteById(Serializable id) {
        try {
            super.deleteById(id);
            redisTemplate.delete(REDIS_TENANT_KEY);
            Thread.sleep(500);
            redisTemplate.delete(REDIS_TENANT_KEY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional
    public boolean update(TenantType entity, Wrapper<TenantType> wrapper) {
        try {
            super.update(entity, wrapper);
            redisTemplate.delete(REDIS_TENANT_KEY);
            Thread.sleep(500);
            redisTemplate.delete(REDIS_TENANT_KEY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<TenantType> selectList(Wrapper<TenantType> wrapper) {
        Object value = redisTemplate.opsForValue().get(REDIS_TENANT_KEY);
        if (Objects.nonNull(value)) {
            return (List<TenantType>) value;
        } else {
            RLock lock = redissonClient.getLock(REDIS_TENANT_KEY);
            try {
                lock.lock();
                Object values = redisTemplate.opsForValue().get(REDIS_TENANT_KEY);
                if (Objects.nonNull(values)) {
                    return (List<TenantType>) values;
                }
                List<TenantType> tenants = tenantTypeMapper.selectList(null);
                redisTemplate.opsForValue().set(REDIS_TENANT_KEY, tenants);
                return tenants;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (lock != null){
                    lock.unlock();
                }
            }
        }
    }

}
