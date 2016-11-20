package com.project.kidsbus_app;

/**
 * Created by yongs on 2016-11-18.
 */
public class pInfo {

    private String parent_birth_date ; //long
    private String parent_location_id ; // name
    private String parent_phone_number; //lat
    private String parent_name; // id

    public pInfo(String name,String date, String ph,String lid){
        parent_birth_date = date;
        parent_location_id=lid;
        parent_phone_number=ph;
        parent_name=name;
    }
    public void setDate(String date) {
        parent_birth_date = date;
    }
    public void setLid(String lid) {
        parent_location_id=lid;
    }
    public void setPh(String ph) {
        parent_phone_number=ph;
    }
    public void setName(String name) {
        parent_name=name;
    }

    public String getParent_birth_date() {
        return this.parent_birth_date;
    }
    public String getParent_phone_number() {
        return parent_phone_number ;
    }
    public String getParent_location_id() {
        return parent_location_id ;
    }

    public String getParent_name() {
        return this.parent_name;
    }
}
