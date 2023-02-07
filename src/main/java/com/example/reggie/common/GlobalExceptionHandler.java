package com.example.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R<String>doException(Exception ex){
        log.info("捕获异常"+ex);
        if(ex.getMessage().contains("Duplicate entry")){
            return R.error("名称已存在");
        }
        return R.error("未知错误");
    }
}
