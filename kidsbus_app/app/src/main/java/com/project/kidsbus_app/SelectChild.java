package com.project.kidsbus_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectChild extends AppCompatActivity {
    private ArrayList<ListViewItem> mList=null;
    private ListView mListView;
    private ListviewAdapter mAdapter;

    Intent intent1;
    cSendThread thread;
    String get_cinfo;
    String pid;
    String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_child);

        Intent intent = getIntent();
        pid = (String)intent.getSerializableExtra("pid");
        intent1 =new Intent(this, Kidsbus.class);

        mListView= (ListView) findViewById(R.id.listView2);
        set_List();
        if(mList.size()==1) {
            cid = mList.get(0).getCid();
            intent1.putExtra("cid", cid);
            intent1.putExtra("name", mList.get(0).getcName());
            startActivity(intent1);
            finish();
        }
    }

    public void set_List(){
        try {
            thread = new cSendThread();
            thread.start();
            thread.join();
            thread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mList = new ArrayList<ListViewItem>();
        mList=JsonManagement.get_cInfo(get_cinfo);
        mAdapter =  new ListviewAdapter(this, R.layout.list, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new CL());
    }
    /**
     * ListView의 item을 클릭했을 때.
     * alert로 클릭된 문자열을 보여준다.
     * @author stargatex
     *
     */
    private class CL implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            cid=mList.get(position).getCid();

            intent1.putExtra("cid",cid);
            intent1.putExtra("name",mList.get(position).getcName());
            startActivity(intent1);
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
}
