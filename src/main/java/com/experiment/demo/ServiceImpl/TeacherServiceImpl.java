package com.experiment.demo.ServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.experiment.demo.Interface.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean check(String teacherid, String password) {
        try {
            List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT password FROM Teacher WHERE teacherid = ?",new Object[]{teacherid});
            if(list.size()>0 && list.get(0).get("password").toString().equals(password)){
                return true;
            }

        }catch (Exception e){
            throw (e);
        }
        return false;

    }

    @Override
    public JSONObject ReturnTeacherInfo(String teacherid) {
        String sql = "SELECT teacherid,name FROM Teacher WHERE teacherid = ?";
        Object []args = new Object[]{teacherid};
        List<Map<String ,Object> > list = jdbcTemplate.queryForList(sql,args);
        JSONObject res = new JSONObject();
//        res.put("size",list.size());
        res.put("teacherid",list.get(0).get("teacherid").toString());
        res.put("name",list.get(0).get("name").toString());
        return res;
    }

    @Override
    public void insertScore(String year, String term, String studentid, String studentname,String designation, String scorep, String scorek, String scoreb, String scoreq,
                            String finalscore,String exchange,String delete) {
        String sql = "INSERT INTO StudentScore VALUES(0, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object []args = new Object[]{studentid,studentname,year,term,designation,scorep,scorek,scoreb,scoreq,finalscore,exchange,delete};
        try {
            jdbcTemplate.update(sql,args);
        }catch (Exception e){
            throw (e);
        }

    }

    @Override
    public boolean DeleteTmp(String year, String term, String studentid, String designation) {
        String deletesql = "DELETE FROM StudentScore WHERE year = ? and  term = ? and designation = ? and studentid  = ?";
        Object []args = new Object[]{year,term,designation,studentid};
        try{
            jdbcTemplate.update(deletesql,args);
        }catch (Exception e){
            throw (e);
        }
        return true;
    }

    @Override
    public JSONArray ReturnStudentList(String classid,String designation,String year,String term) {
        String sql = "SELECT studentid,studentname FROM Student WHERE studentid in (SELECT studentid FROM Class WHERE classid = ?)";
        Object []args = new Object[]{classid};
        JSONArray res = new JSONArray();
        List<Map<String ,Object> >list = jdbcTemplate.queryForList(sql,args);
        for(int i = 0;i<list.size();i++){
            System.out.println(list.get(i).toString());
            JSONObject now = new JSONObject();
            now.put("year",year);
            now.put("term",term);
            now.put("designation",designation);
            now.put("studentid",list.get(i).get("studentid").toString());
            now.put("studentname",list.get(i).get("studentname").toString());
            res.add(now);
        }
        return res;
    }

    @Override
    public JSONObject ProduceAnalysis(String year, String term, String designation, String classid) {
        return null;
    }

    @Override
    public JSONObject InsertManyInfo(String year,String term,String coursename,String classid,String Anum,String Bnum,String Cnum,String Dnum,String Enum,
                                     String Amod,String Bmod,String Cmod,String Dmod,String Emod,String maxnum,String minnum,String studentnum,String scoretype,String department,String teachername,
                                     String coursetime,String bzc,String avgnum,String commend1,String commend2,String commend3) {
        String sql = "insert into ScoreAnalyze values(0,?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?,?)";
        Object args[] = new Object[]{year,term,coursename,classid,Anum,Bnum,Cnum,Dnum,Enum,
                Amod,Bmod,Cmod,Dmod,Emod,maxnum,minnum,studentnum,scoretype,department,teachername,
                coursetime,bzc,avgnum,commend1,commend2,commend3};
        try{
            jdbcTemplate.update(sql,args);
        }catch (Exception e){
            throw (e);
        }
        JSONObject res = new JSONObject();
        res.put("res","yes");
        return res;
    }

    @Override
    public JSONArray GetManyInfo(String year, String term, String coursename, String classid) {
        String sql = "select * from ScoreAnalyze where year = ? and term = ? and designation = ? and classid = ?";
        Object []args = new Object[]{year,term,coursename,classid};
        JSONArray jsonArray = new JSONArray();
        try{
            List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,args);
            if(list.size() == 0)
                return jsonArray;
            JSONObject now = new JSONObject();
            now.put("Anum",list.get(0).get("Anum").toString());
            now.put("Bnum",list.get(0).get("Bnum").toString());
            now.put("Cnum",list.get(0).get("Cnum").toString());
            now.put("Dnum",list.get(0).get("Dnum").toString());
            now.put("Enum",list.get(0).get("Enum").toString());
            now.put("Amod",list.get(0).get("Amod").toString());
            now.put("Bmod",list.get(0).get("Bmod").toString());
            now.put("Cmod",list.get(0).get("Cmod").toString());
            now.put("Dmod",list.get(0).get("Dmod").toString());
            now.put("Emod",list.get(0).get("Emod").toString());
            now.put("maxnum",list.get(0).get("maxnum").toString());
            now.put("minnum",list.get(0).get("minnum").toString());
            now.put("avgnum",list.get(0).get("avgnum").toString());
            now.put("coursename",list.get(0).get("designation").toString());
            now.put("teachername",list.get(0).get("teachername").toString());
            now.put("bzc",list.get(0).get("bzc").toString());
            now.put("studentnum",list.get(0).get("studentnum").toString());
            now.put("scoretype",list.get(0).get("scoretype").toString());
            now.put("department",list.get(0).get("department").toString());
            now.put("coursetime",list.get(0).get("coursetime").toString());
            now.put("commend1",list.get(0).get("commend1").toString());
            now.put("commend2",list.get(0).get("commend2").toString());
            now.put("commend3",list.get(0).get("commend3").toString());
            now.put("year",list.get(0).get("year").toString());
            now.put("term",list.get(0).get("term").toString());
            now.put("classid",list.get(0).get("classid").toString());
            jsonArray.add(now);
        }catch (Exception e){
            throw(e);
        }

        return jsonArray;
    }

    @Override
    public boolean Revoke(String year, String term, String designation, String classid) {
        String checksql = "SELECT * FROM StudentScore WHERE year = ? and term = ? and designation = ? and exdelete = '1' and studentid in (SELECT studentid FROM Class WHERE classid = ?)";
        String deletesql = "DELETE FROM StudentScore WHERE year = ? and  term = ? and designation = ? and studentid in (SELECT studentid FROM Class WHERE classid = ?)";
        Object []args = new Object[]{year,term,deletesql,classid};
        try {
            List<Map<String,Object>> list = jdbcTemplate.queryForList(checksql,args);
            if(list.size() == 0)
                return false;
            else{
                jdbcTemplate.update(deletesql,args);
                return true;
            }
        }catch (Exception e){
            throw (e);
        }
    }

    @Override
    public JSONArray hasTmpScore(String year, String term, String designation, String classid) {
        String sql = "SELECT studentid,studentname,designation,scorep,scorek,scoreb,scoreq FROM StudentScore WHERE year = ? and term = ? and designation = ? and exchange = '1' and studentid in (SELECT studentid FROM Class WHERE classid = ?)";
        Object []args = new Object[]{year,term,designation,classid};
        JSONArray res = new JSONArray();
        try {
            List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,args);
            for(int i = 0;i<list.size();i++){
                JSONObject now = new JSONObject();
                now.put("year",year);
                now.put("term",term);
                now.put("studentid",list.get(i).get("studentid").toString());
                now.put("studentname",list.get(i).get("studentname").toString());
                now.put("designation",list.get(i).get("designation").toString());
                now.put("scorep",(list.get(i).get("scorep") == null || list.get(i).get("scorep").toString().equals(""))?"0":list.get(i).get("scorep").toString());
                now.put("scorek",list.get(i).get("scorek").toString());
                now.put("scoreb",(list.get(i).get("scoreb") == null || list.get(i).get("scoreb").toString().equals(""))?"0":list.get(i).get("scoreb").toString());
                now.put("scoreq",(list.get(i).get("scoreq") == null || list.get(i).get("scoreq").toString().equals(""))?"0":list.get(i).get("scoreq").toString());
                res.add(now);
            }
        }catch (Exception e){
            throw (e);
        }
        return res;
    }

    @Override
    public JSONArray queryClassScore(String year, String term, String designation, String classid) {
        String sql = "SELECT studentid,studentname,designation,scorep,scorek,scoreb,scoreq,finalscore from StudentScore WHERE year = ? and term = ? and designation = ? and studentid in (SELECT studentid FROM Class WHERE classid = ?)";
        Object []args = new Object[]{year,term,designation,classid};
        JSONArray res = new JSONArray();
        try {
            List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,args);
            for(int i = 0;i<list.size();i++){
                System.out.println(list.get(i).toString());
                JSONObject now = new JSONObject();

                now.put("studentid",list.get(i).get("studentid").toString());
                now.put("studentname",list.get(i).get("studentname").toString());
                now.put("designation",list.get(i).get("designation").toString());
                now.put("scorep",(list.get(i).get("scorep") == null || list.get(i).get("scorep").toString().equals(""))?"0":list.get(i).get("scorep").toString());
                now.put("scorek",list.get(i).get("scorek").toString());
                now.put("scoreb",(list.get(i).get("scoreb") == null || list.get(i).get("scoreb").toString().equals(""))?"0":list.get(i).get("scoreb").toString());
                now.put("scoreq",(list.get(i).get("scoreq") == null || list.get(i).get("scoreq").toString().equals(""))?"0":list.get(i).get("scoreq").toString());
                now.put("finalscore",(list.get(i).get("finalscore") == null || list.get(i).get("finalscore").toString().equals(""))?"0":list.get(i).get("finalscore").toString());
                res.add(now);
            }
        }catch (Exception e){
            throw (e);
        }
        return res;
    }

    @Override
    public JSONArray HasBadScore(String year, String term, String designation, String classid) {
        String sql = "select * from StudentScore where year = ? and term = ? and designation = ? and studentid in (SELECT studentid FROM Class WHERE classid = ?)";
        Object []args = new Object[]{year,term,designation,classid};
        JSONArray jsonArray = new JSONArray();
        try{
            List<Map<String ,Object>> list = jdbcTemplate.queryForList(sql,args);
            for(int i = 0;i<list.size();i++){
                String finalscore = list.get(i).get("finalscore").toString();
                boolean isdigit = true;
                for(int j = 0;j<finalscore.length();j++){
                    if(!Character.isDigit(finalscore.charAt(j))){
                        isdigit = false;
                        break;
                    }
                }
                if(finalscore.equals("1不及格") || (isdigit == true && Integer.parseInt(finalscore)<60)){
                    JSONObject now = new JSONObject();
                    now.put("year",year);
                    now.put("term",term);
                    now.put("designation",designation);
                    now.put("studentid",list.get(i).get("studentid").toString());
                    now.put("studentname",list.get(i).get("studentname").toString());
                    now.put("scorep",(list.get(i).get("scorep") == null || list.get(i).get("scorep").toString().equals(""))?"0":list.get(i).get("scorep").toString());
                    now.put("scorek",list.get(i).get("scorek").toString());
                    jsonArray.add(now);
                }
            }
        }catch (Exception e){
            throw (e);
        }
        return jsonArray;
    }

    @Override
    public String GetCourseTime(String designation) {
        String sql = "select time from Course where designation = ?";
        Object []args = new Object[]{designation};
        String res;
        try{
            List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,args);
            res = list.get(0).get("time").toString();
        }catch (Exception e){
            throw (e);
        }
        return res;
    }

    @Override
    public JSONObject GetClassId(String studentid) {
        String sql = "select classid,department from Class where studentid = ?";
        JSONObject res = new JSONObject();
        try {
            List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,new Object[]{studentid});
            res.put("classid",list.get(0).get("classid").toString());
            res.put("department",list.get(0).get("department").toString());
        }catch (Exception e){
            throw (e);
        }

        return res;
    }
}
