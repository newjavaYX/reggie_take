package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.reggie.common.BeasContext;
import com.example.reggie.common.R;
import com.example.reggie.domain.Shopping_cart;
import com.example.reggie.service.impl.Shopping_cartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@RestController
@RequestMapping("/shoppingCart")
public class Shopping_cartController {
    @Autowired
    Shopping_cartServiceImpl shopping_cartService;

    @PostMapping("/add")
    public R<Shopping_cart> add(@RequestBody Shopping_cart shopping_cart){
        LambdaQueryWrapper<Shopping_cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shopping_cart::getName,shopping_cart.getName());
        wrapper.eq(Shopping_cart::getUserId,BeasContext.getCurrentId());
        Shopping_cart one = shopping_cartService.getOne(wrapper);
        if (one == null) {
        shopping_cart.setUserId(BeasContext.getCurrentId());
        shopping_cart.setNumber(1);
        shopping_cart.setCreateTime(LocalDateTime.now());
        shopping_cartService.save(shopping_cart);
        return R.success(shopping_cart);
        }else {
            LambdaUpdateWrapper<Shopping_cart> update = new LambdaUpdateWrapper<>();
            update.eq(Shopping_cart::getName,one.getName());
            wrapper.eq(Shopping_cart::getUserId,BeasContext.getCurrentId());
            update.set(Shopping_cart::getNumber,one.getNumber()+1);
            shopping_cartService.update(update);
            Shopping_cart UpdateAfterData = shopping_cartService.getOne(update);
            return R.success(UpdateAfterData);
        }
    }

    @PostMapping("/sub")
    public R<Shopping_cart> sub(@RequestBody Shopping_cart shopping_cart) {
        if (shopping_cart.getDishId() != null) {
            LambdaQueryWrapper<Shopping_cart> queryWrapper = new LambdaQueryWrapper<>();
            Long dishId = shopping_cart.getDishId();
            queryWrapper.eq(Shopping_cart::getDishId,dishId);
            Shopping_cart DishOne = shopping_cartService.getOne(queryWrapper);
            if (DishOne != null) {
                if (DishOne.getNumber() != 1) {
                    LambdaUpdateWrapper<Shopping_cart> update = new LambdaUpdateWrapper<>();
                    update.eq(Shopping_cart::getDishId,dishId);
                    update.set(Shopping_cart::getNumber, DishOne.getNumber() - 1);
                    shopping_cartService.update(update);
                    DishOne.setNumber(DishOne.getNumber() - 1);
                    return R.success(DishOne);
                } else {
                    LambdaUpdateWrapper<Shopping_cart> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(Shopping_cart::getDishId,dishId);
                    shopping_cartService.remove(updateWrapper);
                    return R.success(new Shopping_cart());
                }
            } else {
                return R.error("没有数据");
            }
        }else {
            LambdaQueryWrapper<Shopping_cart> queryWrapper = new LambdaQueryWrapper<>();
            Long setmealId = shopping_cart.getSetmealId();
            queryWrapper.eq(Shopping_cart::getSetmealId,setmealId);
            Shopping_cart setmeal = shopping_cartService.getOne(queryWrapper);
            if (setmeal != null) {
                if (setmeal.getNumber() != 1) {
                    LambdaUpdateWrapper<Shopping_cart> update = new LambdaUpdateWrapper<>();
                    update.eq(Shopping_cart::getSetmealId,setmealId);
                    update.set(Shopping_cart::getNumber, setmeal.getNumber() - 1);
                    shopping_cartService.update(update);
                    setmeal.setNumber(setmeal.getNumber() - 1);
                    return R.success(setmeal);
                } else {
                    LambdaUpdateWrapper<Shopping_cart> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(Shopping_cart::getSetmealId,setmealId);
                    shopping_cartService.remove(updateWrapper);
                    return R.success(new Shopping_cart());
                }
            } else {
                return R.error("没有数据");
            }
        }
    }

    @DeleteMapping("/clear")
    public R<String> clear() {
        Long currentId = BeasContext.getCurrentId();
        LambdaUpdateWrapper<Shopping_cart> update = new LambdaUpdateWrapper<>();
        update.eq(Shopping_cart::getId,currentId);
        shopping_cartService.remove(update);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Shopping_cart>> getCart(){
        LambdaQueryWrapper<Shopping_cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shopping_cart::getUserId,BeasContext.getCurrentId());
        List<Shopping_cart> list = shopping_cartService.list(wrapper);
        return R.success(list);
    }
}

