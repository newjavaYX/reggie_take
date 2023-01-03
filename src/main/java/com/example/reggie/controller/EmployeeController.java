package com.example.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.domain.Employee;
import com.example.reggie.service.IEmployeeService;
import com.example.reggie.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author yx
 * @since 2022-11-09
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeServiceImpl employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
//        1。获取密码并进行md5加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes(StandardCharsets.UTF_8));
//        2.根据用户名查询用户是否存在
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(wrapper);
//        3.如果没有查到结果返回错误消息
        if (emp==null) {
            return R.error("登入失败");
        }
//        4.判断密码正确？
        if(!emp.getPassword().equals(password)){
            return R.error("登入失败");
        }
//        5.判断账号是否异常
        if(emp.getStatus()==0){
            return R.error("账号异常");
        }
//        6.登入成功，将id存入session中保存
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清除存储在服务器中的Session
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 添加员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        //设置md5加密密码
        String password = DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8));
        employee.setPassword(password);

//        //设置创建时间和更新时间
//        employee.setCreate_time(LocalDateTime.now());
//        employee.setUpdate_time(LocalDateTime.now());
//        //设置创建人以及更新人
//        employee.setCreate_user((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdate_user((Long) request.getSession().getAttribute("employee"));

        //将数据保存到数据库中
        if(employeeService.save(employee)){
            return R.success("保存成功");
        }else {
            return R.error("保存失败");
        }
    }

    /**
     * 请求员工分页信息
     * @param page 页码
     * @param pageSize 每页显示条数
     * @param name 模糊查询条件，如果为空就不进行模糊查询
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageEmployees(int page, int pageSize, String name){
        log.info("=======>  "+page+","+pageSize+"  <===>  "+name);
        Page<Employee> page1 = employeeService.getPage(page, pageSize,name);
        log.info("page: " + page1);
        return R.success(page1);
    }

    /**
     * 员工状态修改
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
//        Long employeeId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdate_user(employeeId);
//        employee.setUpdate_time(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> updateEmployeeOne(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee!=null){
        return R.success(employee);
        }
        return R.error("查无此用户");
    }
}

