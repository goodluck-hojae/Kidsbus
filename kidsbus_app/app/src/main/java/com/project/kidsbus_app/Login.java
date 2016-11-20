package com.project.kidsbus_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText idInput, passwordInput;
    Boolean loginChecked;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    CheckBox Auto_Login;
    Button btn_login;
    String id,pw;

    SendThread thread1;
    cLogin thread0;

    String pid;
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        Auto_Login = (CheckBox) findViewById(R.id.checkBox);
        btn_login = (Button) findViewById(R.id.loginButton);

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        if(setting.getBoolean("checkBox", false)){
            idInput.setText(setting.getString("ID", ""));
            passwordInput.setText(setting.getString("PW", ""));
            loginChecked=true;
            Auto_Login.setChecked(loginChecked);
        }
    }

    public void onClickButton(View v){
        int btn=v.getId();
        Intent intent;

        id=idInput.getText().toString();
        pw=passwordInput.getText().toString();

          switch (btn) {
              case R.id.loginButton:
                  try  {
                      thread0 = new cLogin();
                      thread0.start();
                      thread0.join();
                      thread0.interrupt();
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }

                  if(id.equals("admin")) {
                      intent = new Intent(this, aMainActivity.class);
                      startActivity(intent);
                      finish();
                  } else {
                      if (c == 200) {  //id pw 체크
                          if (Auto_Login.isChecked()) {
                              Toast.makeText(this, "로그인", Toast.LENGTH_SHORT).show();
                              loginChecked = true;
                              editor.putString("ID", id);
                              editor.putString("PW", pw);
                              editor.putBoolean("checkBox", true);
                              editor.commit();

                              try {
                                  thread1 = new SendThread();
                                  thread1.start();
                                  thread1.join();
                                  thread1.interrupt();
                              } catch (InterruptedException e) {
                                  e.printStackTrace();
                              }
                              pid = JsonManagement.get_pid(pid);
                              intent = new Intent(this, MainActivity.class);
                              intent.putExtra("pid", pid);
                              startActivity(intent);
                              finish();
                          } else {
                              loginChecked = false;
                              editor.putBoolean("checkBox", false);
                              editor.clear();
                              editor.commit();
                              try {
                                  thread1 = new SendThread();
                                  thread1.start();
                                  thread1.join();
                                  thread1.interrupt();
                              } catch (InterruptedException e) {
                                  e.printStackTrace();
                              }
                              pid = JsonManagement.get_pid(pid);
                              intent = new Intent(this, MainActivity.class);
                              intent.putExtra("pid", pid);
                              startActivity(intent);
                              finish();
                          }
                      } else if (c == 401) {
                          Toast.makeText(this, "로그인값 오류", Toast.LENGTH_SHORT).show();
                      } else {
                          Toast.makeText(this, "서버 오류", Toast.LENGTH_SHORT).show();
                      }
                  }
                  break;

                case R.id.signupButton:
                intent = new Intent(this, Membership.class);
                startActivity(intent);
                break;
        }
    }

    private class cLogin extends Thread{
        public void run()
        {
            try {
                c=NetworkManagement.check(id,pw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private class SendThread extends Thread{
        public void run()
        {
            try {
                pid=NetworkManagement.get("login/"+id+"/"+pw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }


}
