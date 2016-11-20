package com.project.kidsbus_app;

import android.content.Context;
import android.widget.Button;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.StringTokenizer;

public class Day  {
    String y,m,d;
    String a;

    public Day(int year, int month, int day,String attend) {
        y=String.valueOf(year);
        m=String.valueOf(month);
        d=String.valueOf(day);
        a=attend;
    }

    public Day(String date,String attend){

        changeDate(date);
        a=attend;
    }

    public void changeDate(String date){
        StringTokenizer tokens = new StringTokenizer(date);
        y  = tokens.nextToken("/") ;   // 년
        m   = tokens.nextToken("/") ;   // 월
        d   = tokens.nextToken("/") ;   // 일
    }

    public void setAttend(String attend){
        a=attend;
    }

    public String getYear(){
        return y;
    }
    public String getMonth(){
        return m;
    }
    public String getDay(){
        return d;
    }

    public String getToday(){
        return y+"/"+m+"/"+d;
    }
}