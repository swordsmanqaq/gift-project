package com.heng.service.impl;

import com.heng.domain.Brand;
import com.heng.mapper.BrandMapper;
import com.heng.service.IBrandService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

}
