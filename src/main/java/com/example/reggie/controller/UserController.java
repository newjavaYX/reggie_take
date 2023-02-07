package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie.common.R;
import com.example.reggie.domain.User;
import com.example.reggie.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @PostMapping("login")
    public R<String> login(@RequestBody User user, HttpServletRequest request){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,user.getPhone());
        User byId = userService.getOne(wrapper);
        if (byId!=null){
            request.getSession().setAttribute("user",byId.getId());
            log.info("登入成功");
            return R.success("登入成功");
        }else{
            userService.save(user);
            request.getSession().setAttribute("user",user.getId());
            log.info("注册成功");
            return R.success("注册成功");
        }
    }
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
                request.getSession().removeAttribute("user");
                return R.success("退出成功");
    }
}

