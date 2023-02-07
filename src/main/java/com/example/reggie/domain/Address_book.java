package com.example.reggie.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 地址管理
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("address_book")
public class Address_book implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 性别 0 女 1 男
     */
    private String sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 省级区划编号
     */
    @TableField("province_code")
    private String provinceCode;

    /**
     * 省级名称
     */
    @TableField("province_name")
    private String provinceName;

    /**
     * 市级区划编号
     */
    @TableField("city_code")
    private String cityCode;

    /**
     * 市级名称
     */
    @TableField("city_name")
    private String cityName;

    /**
     * 区级区划编号
     */
    @TableField("district_code")
    private String districtCode;

    /**
     * 区级名称
     */
    @TableField("district_name")
    private String districtName;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 标签
     */
    private String label;

    /**
     * 默认 0 否 1是
     */
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value ="update_time")
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
    @TableField("is_deleted")
    private Integer isDeleted;


}
