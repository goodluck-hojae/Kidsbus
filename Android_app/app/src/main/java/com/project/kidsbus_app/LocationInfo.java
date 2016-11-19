package com.project.kidsbus_app;

/**
 * Created by yongs on 2016-11-19.
 */

public class LocationInfo {

    private String id;
    private String name;
    private String lon;
    private String lat;

    public LocationInfo(String id,String name, String lon,String lat){
        this.id=id;
        this.name=name;
        this.lon=lon;
        this.lat=lat;
    }
    public void setId(String id)      {  this.id=id;    }
    public void setName(String name) {
        this.name=name;
    }
    public void setLon(String lon) {
        this.lon=lon;
    }
    public void setLat(String lat) {
        this.lat=lat;
    }

    public String getId() {        return this.id;    }
    public String getName() {
        return this.name;
    }
    public String getLon() {return this.lon; }
    public String getLat() {
        return this.lat;
    }

}
