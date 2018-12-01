package com.experiment.demo;

import com.alibaba.fastjson.JSONObject;
import com.experiment.demo.ServiceImpl.AdminServiceImpl;
import com.experiment.demo.ServiceImpl.StudentServiceImpl;
import com.experiment.demo.ServiceImpl.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Login {

    @Autowired
    public StudentServiceImpl studentService;

    @Autowired
    public TeacherServiceImpl teacherService;

    @Autowired
    public AdminServiceImpl adminService;

    @PostMapping("/login")
    public JSONObject handleFun(@RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "role") String role
                          ){
        System.out.println(role);
        JSONObject obj = new JSONObject();
        if(role.equals("student")){
            boolean check = studentService.check(username,password);

            //调用ReturnInfo
            if(check) {
                obj.put("res", "yes");
                obj.put("info", studentService.ReturnStudentInfo(username));
                System.out.println("studentController Info OK");
            }
            else
                obj.put("res","no");
        }else if(role.equals("teacher")){
            boolean check = teacherService.check(username, password);
            if(check == true){
                obj.put("res","yes");
                obj.put("info",teacherService.ReturnTeacherInfo(username));
            }else{
                obj.put("res","no");
            }
        }else if(role.equals("admin")){
            boolean check = adminService.check(username,password);

            //调用ReturnInfo
            if(check) {
                obj.put("res", "yes");
                obj.put("info", adminService.ReturnAdmistratorInfo(username));
            }
            else
                obj.put("res","no");
        }
        return obj;
    }
}
