package com.experiment.demo.Interface;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface StudentService {
    boolean check(String studentid,String password);

    JSONObject ReturnStudentInfo(String studentid);//如果登录成功，返回学生信息

    JSONArray QueryInfo(String studentid, String year, String term);//查询成绩
}
