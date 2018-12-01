package com.experiment.demo.ServiceImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.experiment.demo.Interface.StudentService;
import com.experiment.demo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService,RowMapper<Student> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        Student student = new Student();
        student.studentid = resultSet.getString("studentid");
        student.name = resultSet.getString("name");
        student.gender = resultSet.getString("gender");
        student.password = resultSet.getString("password");
        return student;
    }

    @Override
    public boolean check(String studentid, String password) {
        List<Map<String ,Object>> list = jdbcTemplate.queryForList("SELECT password FROM Student WHERE studentid = ?",new Object[]{studentid});
        if(list.size()>0 && list.get(0).get("password").toString().equals(password)){
            return  true;
        }
        return false;
    }

    @Override
    public JSONObject ReturnStudentInfo(String studentid) {
        JSONObject res = new JSONObject();
        List<Map<String,Object> >list = jdbcTemplate.queryForList("SELECT studentid,studentname,gender FROM Student WHERE studentid = ?",studentid);
        res.put("studentid",list.get(0).get("studentid").toString());
        res.put("studentname",list.get(0).get("studentname").toString());
        res.put("gender",list.get(0).get("gender").toString());
        return res;
    }

    public JSONArray getScore(List<Map<String,Object> >list){
        JSONArray res = new JSONArray();
        for(int i = 0;i<list.size();i++){
            JSONObject now = new JSONObject();
            now.put("designation",list.get(i).get("designation").toString());
            now.put("scorep",(list.get(i).get("scorep") == null || list.get(i).get("scorep").toString().equals(""))?"0":list.get(i).get("scorep").toString());
            now.put("scorek",list.get(i).get("scorek").toString());
            now.put("scoreb",(list.get(i).get("scoreb") == null || list.get(i).get("scoreb").toString().equals(""))?"0":list.get(i).get("scoreb").toString());
            now.put("scoreq",(list.get(i).get("scoreq") == null || list.get(i).get("scoreq").toString().equals(""))?"0":list.get(i).get("scoreq").toString());
            now.put("finalscore",(list.get(i).get("finalscore") == null || list.get(i).get("finalscore").toString().equals(""))?"0":list.get(i).get("finalscore").toString());
            res.add(now);
        }
        return res;
    }
    @Override
    public JSONArray QueryInfo(String studentid,String year, String term) {
        JSONArray res = new JSONArray();
        if(year != null && term != null){
            String sql = "SELECT * FROM StudentScore WHERE studentid = ? AND year = ? AND term = ?";
            List<Map<String,Object> >list =  jdbcTemplate.queryForList(sql,new Object[]{studentid,year,term});
            JSONObject date = new JSONObject();
            date.put("year",year);
            date.put("term",term);
            res.add(date);
            res.add(getScore(list));
        }
        else{
            String sql = "SELECT * FROM StudentScore WHERE studentid = ?";
            List<Map<String,Object> >list =  jdbcTemplate.queryForList(sql,new Object[]{studentid});
            JSONObject date = new JSONObject();
            date.put("year","");
            date.put("term","");
            res.add(date);
            res.add(getScore(list));
        }
        return res;
    }
}
