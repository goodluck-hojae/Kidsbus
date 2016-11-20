package com.project.kidsbus_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;

public class Kidsbus extends AppCompatActivity {
    Calendar[] c = new Calendar[42];
    TextView[] day=new TextView[42];
    Button[] tb = new Button[42];

    ArrayList<Day> inputday=new ArrayList<>();
    int month;
    PostThread thread0;
//    SharedPreferences setting;
//    SharedPreferences.Editor editor;


    String cid;
    String name;
    TextView sName;
    Button ccc;
    String check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kidsbus);

        sName=(TextView)findViewById(R.id.textView20);

        initializeCalendar();
        initializeButton();
        Intent intent = getIntent();
        cid = (String)intent.getSerializableExtra("cid");
        name = (String)intent.getSerializableExtra("name");

        setCalender();

        sName.setText(" "+name+ " 어린이 탑승 여부 \n"+month+" 월 "+"달력");
    }

    private void setCalender() {

    }

    public void initializeButton() {
        tb[0] = (Button) findViewById(R.id.Button0);
        tb[1] = (Button) findViewById(R.id.Button1);
        tb[2] = (Button) findViewById(R.id.Button2);
        tb[3] = (Button) findViewById(R.id.Button3);
        tb[4] = (Button) findViewById(R.id.Button4);
        tb[5] = (Button) findViewById(R.id.Button5);
        tb[6] = (Button) findViewById(R.id.Button6);
        tb[7] = (Button) findViewById(R.id.Button7);
        tb[8] = (Button) findViewById(R.id.Button8);
        tb[9] = (Button) findViewById(R.id.Button9);
        tb[10] = (Button) findViewById(R.id.Button10);
        tb[11] = (Button) findViewById(R.id.Button11);
        tb[12] = (Button) findViewById(R.id.Button12);
        tb[13] = (Button) findViewById(R.id.Button13);
        tb[14] = (Button) findViewById(R.id.Button14);
        tb[15] = (Button) findViewById(R.id.Button15);
        tb[16] = (Button) findViewById(R.id.Button16);
        tb[17] = (Button) findViewById(R.id.Button17);
        tb[18] = (Button) findViewById(R.id.Button18);
        tb[19] = (Button) findViewById(R.id.Button19);
        tb[20] = (Button) findViewById(R.id.Button20);
        tb[21] = (Button) findViewById(R.id.Button21);
        tb[22] = (Button) findViewById(R.id.Button22);
        tb[23] = (Button) findViewById(R.id.Button23);
        tb[24] = (Button) findViewById(R.id.Button24);
        tb[25] = (Button) findViewById(R.id.Button25);
        tb[26] = (Button) findViewById(R.id.Button26);
        tb[27] = (Button) findViewById(R.id.Button27);
        tb[28] = (Button) findViewById(R.id.Button28);
        tb[29] = (Button) findViewById(R.id.Button29);
        tb[30] = (Button) findViewById(R.id.Button30);
        tb[31] = (Button) findViewById(R.id.Button31);
        tb[32] = (Button) findViewById(R.id.Button32);
        tb[33] = (Button) findViewById(R.id.Button33);
        tb[34] = (Button) findViewById(R.id.Button34);
        tb[35] = (Button) findViewById(R.id.Button35);
        tb[36] = (Button) findViewById(R.id.Button36);
        tb[37] = (Button) findViewById(R.id.Button37);
        tb[38] = (Button) findViewById(R.id.Button38);
        tb[39] = (Button) findViewById(R.id.Button39);
        tb[40] = (Button) findViewById(R.id.Button40);
        tb[41] = (Button) findViewById(R.id.Button41);

        day[0] = (TextView) findViewById(R.id.tday0);
        day[1] = (TextView) findViewById(R.id.tday1);
        day[2] = (TextView) findViewById(R.id.tday2);
        day[3] = (TextView) findViewById(R.id.tday3);
        day[4] = (TextView) findViewById(R.id.tday4);
        day[5] = (TextView) findViewById(R.id.tday5);
        day[6] = (TextView) findViewById(R.id.tday6);
        day[7] = (TextView) findViewById(R.id.tday7);
        day[8] = (TextView) findViewById(R.id.tday8);
        day[9] = (TextView) findViewById(R.id.tday9);
        day[10] = (TextView) findViewById(R.id.tday10);
        day[11] = (TextView) findViewById(R.id.tday11);
        day[12] = (TextView) findViewById(R.id.tday12);
        day[13] = (TextView) findViewById(R.id.tday13);
        day[14] = (TextView) findViewById(R.id.tday14);
        day[15] = (TextView) findViewById(R.id.tday15);
        day[16] = (TextView) findViewById(R.id.tday16);
        day[17] = (TextView) findViewById(R.id.tday17);
        day[18] = (TextView) findViewById(R.id.tday18);
        day[19] = (TextView) findViewById(R.id.tday19);
        day[20] = (TextView) findViewById(R.id.tday20);
        day[21] = (TextView) findViewById(R.id.tday21);
        day[22] = (TextView) findViewById(R.id.tday22);
        day[23] = (TextView) findViewById(R.id.tday23);
        day[24] = (TextView) findViewById(R.id.tday24);
        day[25] = (TextView) findViewById(R.id.tday25);
        day[26] = (TextView) findViewById(R.id.tday26);
        day[27] = (TextView) findViewById(R.id.tday27);
        day[28] = (TextView) findViewById(R.id.tday28);
        day[29] = (TextView) findViewById(R.id.tday29);
        day[30] = (TextView) findViewById(R.id.tday30);
        day[31] = (TextView) findViewById(R.id.tday31);
        day[32] = (TextView) findViewById(R.id.tday32);
        day[33] = (TextView) findViewById(R.id.tday33);
        day[34] = (TextView) findViewById(R.id.tday34);
        day[35] = (TextView) findViewById(R.id.tday35);
        day[36] = (TextView) findViewById(R.id.tday36);
        day[37] = (TextView) findViewById(R.id.tday37);
        day[38] = (TextView) findViewById(R.id.tday38);
        day[39] = (TextView) findViewById(R.id.tday39);
        day[40] = (TextView) findViewById(R.id.tday40);
        day[41] = (TextView) findViewById(R.id.tday41);
//-------------------------------------------------------
        // 모든 Button을 on으로 처리함.
//        for(int i=0; i<tb.length; i++)
//            tb[i].setText("On");
        //toggle button에 on/off되었을때 버튼에 표현되어질 텍스트를 초기화함
        for (int i = 0; i < tb.length; i++) {
            CharSequence cs = c[i].get(Calendar.DATE)  + "";
            day[i].setText(cs);
            if(i%7==0)
                day[i].setTextColor(Color.parseColor("#FF0000"));
            if(i%7==6)
                day[i].setTextColor(Color.parseColor("#0000FF"));
            tb[i].setText("O");
        }
//----------------------------------------------------------
    }

    public void onClickButton(View v){
            int id=v.getId();
            int position=0;

             ccc = (Button) findViewById(id);
        for (int i = 0; i < tb.length; i++) {
            if(ccc==tb[i]){
                position=i;
                break;}
        }

            check=ccc.getText().toString();
        if(check.equals("O")){
                ccc.setText("X");
//
//            editor.commit();
//            editor.putString("ID", id);
//            editor.putString("PW", pw);
//            editor.putBoolean("checkBox", true);
//            editor.commit();
//            try {
//                thread1 = new GetThread();
//                thread1.start();
//                thread1.join();
//                thread1.interrupt();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
                ccc.setTextColor(Color.parseColor("#FF0000"));
        }
        else {
                ccc.setText("O");
//                inputday.get(position).setAttend("T");

//        try {
//                thread1 = new GetThread();
//                thread1.start();
//                thread1.join();
//                thread1.interrupt();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
                ccc.setTextColor(Color.parseColor("#000000"));
            }

        Toast.makeText(getApplicationContext(), "year :" + c[position].get(Calendar.YEAR)
                + "\nmonth :" + (c[position].get(Calendar.MONTH)+1) +"\nday :"
                + c[position].get(Calendar.DATE), Toast.LENGTH_SHORT).show();
    }

    public void initializeCalendar() {
        for(int i=0; i<c.length; i++)
            c[i]=Calendar.getInstance();

        //Calendar클래스 이용
        //29 30 1 2 3 4 5 ...
        //28 29 30 1 2 3.. 표현되게 해줌
        Calendar today = Calendar.getInstance();
        Calendar sDay = Calendar.getInstance();
        Calendar eDay = Calendar.getInstance();

        int n;
        int day=0, month1=0;
        int year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH)+1; //+1을 하면 11월이 표시됨 +2는 12월.
        sDay.set(year, month - 1, 1);
        eDay.set(year, month - 1, sDay.getActualMaximum(Calendar.DATE));
        sDay.add(Calendar.DATE, -sDay.get(Calendar.DAY_OF_WEEK) + 1);
        eDay.add(Calendar.DATE, 7 - eDay.get(Calendar.DAY_OF_WEEK));

        // 2016년 10월처럼 달력을 표현하는데 6줄이 필요한 경우,
        // 밑의 반복문은 9월의 나머지와 10월, 11월의 나머지를 표현해줌.
        // 그러나 2016년 9월처럼 5줄이 표현할때는 5줄만 표현되고 나머지 1줄은 표현되지 않음.
        for (n = 1; sDay.before(eDay) || sDay.equals(eDay); sDay.add(Calendar.DATE, 1)) {
            day = sDay.get(Calendar.DATE);
            month1 = sDay.get(Calendar.MONTH);
            c[n-1].set(year, month1, day);
            n++;
        }

        // 그래서 나머지 한줄을 표현하기 위해 밑의 반복문 실행함
        if(n!=43)
            for(int i=n-1; i<c.length; i++, day++) {
                c[i].set(year, month1, day);
                Day a=new Day(year,month1,day,"T");
                inputday.add(a);
            }
    }

    private class PostThread extends Thread{
        public void run()
        {
            try {
                NetworkManagement.del_child(cid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}