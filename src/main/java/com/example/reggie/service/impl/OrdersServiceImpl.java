package com.example.reggie.service.impl;

import com.example.reggie.domain.Orders;
import com.example.reggie.dao.OrdersDao;
import com.example.reggie.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements IOrdersService {

}
