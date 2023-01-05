package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.domain.Category;
import com.example.reggie.domain.Dish;
import com.example.reggie.domain.Setmeal;
import com.example.reggie.dao.SetmealDao;
import com.example.reggie.domain.Setmeal_dish;
import com.example.reggie.dto.DishDto;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.service.ISetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements ISetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private Setmeal_dishServiceImpl setmealDishService;

    @Autowired
    private CategoryServiceImpl categoryService;
    @Override
    public Page<SetmealDto> getPage(Integer page, Integer pageSize, String name) {
        Page<Setmeal> setmealPagepage = new Page(page, pageSize);
        Page<SetmealDto> setmealDtoPage=new Page<>();
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealDao.selectPage(setmealPagepage, wrapper);

        BeanUtils.copyProperties(setmealPagepage,setmealDtoPage,"records");

        List<Setmeal> records = setmealPagepage.getRecords();
        setmealDtoPage.setRecords(records.stream().map(item ->{
            SetmealDto dto = new SetmealDto();
            BeanUtils.copyProperties(item,dto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            dto.setCategoryName(byId.getName());
            return dto;
        }).collect(Collectors.toList()));
        return setmealDtoPage;
    }
}
