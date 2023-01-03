package com.example.reggie.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("order_detail")
public class Order_detail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 订单id
     */
    private Long order_id;

    /**
     * 菜品id
     */
    private Long dish_id;

    /**
     * 套餐id
     */
    private Long setmeal_id;

    /**
     * 口味
     */
    private String dish_flavor;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 金额
     */
    private BigDecimal amount;


}
