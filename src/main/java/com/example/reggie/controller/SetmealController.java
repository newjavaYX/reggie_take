package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.BeasContext;
import com.example.reggie.common.R;
import com.example.reggie.domain.Setmeal;
import com.example.reggie.domain.Setmeal_dish;
import com.example.reggie.dto.SetmealDto;
import com.example.reggie.service.impl.SetmealServiceImpl;
import com.example.reggie.service.impl.Setmeal_dishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SetmealServiceImpl setmealService;

    @Autowired
    private Setmeal_dishServiceImpl setmeal_dishService;

    @GetMapping("/page")
    public R<Page> getPage(Integer page, Integer pageSize, String name) {
        Page<SetmealDto> list = setmealService.getPage(page, pageSize, name);
        return R.success(list);
    }

    @CacheEvict(value = "Cache_Setmeal",allEntries = true)
    @Transactional
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        System.out.println(setmealDto);
        //保存套餐基本信息
        setmealService.save(setmealDto);

        //获取自动生成的id
        String setmealId = String.valueOf(setmealDto.getId());


        List<Setmeal_dish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmeal_dishService.saveBatch(setmealDishes);
        return R.success("保存成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> updateOne(@PathVariable Long id) {
        //查询套餐基本信息
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();

        //为setmealDto复制查询出的套餐信息
        BeanUtils.copyProperties(setmeal, setmealDto);

        //获取查询出的套餐信息id
        Long setmealId = setmeal.getId();
        LambdaQueryWrapper<Setmeal_dish> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Setmeal_dish::getSetmealId, setmealId);

        //查询套餐中的菜品
        List<Setmeal_dish> list = setmeal_dishService.list(wrapper);
        setmealDto.setSetmealDishes(list);
        return R.success(setmealDto);
    }
    @CacheEvict(value = "Cache_Setmeal",allEntries = true)
    @PutMapping
    public R<String> updateTow(@RequestBody SetmealDto setmealDto) {
        setmealService.updateById(setmealDto);

        Long id = setmealDto.getId();
        LambdaQueryWrapper<Setmeal_dish> query = new LambdaQueryWrapper();
        query.eq(Setmeal_dish::getSetmealId, id);
        setmeal_dishService.remove(query);

        List<Setmeal_dish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(String.valueOf(id));
            return item;
        }).collect(Collectors.toList());

        setmeal_dishService.saveBatch(setmealDishes);
        return R.success("更新成功");
    }

    @CacheEvict(value = "Cache_Setmeal",allEntries = true)
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam("ids") String ids){
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Setmeal::getId,ids);
        updateWrapper.set(Setmeal::getStatus,status);
        updateWrapper.set(Setmeal::getUpdateTime, LocalDateTime.now());
        Long id = BeasContext.getCurrentId();
        updateWrapper.set(Setmeal::getUpdateUser,id);
        setmealService.update(updateWrapper);
        log.info("修改状态成功");
        return R.success("修改成功");
    }

    @Cacheable(value = "Cache_Setmeal",key ="#root.args[0]+'_'+#root.args[1]",unless ="#result == null")
    @GetMapping("/list")
    public R<List<Setmeal>> list(@RequestParam Long categoryId,@RequestParam Integer status){
//        String key = "setmeal_" + categoryId + "_" + status;
//        List<Setmeal> setmeals = (List<Setmeal>) redisTemplate.opsForValue().get(key);
//        if (setmeals != null) {
//            return R.success(setmeals);
//        }

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Setmeal::getCategoryId,categoryId);
        wrapper.eq(Setmeal::getStatus,status);
        List<Setmeal> list = setmealService.list(wrapper);

//        redisTemplate.opsForValue().set(key,list,60, TimeUnit.MINUTES);

        return R.success(list);
    }
}