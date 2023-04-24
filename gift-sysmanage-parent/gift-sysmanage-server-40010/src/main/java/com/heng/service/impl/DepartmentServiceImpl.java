package com.heng.service.impl;

import com.heng.domain.Department;
import com.heng.mapper.DepartmentMapper;
import com.heng.service.IDepartmentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-04-24
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
