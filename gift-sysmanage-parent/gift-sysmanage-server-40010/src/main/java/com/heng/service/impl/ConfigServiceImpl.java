package com.heng.service.impl;

import com.heng.domain.Config;
import com.heng.mapper.ConfigMapper;
import com.heng.service.IConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-04-24
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
