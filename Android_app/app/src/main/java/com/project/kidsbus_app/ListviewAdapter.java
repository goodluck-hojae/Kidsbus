package com.project.kidsbus_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yongs on 2016-11-01.
 */
public class ListviewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ListViewItem> data;
    private int layout;

    public ListviewAdapter(Context context, int layout, ArrayList<ListViewItem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getcName();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        ListViewItem listviewitem=data.get(position);
        ImageView icon=(ImageView)convertView.findViewById(R.id.imageView);
        icon.setImageResource(listviewitem.getIcon());
        TextView kName=(TextView)convertView.findViewById(R.id.kName);
        kName.setText("이름 : "+listviewitem.getcName());

        TextView kAge=(TextView)convertView.findViewById(R.id.kAge);
        kAge.setText("나이 : "+listviewitem.getAge());

        TextView kSex=(TextView)convertView.findViewById(R.id.kSex);
        kSex.setText("성별 : "+listviewitem.getcGender());
        return convertView;
    }
}
