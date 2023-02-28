package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.reggie.common.BeasContext;
import com.example.reggie.common.R;
import com.example.reggie.domain.Address_book;
import com.example.reggie.service.impl.Address_bookServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 地址管理 前端控制器
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class Address_bookController {
    @Autowired
    private Address_bookServiceImpl address_bookService;
    @PostMapping
    public R<String> save(@RequestBody Address_book address_book){
        Long userid = BeasContext.getCurrentId();
        address_book.setUserId(userid);
        address_book.setIsDefault(0);
        log.info("保存用户："+address_book);
        address_bookService.save(address_book);
        return R.success("保存成功");
    }

    @GetMapping("/list")
    public R<List<Address_book>> list(){
        Long userid = BeasContext.getCurrentId();
        LambdaQueryWrapper<Address_book> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Address_book::getUserId, userid);
        List<Address_book> addressbookList = address_bookService.list(wrapper);
        return  R.success(addressbookList);
    }

    @PutMapping("/default")
    public R<Address_book> defaultadderess(@RequestBody Address_book id){
            log.info("设置默认地址:" + id.toString());
            LambdaUpdateWrapper<Address_book> updateWrapper = new LambdaUpdateWrapper();
            updateWrapper.eq(Address_book::getUserId, BeasContext.getCurrentId());
            updateWrapper.set(Address_book::getIsDefault, 0);
            address_bookService.update(updateWrapper);
            LambdaUpdateWrapper<Address_book> updateWrapper2 = new LambdaUpdateWrapper();
            updateWrapper2.eq(Address_book::getId, id.getId());
            updateWrapper2.set(Address_book::getIsDefault, 1);
            updateWrapper2.set(Address_book::getUpdateTime, LocalDateTime.now());
            Long userId = BeasContext.getCurrentId();
            updateWrapper2.set(Address_book::getUpdateUser, userId);
            address_bookService.update(updateWrapper2);
            Address_book byId = address_bookService.getById(id);
            return R.success(byId);
    }

    @GetMapping("/{id}")
    public R<Address_book> getAddressBook(@PathVariable Long id){
        LambdaQueryWrapper<Address_book> Wrapper = new LambdaQueryWrapper<>();
        Long currentId = BeasContext.getCurrentId();
        Wrapper.eq(Address_book::getUserId,currentId);
        Wrapper.eq(Address_book::getId,id);
        Address_book byId = address_bookService.getOne(Wrapper);
        return R.success(byId);
    }

    @PutMapping
    public R<String> update(@RequestBody Address_book address_book){
        address_bookService.updateById(address_book);
        return R.success("修改成功");
    }

    @Transactional
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long ids){
        LambdaQueryWrapper<Address_book> queryWrapper = new LambdaQueryWrapper();
        Long currentId = BeasContext.getCurrentId();
        queryWrapper.eq(Address_book::getUserId,currentId);
        queryWrapper.eq(Address_book::getId,ids);
        Address_book one = address_bookService.getOne(queryWrapper);
        LambdaUpdateWrapper<Address_book> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Address_book::getUserId,currentId);
        wrapper.eq(Address_book::getId,ids);
        address_bookService.remove(wrapper);
        if (one.getIsDefault()==1){
            List<Address_book> list = address_bookService.list();
            Address_book address_book = list.get(0);
            Long id = address_book.getId();
            LambdaUpdateWrapper<Address_book> updateWrapper = new LambdaUpdateWrapper();
            updateWrapper.eq(Address_book::getUserId,currentId);
            updateWrapper.eq(Address_book::getId,id);
            updateWrapper.set(Address_book::getIsDefault,1);
            address_bookService.update(updateWrapper);
        }
        return R.success("修改成功");
    }

    @GetMapping("/default")
    public R<Address_book> getDefault() {
        LambdaQueryWrapper<Address_book> wrapper = new LambdaQueryWrapper<>();
        Long currentId = BeasContext.getCurrentId();
        wrapper.eq(Address_book::getUserId,currentId);
        wrapper.eq(Address_book::getIsDefault,1);
        Address_book one = address_bookService.getOne(wrapper);
        return R.success(one);
    }
}

