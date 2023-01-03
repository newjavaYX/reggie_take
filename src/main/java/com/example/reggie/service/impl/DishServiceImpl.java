package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.domain.Category;
import com.example.reggie.domain.Dish;
import com.example.reggie.dao.DishDao;
import com.example.reggie.domain.Dish_flavor;
import com.example.reggie.dto.DishDto;
import com.example.reggie.service.IDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.logging.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements IDishService {
    @Autowired
    private DishDao dishDao;
    @Autowired
    private Dish_flavorServiceImpl dish_flavorService;
    @Autowired
    private CategoryServiceImpl categoryService;

    public Page<DishDto> getDishPage(int currentPage, int pageSize, String name) {
        Page<Dish> page = new Page<>(currentPage,pageSize);
        Page<DishDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishDao.selectPage(page, wrapper);

        BeanUtils.copyProperties(page,dtoPage,"records");

        List<Dish> records = page.getRecords();

        dtoPage.setRecords(records.stream().map((item) ->{
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(item,dto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dto.setCategoryName(category.getName());
            return dto;
        }).collect(Collectors.toList()));
        return dtoPage;
    }


    @Override
    @Transactional
    public void saveWhitFlavor(DishDto dishDto){
        //保存菜品基本信息
        this.save(dishDto);

        //获取保存后自动生成的id
        Long id = dishDto.getId();
        log.info("自动生成的id{"+id+"}");

        //设置菜品关联的口味信息中的菜品id
        List<Dish_flavor> flavors = dishDto.getFlavors();
        flavors.stream().map(item -> {
            item.setDish_id(id);
            return item;
        }).collect(Collectors.toList());
        log.info("设置菜品关联的口味信息中的菜品id"+flavors);

        //将口味保存到口味表中
        dish_flavorService.saveBatch(flavors);
    }
}
