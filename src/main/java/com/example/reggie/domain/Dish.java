package com.example.reggie.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜品管理
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dish")
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品分类id
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 菜品价格
     */
    private BigDecimal price;

    /**
     * 商品码
     */
    private String code;

    /**
     * 图片
     */
    private String image;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 0 停售 1 起售
     */
    private Integer status;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT,value = "create_user")
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
