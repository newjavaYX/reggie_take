package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.domain.Category;
import com.example.reggie.domain.Employee;
import com.example.reggie.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/page")
    public R<Page> pageEmployees(int page, int pageSize){
        log.info("=======>  "+page+","+pageSize);
        Page<Category> page1 = categoryService.getCategoryPage(page, pageSize);
        log.info("page: " + page1);
        return R.success(page1);
    }

    @PostMapping
    public R<String> saveCategory(@RequestBody Category category){
        boolean flog = categoryService.save(category);
        if (flog) {
            return R.success("保存成功");
        }else {
            return R.error("保存失败");
        }
    }

    @PutMapping
    public R<String> updateCategory(@RequestBody Category category){
        boolean flag = categoryService.updateById(category);
        if (flag) {
            return R.success("修改成功");
        }else {
            return R.error("修改失败");
        }
    }

    @DeleteMapping
    public R<String> deleteCategory(Long ids){
        System.out.println(ids);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if (ids != null) {
        wrapper.eq(Category::getId,ids);
        boolean remove = categoryService.remove(wrapper);
            if (remove){
                return R.success("删除成功");
            }else {
                return R.error("删除失败");
            }
        }
        return R.error("id为空");
    }

    /**
     * 新增菜品時查詢所有菜品的分類
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getType(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType()!=null,Category::getType,category.getType());
        wrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }
}

