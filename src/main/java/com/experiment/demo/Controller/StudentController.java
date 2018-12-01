package com.experiment.demo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.experiment.demo.ServiceImpl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    StudentServiceImpl studentService;
    
    //成绩查询
    //测试通过
    //@CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/query")
    public JSONArray query(@RequestParam(value = "year") String year,
                           @RequestParam(value = "term") String term,
                           @RequestParam(value = "studentid") String studentid){
        System.out.println("studentController query OK");
        return (studentService.QueryInfo(studentid,year,term));
    }
}
