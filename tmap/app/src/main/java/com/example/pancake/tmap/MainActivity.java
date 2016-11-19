package com.example.pancake.tmap;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.skp.Tmap.TMapView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout rl = new RelativeLayout(this);
        TMapView tmapview = new TMapView(this);

        tmapview.setSKPMapApiKey("8abafa18-e715-38be-b488-cc384c7a73e5");
        tmapview.setCompassMode(true);
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);
        rl.addView(tmapview);
        setContentView(rl);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Thread() {
                    @Override
                    public void run() {
                        try {String startX="128.611238";
                            String startY="35.884982";
                            String endX="128.611971";
                            String endY="35.885180";
                            URL url = new URL("https://apis.skplanetx.com/tmap/routes?version=1&endX="+endX+"&endY="+endY+"&startX="+startX+"&startY="+startY+"&reqCoordType=WGS84GEO&resCoordType=WGS84GEO&tollgateFareOption=1&roadType=32&directionOption=0&endRpFlag=16&endPoiId=67516&gpsTime=10000&angle=90&speed=60&uncetaintyP=3&uncetaintyA=3&uncetaintyAP=12&camOption=0&carType=0");  //Open the connection here, and remember to close it when job its done.
                            URLConnection conn = url.openConnection();
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.setRequestProperty("appKey", "8abafa18-e715-38be-b488-cc384c7a73e5");
                            conn.setDoOutput(true);

                            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                            //theJSONYouWantToSend should be the JSONObject as String
                            wr.write("{test:test}");
                            wr.flush();

                            //  Here you read any answer from server.
                            BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String line;
                            String jsonString = "";
                            while ((line = serverAnswer.readLine()) != null) {
                                jsonString += line;
                                //use it as you need, if server send something back you will get it here.
                            }
                            Log.i("LINE: ", jsonString); //<--If any response from server
                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray jsonArray = jsonObject.getJSONArray("features");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONArray jsonCoordinates = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates");
                                for(int j=0; j<jsonCoordinates.length(); j++) {
                                    if (!jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getString(0).startsWith("[")) {
                                        Log.i("NUMBER", jsonCoordinates.getString(0));
                                        Log.i("NUMBER", jsonCoordinates.getString(1));
                                    }else {
                                        Log.i("NUMBER", jsonCoordinates.getJSONArray(j).getString(0));
                                        Log.i("NUMBER", jsonCoordinates.getJSONArray(j).getString(1));
                                    }
                                }
                            }
                            wr.close();
                            serverAnswer.close();

                        } catch (Exception e) {
                            Log.e("Cuack", e.toString());
                        }
                    }
                }.start();
            }
        });



    }
}