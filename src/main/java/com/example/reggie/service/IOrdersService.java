package com.example.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
public interface IOrdersService extends IService<Orders> {

     void submit(Orders order) throws Exception;
     Page<Orders> userPage(Integer page,Integer pageSize);
}
