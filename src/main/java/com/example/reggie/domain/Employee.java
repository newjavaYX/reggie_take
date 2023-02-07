package com.example.reggie.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 员工信息
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份证号
     */
    @TableField("id_number")
    private String idNumber;

    /**
     * 状态 0:禁用，1:正常
     */
    private Integer status;

    /**
     * 创建时间
     * @TableField(fill = FieldFill.INSERT)
     * 将该字段设置为公共数据，在添加操作时在元数据对象处理类（MyMetaObjectHandler）中统一处理
     */
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     * @TableField(fill = FieldFill.INSERT_UPDATE)
     * 将该字段设置为公共数据，在添加和修改时操作时在元数据对象处理类（MyMetaObjectHandler）中统一处理
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value ="update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     * @TableField(fill = FieldFill.INSERT)
     * 将该字段设置为公共数据，在添加操作时在元数据对象处理类（MyMetaObjectHandler）中统一处理
     */
    @TableField(fill = FieldFill.INSERT,value = "create_user")
    private Long createUser;

    /**
     * 修改人
     * @TableField(fill = FieldFill.INSERT_UPDATE)
     * 将该字段设置为公共数据，在添加和修改时操作时在元数据对象处理类（MyMetaObjectHandler）中统一处理
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_user")
    private Long updateUser;


}
