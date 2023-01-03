package com.example.reggie.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.domain.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 菜品管理 Mapper 接口
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
public interface DishDao extends BaseMapper<Dish> {
    Page<Dish> getDishAndCategoryPage(@Param("page") Page<Dish> page, @Param("name") String name);
}
