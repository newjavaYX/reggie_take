package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.dao.Dish_flavorDao;
import com.example.reggie.domain.Category;
import com.example.reggie.domain.Dish;
import com.example.reggie.domain.Dish_flavor;
import com.example.reggie.dto.DishDto;
import com.example.reggie.service.impl.DishServiceImpl;
import com.example.reggie.service.impl.Dish_flavorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.naming.InsufficientResourcesException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private Dish_flavorServiceImpl dishFlavorService;
    @GetMapping("/page")
    public R<Page> getDishPage(Integer page, Integer pageSize,String name){
        log.info("============="+page + "---" +pageSize + " ---" +name);
        Page<DishDto> dishPage = dishService.getDishPage(page, pageSize,name);
        return R.success(dishPage);
    }

    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto){
        dishService.saveWhitFlavor(dishDto);
        return R.success("保存成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> updateOne(@PathVariable Long id){
        Dish byId = dishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId,dishDto);
        LambdaQueryWrapper<Dish_flavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish_flavor::getDishId,id);
        dishDto.setFlavors(dishFlavorService.list(wrapper));
        return R.success(dishDto);
    }

    @PutMapping
    @Transactional
    public R<String> updateTow(@RequestBody DishDto dishDto){
        Long id = dishDto.getId();
        LambdaQueryWrapper<Dish_flavor> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Dish_flavor::getDishId,id);
        log.info("删除原有口味");
        dishFlavorService.remove(wrapper);

        log.info("更新菜品信息");
        dishService.updateById(dishDto);

        //设置菜品关联的口味信息中的菜品id
        List<Dish_flavor> flavors = dishDto.getFlavors();
        flavors.stream().map(item -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        log.info("重新保存口味");
        dishFlavorService.saveBatch(flavors);


        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> listDish(Long categoryId){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> collect = list.stream().map(item -> {
            Long id = item.getId();
            LambdaQueryWrapper<Dish_flavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Dish_flavor::getDishId, id);
            List<Dish_flavor> list1 = dishFlavorService.list(wrapper);
            DishDto dishDto = new DishDto();
            dishDto.setFlavors(list1);
            BeanUtils.copyProperties(item, dishDto);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(collect);
    }
}

