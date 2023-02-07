package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.BeasContext;
import com.example.reggie.common.ReggieException;
import com.example.reggie.dao.UserDao;
import com.example.reggie.domain.*;
import com.example.reggie.dao.OrdersDao;
import com.example.reggie.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.uitl.UuidUitl;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;

import java.lang.invoke.LambdaConversionException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements IOrdersService {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private Shopping_cartServiceImpl shopping_cartService;

    @Autowired
    private Address_bookServiceImpl address_bookService;

    @Autowired
    private Order_detailServiceImpl order_detailService;

    @Autowired
    private OrdersDao ordersDao;

    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    @Override
    public void submit(Orders orders) throws Exception {
        //设置订单id
        long orderId = UuidUitl.uniqId();

        log.info("orderId:"+orderId);
        orders.setId(orderId);

        //设置订单号
        orders.setNumber(UUID.randomUUID().toString());

        //设置订单状态
        orders.setStatus(2);

        //设置下单用户id
        orders.setUserId(BeasContext.getCurrentId());

        //查询用户购物车数据
        LambdaQueryWrapper<Shopping_cart> queryWrapperCart = new LambdaQueryWrapper<>();
        queryWrapperCart.eq(Shopping_cart::getUserId,BeasContext.getCurrentId());
        List<Shopping_cart> shopping_carts = shopping_cartService.list(queryWrapperCart);

        if (shopping_carts==null || shopping_carts.size() == 0) throw new ReggieException("购物车为空");

        //创建总金额变量
        AtomicInteger amount =new AtomicInteger(0);
        //创建订单详细数据
        List<Order_detail> order_details = shopping_carts.stream().map((item)-> {
            Order_detail orderDetail = new Order_detail();
            orderDetail.setOrder_id(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDish_id(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            //计算总金额
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //订单表中设置总金额
        orders.setAmount(new BigDecimal(amount.get()));

        //设置用户名 设置用户电话
        LambdaQueryWrapper<User> queryWrapperOrder = new LambdaQueryWrapper<>();
        queryWrapperOrder.eq(User::getId,BeasContext.getCurrentId());
        User one = userService.getOne(queryWrapperOrder);
        orders.setUserName(one.getName());
        orders.setPhone(one.getPhone());

        //设置地址 设置收货人
        LambdaQueryWrapper<Address_book> queryWrapperAddressBook = new LambdaQueryWrapper<>();
        queryWrapperAddressBook.eq(Address_book::getId,orders.getAddressBookId());
        Address_book addressBook = address_bookService.getOne(queryWrapperAddressBook);
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //收货人
        orders.setConsignee(addressBook.getConsignee());

        //设置下单时间
        orders.setOrderTime(LocalDateTime.now());

        //设置结账时间
        orders.setCheckoutTime(LocalDateTime.now());

        //设置支付方式
        orders.setPayMethod(1);

        log.info("order:"+orders.toString());

        //保存订单
        this.save(orders);

        //保存订单详细数据
        order_detailService.saveBatch(order_details);

        //清空购物车
        shopping_cartService.remove(queryWrapperCart);
    }

    @Override
    public Page<Orders> userPage(Integer page, Integer pageSize) {
        IPage<Orders> pageUser = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId,BeasContext.getCurrentId());
        wrapper.orderByDesc(Orders::getCheckoutTime);
        ordersDao.selectPage(pageUser,wrapper);
        return (Page<Orders>) pageUser;
    }
}
