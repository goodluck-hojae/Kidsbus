package com.project.kidsbus_app;

/**
 * Created by yongs on 2016-11-20.
 */

public class BusLocation {
    String lat;
    String lon;
    BusLocation(){

    }
    BusLocation(String lat,String lon){
        this.lat=lat;
        this.lon=lon;
    }

    public void setLat(String lat){
        this.lat=lat;
    }
    public void setLon(String lon){
        this.lon=lon;
    }

    public String getLat(){
        return lat;
    }
    public String getLon(){
        return lon;
    }
}
