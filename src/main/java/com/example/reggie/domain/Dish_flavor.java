package com.example.reggie.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Long dish_id;

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
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime create_time;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill= FieldFill.INSERT)
    private Long create_user;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long update_user;

    /**
     * 是否删除
     */
    private Integer is_deleted;


}
