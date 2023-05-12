package com.heng.service.impl;

import com.heng.domain.Category;
import com.heng.mapper.CategoryMapper;
import com.heng.service.ICategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-09
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
