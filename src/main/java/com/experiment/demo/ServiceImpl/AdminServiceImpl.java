package com.experiment.demo.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.experiment.demo.Interface.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean check(String adminid, String password) {
        List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT password FROM Admin WHERE adminid = ?",new Object[]{adminid});
        if(list.size()>0 && list.get(0).get("password").toString().equals(password)){
            return  true;
        }
        return false;
    }

    @Override
    public JSONObject ReturnAdmistratorInfo(String adminid) {
        JSONObject res = new JSONObject();
        List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT adminid,name FROM Admin WHERE adminid = ?",adminid);
        res.put("adminid",list.get(0).get("adminid").toString());
        res.put("name",list.get(0).get("name").toString());
        return res;

    }

    @Override
    public boolean AwardModify(String year, String term, String classid, String designation) {
        String sql = "UPDATE StudentScore SET exchange = '1' WHERE year = ? and term = ? and designation = ? and studentid in (SELECT studentid FROM Class WHERE classid = ?)";
        Object []args = new Object[]{year,term,designation,classid};
        try {
            jdbcTemplate.update(sql,args);
        }catch (Exception e){
            throw (e);
        }
        return true;
    }

    @Override
    public boolean AwardRevoke(String year, String term, String classid, String designation) {
        String sql = "UPDATE StudentScore SET exdelete = '1' WHERE year = ? and term = ? and designation = ? and studentid in (SELECT studentid FROM Class WHERE classid = ?)";
        Object []args = new Object[]{year,term,designation,classid};
        try {
            jdbcTemplate.update(sql,args);
        }catch (Exception e){
            throw(e);
        }
        return true;
    }
}
