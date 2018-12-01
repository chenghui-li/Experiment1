package com.experiment.demo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.experiment.demo.ServiceImpl.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("teacher")
public class TeacherController {

    @Autowired
    public TeacherServiceImpl teacherService;
    
    //查看成绩
    @PostMapping("/query")
    public JSONArray query(@RequestParam(value = "year") String year,
                           @RequestParam(value = "term") String term,
                           @RequestParam(value = "designation") String designation,
                           @RequestParam(value = "classid") String classid){
        System.out.println("teacherController query OK");
        return teacherService.queryClassScore(year,term,designation,classid);

    }

    //成绩分析
    @PostMapping("/analyze")
    public JSONArray analyze(@RequestParam(value = "year") String year,
                             @RequestParam(value = "term") String term,
                             @RequestParam(value = "designation") String designation,
                             @RequestParam(value = "classid") String classid){
        return  teacherService.GetManyInfo(year,term,designation,classid);
    }
    //录入成绩before
    @PostMapping("/insert")
    public JSONArray insert(@RequestParam(value = "year") String year,
                            @RequestParam(value = "term") String term,
                            @RequestParam(value = "designation") String designation,
                             @RequestParam(value = "classid") String classid){
        JSONObject obj = new JSONObject();
        //先查询是否有暂存成绩，没有则生成空表
        JSONArray jsonArray = new JSONArray();
        if ( teacherService.hasTmpScore(year, term, designation, classid).size() != 0){
            //有暂存成绩
            jsonArray = teacherService.hasTmpScore(year, term, designation, classid);
        }else if(teacherService.HasBadScore(year,term,designation,classid).size() != 0){
            jsonArray = teacherService.HasBadScore(year,term,designation,classid);
        }else{
            //没有暂存成绩，返回空表
            jsonArray = teacherService.ReturnStudentList(classid,designation,year,term);
        }
        System.out.println("teacherController insert OK");
        return jsonArray;
    }
    //暂存成绩
    @PostMapping("/tmp")
    public JSONObject saveTmp(@RequestParam(value = "student") String student){
        JSONObject res = new JSONObject();
        try {
            if (student != null && !student.equals("")) {
                JSONArray jsonArray = JSONArray.parseArray(student);
                int size = jsonArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    String year = jo.getString("year");
                    String term = jo.getString("term");
                    String studentid = jo.getString("studentid");
                    String studentname = jo.getString("studentname");
                    String designation = jo.getString("designation");
                    String scorep = (jo.getString("scorep") == null || jo.getString("scorep").equals(""))?"0":jo.getString("scorep");
                    String scorek = (jo.getString("scorek") == null || jo.getString("scorek").equals(""))?"0":jo.getString("scorek");
                    String scoreb = (jo.getString("scoreb") == null || jo.getString("scoreb").equals(""))?"0":jo.getString("scoreb");
                    String scoreq = (jo.getString("scoreq") == null || jo.getString("scoreq").equals(""))?"0":jo.getString("scoreq");

                    String finalscore =(jo.getString("finalscore") == null || jo.getString("finalscore").equals(""))?"0":jo.getString("finalscore");
                    teacherService.insertScore(year, term, studentid, studentname,designation,
                            scorep, scorek, scoreb, scoreq, finalscore, "1", "0");
                }
                res.put("res","yes");
            }
        }catch (Exception e){
            res.put("res","no");
            throw (e);
        }
        System.out.println("teacherController savetmp OK");


        return res;
    }
    public String []level = {"1不及格","2及格","3中等","4良好","5优秀"};

    double getbzc(List<Integer> list,int size,int agvnum){
        double fx = 0.0;
        for(Integer now : list){
            fx += (now-agvnum)*(now-agvnum);
        }
        fx = fx /size;
        return Math.sqrt(fx);
    }
    //提交录入的成绩
    @PostMapping("/save")
    public JSONObject save(@RequestParam(value = "student") String student,
                           @RequestParam(value = "baifenzhi") String str){
        String coursename,   //课程名称
                coursetime,   //学时
                classid,     //行政班级
                studentnum,  //考试人数
                teachername, //教师姓名
                department;  //所在院系
        boolean levelflag = true;   //是不是等级制
        JSONObject jsonObjecttmp = JSONArray.parseArray(student).getJSONObject(0);
        coursename = jsonObjecttmp.getString("designation");
        //获取教师姓名
        teachername = "";
        coursetime = teacherService.GetCourseTime(coursename);
        classid = teacherService.GetClassId(jsonObjecttmp.getString("studentid")).getString("classid");
        department = teacherService.GetClassId(jsonObjecttmp.getString("studentid")).getString("department");
        String[] pk = new String[2];
        /*
        * <60 E
        * [60,70) D
        * [71,80) C
        * [81,90) B
        * [91,100) A
        * */
        int Anum=0,Bnum=0,Cnum=0,Dnum=0,Enum=0;
        double Amod=0.0,Bmod=0.0,Cmod=0.0,Dmod=0.0,Emod=0.0;
        int maxnum=0,minnum=0,avgnum=0,sumnum = 0;
        List<Integer> list = new LinkedList<Integer>();
        int istudentnum = 0;
        double p ,k;
        p = k = 0.0;
        if(str !=null && !str.equals("")){   //百分制
            levelflag = false;
            pk = str.split("-");
            p = Integer.parseInt(pk[0])*1.0/10;
            k = Integer.parseInt(pk[1])*1.0/10;
        }
        try {
            if (student != null && !student.equals("")) {
                JSONArray jsonArray = JSONArray.parseArray(student);
                int size = jsonArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    String year = jo.getString("year");
                    String term = jo.getString("term");
                    String studentid = jo.getString("studentid");
                    String studentname = jo.getString("studentname");
                    String designation = jo.getString("designation");
                    String scorep = jo.getString("scorep")/*.equals("")?"0":jo.getString("scorep")*/;
                    String scorek = jo.getString("scorek");
                    String scoreb = jo.getString("scoreb")/*.equals("")?"0":jo.getString("scoreb")*/;
                    String scoreq = jo.getString("scoreq")/*.equals("")?"0":jo.getString("scoreq")*/;
                    String finalscore;
                    if(levelflag == true){          //等级制
                        int numk=0,numb=0,numq=0;
                        numk = Arrays.binarySearch(level,jo.getString("scorek"));
                        if(jo.getString("scoreb")!=null && !jo.getString("scoreb").equals(""))
                            numb = Arrays.binarySearch(level,jo.getString("scoreb"));
                        if(jo.getString("scoreq")!=null && !jo.getString("scoreq").equals(""))
                            numq = Arrays.binarySearch(level,jo.getString("scoreq"));
                        numb = numb>1?1:numb;
                        numq = numq>1?1:numq;
                        System.out.println(numb);
                        System.out.println(numk);
                        System.out.println(numq);
                        int ans = Math.max(numk,Math.max(numb,numq));
                        finalscore = level[ans];
                        if(finalscore.equals("1不及格")){
                            Enum++;
                        }else if(finalscore.equals("2及格")){
                            Dnum++;
                        }else if(finalscore.equals("3中等")){
                            Cnum++;
                        }else if(finalscore.equals("4良好")){
                            Bnum++;
                        }else if(finalscore.equals("5优秀")){
                            Anum++;
                        }
                    }else{
                        double f = Integer.parseInt(scorep)*1.0*p+ Integer.parseInt(scorek)*1.0*k;
                        int sb,sq;
                        sb = sq = 0;
                        if(scoreb != null && !scoreb.equals(""))
                            sb = Integer.parseInt(scoreb)>60?60:Integer.parseInt(scoreb);
                        if(scoreq != null && !scoreq.equals(""))
                            sq = Integer.parseInt(scoreq)>60?60:Integer.parseInt(scoreq);
                        int fi = (int)f;
                        int sans = Math.max(fi,Math.max(sb,sq));
                        sumnum += sans;
                        list.add(sans);
                        if(sans>maxnum)
                            maxnum = sans;
                        if(sans<minnum)
                            minnum = sans;
                        if(sans>0 && sans<60){
                            Enum++;
                        }else if(sans>=60 && sans<70){
                            Dnum++;
                        }else if(sans>=70 && sans<80){
                            Cnum++;
                        }else if(sans>=80 && sans<90){
                            Bnum++;
                        }else if(sans>=90 && sans<=100){
                            Anum++;
                        }
                        finalscore = String.valueOf(sans);
                    }
                    if(finalscore != null && !finalscore.equals("0")){
                        istudentnum++;
                    }
                    teacherService.DeleteTmp(year,term,studentid,designation);
                    teacherService.insertScore(year, term, studentid, studentname,designation,
                            scorep, scorek, scoreb, scoreq, finalscore, "0", "0");
                }
            }
        }catch (Exception e){
            throw (e);
        }
        studentnum = String.valueOf(istudentnum);
        Amod = Anum*1.0/istudentnum;
        Bmod = Bnum*1.0/istudentnum;
        Cmod = Cnum*1.0/istudentnum;
        Dmod = Dnum*1.0/istudentnum;
        Emod = Enum*1.0/istudentnum;
        avgnum = sumnum / istudentnum;
        System.out.println("teacherController save OK");
        double bzc = 0.0;
        bzc = getbzc(list,istudentnum,avgnum);
        JSONObject res = new JSONObject();
        res.put("res","yes");
        res.put("coursename",coursename);
        res.put("coursetime",coursetime);
        res.put("classid",classid);
        res.put("studentnum",studentnum);
        res.put("teachername",teachername);
        res.put("department",department);
        res.put("scoretype",levelflag==true?"等级制":"百分制");
        res.put("Anum",Anum);
        res.put("Bnum",Bnum);
        res.put("Cnum",Cnum);
        res.put("Dnum",Dnum);
        res.put("Enum",Enum);
        res.put("Amod",Amod);
        res.put("Bmod",Bmod);
        res.put("Cmod",Cmod);
        res.put("Dmod",Dmod);
        res.put("Emod",Emod);
        res.put("maxnum",maxnum);
        res.put("minnum",minnum);
        res.put("avgnum",avgnum);
        res.put("bzc",bzc);
        return res;
    }

    //提交评价
    @PostMapping("/submit")
    public JSONObject submit(@RequestParam(value = "Anum") String Anum,
                             @RequestParam(value = "Bnum") String Bnum,
                             @RequestParam(value = "Cnum") String Cnum,
                             @RequestParam(value = "Dnum") String Dnum,
                             @RequestParam(value = "Enum") String Enum,
                             @RequestParam(value = "Amod") String Amod,
                             @RequestParam(value = "Bmod") String Bmod,
                             @RequestParam(value = "Cmod") String Cmod,
                             @RequestParam(value = "Dmod") String Dmod,
                             @RequestParam(value = "Emod") String Emod,
                             @RequestParam(value = "maxnum") String maxnum,
                             @RequestParam(value = "minnum") String minnum,
                             @RequestParam(value = "avgnum") String avgnum,
                             @RequestParam(value = "studentnum") String studentnum,
                             @RequestParam(value = "classid") String classid,
                             @RequestParam(value = "coursename") String coursename,
                             @RequestParam(value = "coursetime") String coursetime,
                             @RequestParam(value = "bzc") String bzc,
                             @RequestParam(value = "teachername") String teachername,
                             @RequestParam(value = "scoretype") String scoretype,
                             @RequestParam(value = "commend1") String commend1,
                             @RequestParam(value = "commend2") String commend2,
                             @RequestParam(value = "commend3") String commend3,
                             @RequestParam(value = "year") String year,
                             @RequestParam(value = "term") String term,
                             @RequestParam(value = "department") String department
                             ){
        return teacherService.InsertManyInfo(year,term,coursename,classid,Anum,Bnum,Cnum,Dnum,Enum,
                Amod,Bmod,Cmod,Dmod,Emod,maxnum,minnum,studentnum,scoretype,department,teachername,
                coursetime,bzc,avgnum,commend1,commend2,commend3);

    }

    @PostMapping("/delete")
    public JSONObject delete(@RequestParam(value = "year") String year,
                            @RequestParam(value = "term") String term,
                            @RequestParam(value = "designation") String designation,
                            @RequestParam(value = "classid") String classid){
        JSONObject jsonObject = new JSONObject();
        try{

            if(teacherService.Revoke(year,term,designation,classid) == true){
                jsonObject.put("res","yes");
            }
            else{
                jsonObject.put("res","no");
            }

        }catch (Exception e){
            throw (e);
        }
        return jsonObject;
    }
}
