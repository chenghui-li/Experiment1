package com.experiment.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
public class home {

    @Autowired
    public StudentProperties studentProperties;
    @RequestMapping(method = RequestMethod.GET,value = "/hello")
    //@pathvariable 获取url中的数据
    //@requestParam 获取url参数的值
    public String sayHello(/*@PathVariable("id") Integer id*/@RequestParam(value="id",required = false,defaultValue = "0") Integer id){

        return studentProperties.getClassnum()+studentProperties.getName()+id;
    }
}
