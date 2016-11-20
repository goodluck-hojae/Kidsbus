package com.project.kidsbus_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class Childadd extends AppCompatActivity { //
    RadioButton male;
    RadioButton female;
    PostThread thread;
    EditText name;
    EditText date;
    String gender="M";
    String pid;

    String gName;
    String gDate;
    int kSex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childadd);

        name =  (EditText)findViewById(R.id.editName);
        date =  (EditText)findViewById(R.id.editBirth);
        date.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        Intent intent = getIntent();
        pid = (String)intent.getSerializableExtra("pid");


        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        male.setChecked(true);
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String result;
                if(checkedId == R.id.male){
                    gender="M";
                }else{
                    gender="W";
                }
            }
        });
    }

    public void onClickButton(View v) {
        int btn = v.getId();
        Intent intent;

        switch (btn){
            case R.id.cancel:
                finish();
                break;
            case R.id.complete:
                gName=name.getText().toString();
                gDate=date.getText().toString();
                try {
                    thread = new PostThread();
                    thread.start();
                    thread.join();
                    thread.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                Toast.makeText(this, gName+ " 어린이가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class PostThread extends Thread{

        public void run()
        {
            try {
                //String addr, String name,String gender,String date,String pid
                NetworkManagement.post_child("",gName,gender,gDate,pid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
