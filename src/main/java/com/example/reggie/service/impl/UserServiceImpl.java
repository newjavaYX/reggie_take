package com.example.reggie.service.impl;

import com.example.reggie.domain.User;
import com.example.reggie.dao.UserDao;
import com.example.reggie.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {

}
