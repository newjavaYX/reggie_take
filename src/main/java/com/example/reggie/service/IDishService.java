package com.example.reggie.service;

import com.example.reggie.domain.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.dto.DishDto;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
public interface IDishService extends IService<Dish> {
    void saveWhitFlavor(DishDto dishDto);
}
