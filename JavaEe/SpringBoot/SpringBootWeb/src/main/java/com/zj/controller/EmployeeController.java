package com.zj.controller;

import com.zj.dao.DepartmentDao;
import com.zj.dao.EmployeeDao;
import com.zj.entities.Department;
import com.zj.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * Created by ZJ on 2020/7/14
 * comment:
 */

@Controller
public class EmployeeController {
    //自动注入员工
    @Autowired
    EmployeeDao employeeDao;

    //自动驻入部门
    @Autowired
    DepartmentDao departmentDao;
    /**
     * 查询所有员工返回列表页面
     * @return
     */
//    @RequestMapping()
    @GetMapping("/emps")
    public String list(Model model){
        Collection<Employee> employees = employeeDao.getAll();

        //放在请求域中共享
        model.addAttribute("emps",employees);

        return ("emp/list");
    }

    /**
     * 员工添加页面
     */
    @GetMapping("/emp")
    public String toAddPage(Model model){
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("departs",departments);
        return "emp/end";
    }
}
