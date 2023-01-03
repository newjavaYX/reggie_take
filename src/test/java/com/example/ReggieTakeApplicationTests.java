package com.example;

import com.example.reggie.dao.EmployeeDao;
import com.example.reggie.domain.Employee;
import com.example.reggie.service.IEmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ReggieTakeApplicationTests {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private IEmployeeService iEmployeeService;
    @Test
    void contextLoads() {
        List<Employee> employees = employeeDao.selectList(null);
        employees.forEach(System.out::println);
    }
    @Test
    void contextLoads2() {
        List<Employee> employees = iEmployeeService.list(null);
    }
}
