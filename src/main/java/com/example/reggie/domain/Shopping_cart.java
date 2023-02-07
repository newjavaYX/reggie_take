package com.example.reggie.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("shopping_cart")
public class Shopping_cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private String image;
    @TableField("user_id")
    private Long userId;
    @TableField("dish_id")
    private Long dishId;
    @TableField("setmeal_id")
    private Long setmealId;
    @TableField("dish_flavor")
    private String dishFlavor;
    private Integer number;
    private BigDecimal amount;
    @TableField("create_time")
    private LocalDateTime createTime;
}
