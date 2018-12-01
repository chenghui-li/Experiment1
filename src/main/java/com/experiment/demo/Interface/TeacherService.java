package com.experiment.demo.Interface;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
public interface TeacherService {

    void insertScore(String year, String term, String studentid, String studentname,String designation, String scorep, String scorek, String scoreb, String scoreq,
                String finalscore,String exchange,String delete);//输入成绩

    boolean check(String teacherid, String  password);//判断用户名密码是否正确

    JSONArray queryClassScore(String year, String term, String designation, String classid);

    JSONObject ReturnTeacherInfo(String teacherid);//如果登录成功，返回教师的相关信息

    JSONArray ReturnStudentList(/*String year, String term, */String classid,String designation,String year,String term);//返回某学年学期下某班级选修该课的学生列表

    boolean DeleteTmp(String year,String term,String studentid,String designation);//删除暂存成绩

    JSONArray hasTmpScore(String year,String term,String designation,String classid);//查询已经缓存的成绩

    JSONObject ProduceAnalysis(String year,String term,String designation,String classid);//生成成绩分析表

    boolean Revoke(String year,String term,String designation,String classid);//撤销成绩信息

    String GetCourseTime(String designation);

    JSONObject GetClassId(String studentid);   //获取班级id和所在院系

    JSONArray GetManyInfo(String year,String term,String designation,String classid);

    JSONObject InsertManyInfo(String year,String term,String coursename,String classid,String Anum,String Bnum,String Cnum,String Dnum,String Enum,
                              String Amod,String Bmod,String Cmod,String Dmod,String Emod,String maxnum,String minnum,String studentnum,String scoretype,String department,String teachername,
                              String coursetime,String bzc,String avgnum,String commend1,String commend2,String commend3);

    JSONArray HasBadScore(String year,String term,String designation,String classid);
}
