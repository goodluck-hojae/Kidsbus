package com.project.kidsbus_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Membership extends AppCompatActivity {
    EditText name;
    EditText pw;
    EditText phone;
    EditText date;
    EditText id;
    EditText cpw;
    PostThread thread;
    GetThread thread1;
    String gID;
    String gName;
    String gDate;
    String gPhone;
    String gPw;
    String gCpw;
    String gL;
    ArrayList<String> Location;
    ArrayList<LocationInfo> l;
    Spinner spin;
    int LocationPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        id=(EditText) findViewById(R.id.editId);
        name =  (EditText)findViewById(R.id.editName);
        date = (EditText) findViewById(R.id.editBirth);
        date.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        pw =  (EditText)findViewById(R.id.editpw);
        cpw=(EditText)findViewById(R.id.editpwCheck) ;
        phone =  (EditText)findViewById(R.id.editPhone);
        spin = (Spinner)findViewById(R.id. spinner );

        l=new ArrayList<LocationInfo>();

        try {
            thread1 = new GetThread();
            thread1.start();
            thread1.join();
            thread1.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        l=JsonManagement.get_loction(gL);
        Location= new ArrayList<String>();
        for(int i=0; i<l.size(); i++){
            Location.add(l.get(i).getName());
        }

        ArrayAdapter<String> adapName = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,Location);
        spin.setAdapter(adapName);
        spin.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
            LocationPos=pos;
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
    public void onClickButton(View v) {
        int btn = v.getId();
        Intent intent;

        switch (btn){
            case R.id.cancel:
                finish();
                break;
            case R.id.complete:
                gID=id.getText().toString();
                gName=name.getText().toString();
                gDate=date.getText().toString();
                gPhone=phone.getText().toString();
                gPw=pw.getText().toString();
                gCpw=cpw.getText().toString();

                boolean c=check_in();
                if(c) {
                    try {
                        thread = new PostThread();
                        thread.start();
                        thread.join();
                        thread.interrupt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, gID + " 님 회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                }else {
                    Toast.makeText(this, "입력정보 확인", Toast.LENGTH_SHORT).show();
                    break;
                }

        }
    }

    private class GetThread extends Thread{ //모든 정류장 정보를 받아옴
        public void run()
        {
            try {
                gL=NetworkManagement.get("get_all_location_info");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class PostThread extends Thread{ //회원가입신청

        public void run()
        {
            try {
                Log.i("",""+gName+gID+gPw+gDate+gPhone+"1");
                //String name,String id,String pw,String date,String phNumber,String location_id
                    NetworkManagement.post_parent("",gName,gID,gPw,gDate,gPhone,Integer.toString(LocationPos));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean check_in(){ //회원가입 입력값 확인
        if(gID.length()>0 && gPw.length()>0 && gPhone.length()==11 && gPw.equals(gCpw) && gDate.length()==8 && gName.length()>0)
            return true;
        else
            return false;
    }
}
