package com.project.kidsbus_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by yongs on 2016-11-18.
 */
public class JsonManagement {

    public JsonManagement(){

    }

    public static String get_pid(String str){
        StringBuffer sb = new StringBuffer();
        str="["+str+"]";
        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String pid= jObject.getString("parent_id");
                sb.append(pid);
            }
            return sb.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    } // end doJSONParser()

    public static ArrayList<LocationInfo> get_loction(String str){

       ArrayList<LocationInfo> mList = new ArrayList<LocationInfo>();
        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String  location_name= jObject.getString("location_name");
                String location_id= jObject.getString("location_id");
                String  location_longitude= jObject.getString("location_longitude");
                String location_latitude= jObject.getString("location_latitude");
                String eName= URLDecoder.decode(location_name);
                LocationInfo a=new LocationInfo(location_id,eName,location_longitude,location_latitude);
                mList.add(a);
            }
            return mList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    } // end doJSONParser()

    public static pInfo get_pInfo(String str){

        pInfo a = null;
        str="["+str+"]";
        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String parent_birth_date= jObject.getString("parent_birth_date");
                String  parent_location_id= jObject.getString("parent_location_id");
                String parent_phone_number= jObject.getString("parent_phone_number");
                String  parent_name= jObject.getString("parent_name");
                String eName= URLDecoder.decode(parent_name);
                a=new pInfo(eName,parent_birth_date,parent_phone_number,parent_location_id);
            }
            return a;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    } // end doJSONParser()


    public static ArrayList<ListViewItem> get_cInfo(String str){

        ArrayList<ListViewItem> mlist=new ArrayList<ListViewItem>();
        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String child_name= jObject.getString("child_name");
                String child_gender= jObject.getString("child_gender");
                String child_id= jObject.getString("child_id");
                String child_birth_date= jObject.getString("child_birth_date");
                String eName= URLDecoder.decode(child_name);
                ListViewItem a=new ListViewItem(R.drawable.children,eName,child_birth_date,child_gender,child_id);
                mlist.add(a);
            }
            return mlist;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    } // end doJSONParser()
}
