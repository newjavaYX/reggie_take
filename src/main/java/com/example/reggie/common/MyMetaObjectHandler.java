package com.example.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 元数据对象处理类
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 处理添加操作时需要处理的公共数据
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //获取当前用户id
        Long id = BeasContext.getCurrentId();
        log.info("当前用户id:{}",id);
        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);
    }

    /**
     * 处理更新数据时需要统一处理的数据
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //获取当前用户id
        Long id = BeasContext.getCurrentId();
        log.info("《--更新--》:当前用户id:{}",id);
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",id);
    }
}
