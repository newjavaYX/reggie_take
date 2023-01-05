package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.domain.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggie.dto.SetmealDto;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
public interface ISetmealService extends IService<Setmeal> {
    Page<SetmealDto> getPage(Integer page, Integer pageSize, String name);
}
