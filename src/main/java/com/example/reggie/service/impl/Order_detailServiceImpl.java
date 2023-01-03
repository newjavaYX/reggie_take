package com.example.reggie.service.impl;

import com.example.reggie.domain.Order_detail;
import com.example.reggie.dao.Order_detailDao;
import com.example.reggie.service.IOrder_detailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Service
public class Order_detailServiceImpl extends ServiceImpl<Order_detailDao, Order_detail> implements IOrder_detailService {

}
