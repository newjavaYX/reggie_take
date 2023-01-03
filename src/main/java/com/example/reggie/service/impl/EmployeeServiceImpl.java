package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.domain.Employee;
import com.example.reggie.dao.EmployeeDao;
import com.example.reggie.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements IEmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

    public Page<Employee> getPage(int currentPage, int pageSize,String name) {
        IPage<Employee> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        wrapper.orderByDesc(Employee::getUpdateTime);
        employeeDao.selectPage(page, wrapper);
        return (Page<Employee>) page;
    }
}
