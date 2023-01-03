package com.example.reggie.common;

import com.example.reggie.domain.Employee;

/**
 * 封装ThreadLocal数据的工具类，保存单个线程中特定的变量
 */
public class BeasContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
