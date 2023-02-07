package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.BeasContext;
import com.example.reggie.common.R;
import com.example.reggie.domain.Orders;
import com.example.reggie.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private IOrdersService ordersService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) throws Exception {
        log.info("Sending order:" + orders.toString());
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    //GET http://localhost/order/userPage?page=1&pageSize=5
    @GetMapping("/userPage")
    public R<Page> userPage(Integer page, Integer pageSize){
        Page<Orders> userPage = ordersService.userPage(page, pageSize);
        return R.success(userPage);
    }
}

