package com.experiment.demo.Interface;

import com.alibaba.fastjson.JSONObject;

public interface AdminService {

    boolean check(String adminid,String password);

    JSONObject ReturnAdmistratorInfo(String adminid);

    boolean AwardModify(String year, String term, String classid,String designation);//授权修改

    boolean AwardRevoke(String year, String term, String classid,String designation);//授权撤销
}
