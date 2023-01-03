package com.example.reggie.service.impl;

import com.example.reggie.domain.Address_book;
import com.example.reggie.dao.Address_bookDao;
import com.example.reggie.service.IAddress_bookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Service
public class Address_bookServiceImpl extends ServiceImpl<Address_bookDao, Address_book> implements IAddress_bookService {

}
