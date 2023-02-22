package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.domain.Dish;
import com.example.reggie.domain.Dish_flavor;
import com.example.reggie.dto.DishDto;
import com.example.reggie.service.impl.DishServiceImpl;
import com.example.reggie.service.impl.Dish_flavorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/page")
    public R<Page> getDishPage(Integer page, Integer pageSize,String name){
        log.info("============="+page + "---" +pageSize + " ---" +name);
        Page<DishDto> dishPage = dishService.getDishPage(page, pageSize,name);
        return R.success(dishPage);
    }

    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto){
        dishService.saveWhitFlavor(dishDto);
        Long categoryId = dishDto.getCategoryId();
        redisTemplate.delete("dish_"+categoryId+"_1");
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

        Long categoryId = dishDto.getCategoryId();
        redisTemplate.delete("dish_"+categoryId+"_1");

        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> listDish(Long categoryId){
        List<DishDto> collect;
        String key = "dish_"+categoryId+"_1";
        collect= (List<DishDto>) redisTemplate.opsForValue().get(key);
        log.info("<<----------------  Key="+key+"------------------>>");
        if (collect != null){
            return R.success(collect);
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId);
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);

        collect = list.stream().map(item -> {
            Long id = item.getId();
            LambdaQueryWrapper<Dish_flavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Dish_flavor::getDishId, id);
            List<Dish_flavor> list1 = dishFlavorService.list(wrapper);
            DishDto dishDto = new DishDto();
            dishDto.setFlavors(list1);
            BeanUtils.copyProperties(item, dishDto);
            return dishDto;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key,collect,60, TimeUnit.MINUTES);
        return R.success(collect);
    }
    @Transactional
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id){
        log.info("<----------------------删除 id ="+ id+"-------------------->");
        Dish d = dishService.getById(id);
        Long categoryId = d.getCategoryId();

        dishService.removeById(id);

        LambdaUpdateWrapper<Dish_flavor> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dish_flavor::getDishId,id);
        dishFlavorService.remove(wrapper);

        redisTemplate.delete("dish_"+categoryId+"_1");
        return R.success("删除成功");
    }
}

