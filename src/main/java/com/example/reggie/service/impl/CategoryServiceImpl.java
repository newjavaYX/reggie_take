package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.domain.Category;
import com.example.reggie.dao.CategoryDao;
import com.example.reggie.domain.Employee;
import com.example.reggie.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements ICategoryService {
    @Autowired
    private CategoryDao categoryDao;

    public Page<Category> getCategoryPage(int currentPage, int pageSize) {
        IPage<Category> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        categoryDao.selectPage(page, wrapper);
        return (Page<Category>) page;
    }
}
