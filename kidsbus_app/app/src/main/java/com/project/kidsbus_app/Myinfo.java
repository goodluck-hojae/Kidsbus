package com.project.kidsbus_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class Myinfo extends AppCompatActivity {

    private ArrayList<ListViewItem> mList=null;
    private ListView mListView;
    private ListviewAdapter mAdapter;

    TextView pn;
    TextView pp;
    TextView pb;
    TextView pl;

    pSendThread thread1;
    cSendThread thread2;
    dSendThread thread3;
    String pid;
    String cid;
    String get_pinfo;
    String get_cinfo;
    pInfo p=null;

    String strResult = "";
    int age;
//    http://155.230.118.252:5001/kidsbus/get_parent_info_by_id/1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        Intent intent = getIntent();
        pid = (String)intent.getSerializableExtra("pid");
        Log.i("test",pid);
        pn=(TextView)findViewById(R.id.pname);
        pb=(TextView)findViewById(R.id.pbirth);
        pp=(TextView)findViewById(R.id.pph);
        pl=(TextView)findViewById(R.id.plocation);
        mListView= (ListView) findViewById(R.id.listView);

        try {
            thread1 = new pSendThread();
            thread1.start();
            thread1.join();
            thread1.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        p=JsonManagement.get_pInfo(get_pinfo);

        pn.setText("이름     :   "+p.getParent_name());

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        age=Integer.parseInt(p.getParent_birth_date());
        age =year-(age/10000)+1;
        strResult = Integer.toString(age);

        pb.setText("나이     :   "+strResult+" 세");
        pp.setText("연락처 : "+p.getParent_phone_number());
        pl.setText("정류장 : "+p.getParent_location_id());
        set_List();
    }

    public void set_List(){
        try {
            thread2 = new cSendThread();
            thread2.start();
            thread2.join();
            thread2.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mList = new ArrayList<ListViewItem>();
        mList=JsonManagement.get_cInfo(get_cinfo);
        mAdapter =  new ListviewAdapter(this, R.layout.list, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener( new ListViewItemLongClickListener() );
    }

    public void onClickButton(View v) {
        int btn = v.getId();
        Intent intent;
        switch (btn){
            case R.id.toChildadd:
                intent = new Intent(this, Childadd.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
                break;
        }
    }

    private class pSendThread extends Thread{
        public void run()
        {
            try {
                get_pinfo=NetworkManagement.get("get_parent_info_by_id/"+pid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class cSendThread extends Thread{
        public void run()
        {
            try {
                get_cinfo=NetworkManagement.get("get_children_info_by_parent_id/"+pid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onResume()
    {
        super.onResume();
        set_List();
        Log.i("test","onResume");
    }
//     Long click된 item의 index(position)을 기록한다.
    int selectedPos = -1;

    /**
     * ListView의 item을 길게 클릭했을 경우.
     * 클릭된 item을 삭제한다.
     * @author stargatex
     *
     */

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle(R.string.alert_title_question);

            // '예' 버튼이 클릭되면
            alertDlg.setPositiveButton( R.string.button_yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which )
                {
                    cid=mList.get(selectedPos).getCid();
                    try {
                        thread3 = new dSendThread();
                        thread3.start();
                        thread3.join();
                        thread3.interrupt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mList.remove(selectedPos);
                    // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                    mAdapter.notifyDataSetChanged();
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });

            // '아니오' 버튼이 클릭되면
            alertDlg.setNegativeButton( R.string.button_no, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });

            alertDlg.setMessage( "정말 삭제하시겠습니까?" );
            alertDlg.setIcon(R.drawable.ic_kids);
            alertDlg.show();
            return false;
        }
    }

    private class dSendThread extends Thread{
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
