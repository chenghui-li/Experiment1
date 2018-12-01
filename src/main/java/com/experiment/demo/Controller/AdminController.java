package com.experiment.demo.Controller;

import com.alibaba.fastjson.JSONObject;
import com.experiment.demo.ServiceImpl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    public AdminServiceImpl adminService;
    
    //暂存成绩
    @PostMapping("/awardmodify")
    public JSONObject awardmodify(@RequestParam(value = "year") String year,
                                  @RequestParam(value = "term") String term,
                                  @RequestParam(value = "classid") String classid,
                                  @RequestParam(value = "designation") String designation){
        System.out.println("adminController modify OK");
        try {
            adminService.AwardModify(year,term,classid,designation);

        }catch (Exception e){
            throw (e);
        }
        JSONObject res = new JSONObject();
        res.put("res","yes");
        return  res;

    }
    //撤销成绩
    @PostMapping("/awardrevoke")
    public JSONObject awardrevoke(@RequestParam(value = "year") String year,
                                  @RequestParam(value = "term") String term,
                                  @RequestParam(value = "classid") String classid,
                                  @RequestParam(value = "designation") String designation){
        System.out.println("adminController revoke OK");
        try {
            adminService.AwardRevoke(year,term,classid,designation);
        }catch (Exception e){
            throw (e);
        }
        JSONObject res = new JSONObject();
        res.put("res","yes");
        return  res;
    }
}
