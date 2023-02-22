package com.example.reggie.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜品口味关系表
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dish_flavor")
public class Dish_flavor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 菜品
     */
    @TableField(value = "dish_id")
    private Long dishId;

    /**
     * 口味名称
     */
    private String name;

    /**
     * 口味数据list
     */
    private String value;

    /**
     * 创建时间
     */
    @TableField(fill=FieldFill.INSERT,value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill= FieldFill.INSERT,value = "create_user")
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_user")
    private Long updateUser;

    /**
     * 是否删除
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;


}
