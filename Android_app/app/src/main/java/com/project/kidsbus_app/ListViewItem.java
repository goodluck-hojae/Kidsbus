package com.project.kidsbus_app;

import java.util.Calendar;

/**
 * Created by yongs on 2016-11-01.
 */
public class ListViewItem {
    private int iconDrawable ;
    private String cName ;
    private String cDate ;
    private String cGender ;
    private String age;
    private String cid;

    public ListViewItem(int icon, String name,String date,String gender,String id){
        iconDrawable=icon;
        cName=name;
        cDate=date;
        cGender=gender;
        cid=id;
        age=getAge(date);
    }
    public void setIcon(int icon) {
        iconDrawable = icon ;
    }
    public void setcName(String n) {
        cName = n ;
    }
    public void setcDate(String d) {
        cDate = d ;
    }
    public void setcGender(String g) {cGender= g ;  }
    public void setCid(String id){cid=id;}

    public int getIcon() {
        return this.iconDrawable ;
    }
    public String getcName() {
        return cName;
    }
    public String getAge() {
        return age;
    }
    public String getcGender() {
        if(cGender.equals("M"))
            return "남자";
        else
            return "여자";
    }
    public String getCid(){return cid;}

    public String getAge(String age){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int iage=Integer.parseInt(age);
        iage =year-(iage/10000)+1;
        String strResult = Integer.toString(iage);
        return strResult;
    }
}
