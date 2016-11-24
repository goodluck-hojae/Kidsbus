package com.project.kidsbus_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGPoint;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;
import com.skp.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends NMapActivity implements OnMapStateChangeListener, NMapOverlayManager.OnCalloutOverlayListener {

    returnLocationAsync asyncTask = new returnLocationAsync();
    java.util.List<java.util.Map.Entry<String, String>> startAndEndPoints = new java.util.ArrayList<>();

    BusLocation busLocation;
    String get_pinfo;// 저장
    String gL;
    pInfo p; // 부모정보 저장
    ArrayList<LocationInfo> l;
    pSendThread thread1; // 부모정보 Thread
    GetThread thread0;
    String pLocation;
    GetBusLocationThread busThread;

    private AppCompatDelegate mDelegate;
    TMapView tmapview  ;
    java.util.List<java.util.Map.Entry<String,String>> pairList= new java.util.ArrayList<>();
    String pName, pAge, pNumber;

    TextView gtest;
    String pid;

    // API-KEY
    public static final String API_KEY = "adePfbPVnbdl5wzgDoPG";  //<---맨위에서 발급받은 본인 ClientID 넣으세요.
    // 네이버 맵 객체
    NMapView mMapView = null;
    // 맵 컨트롤러
    NMapController mMapController = null;
    // 맵을 추가할 레이아웃
    LinearLayout MapContainer;

    NMapViewerResourceProvider mMapViewerResourceProvider = null;
    NMapOverlayManager mOverlayManager;
    NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = null;
    private NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener;

    private NMapMyLocationOverlay mMyLocationOverlay;
    private NMapLocationManager mMapLocationManager;
    private NMapCompassManager mMapCompassManager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    Activity act;
    NMapPathData pathData;
    NMapPOIdataOverlay poiDataOverlay;
    static TextView mSecond;
    static TextView mDis;
    static int totalTime=0;
    static int totalDistance=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        act = this;
        gtest = (TextView) findViewById(R.id.get_test);
        Intent intent = getIntent();
        pid = (String) intent.getSerializableExtra("pid");
        gtest.setText("안전운행하세요.\nCopyright 2016ⓒ Team 키즈버스");

        try {
            thread1 = new pSendThread();
            thread1.start();
            thread1.join();
            thread1.interrupt();
            busThread = new GetBusLocationThread();
            busThread.start();
            busThread.join();
            busThread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        p = JsonManagement.get_pInfo(get_pinfo);
        pLocation = p.getParent_location_id();

        l = new ArrayList<LocationInfo>();

        try {
            thread0 = new GetThread();
            thread0.start();
            thread0.join();
            thread0.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        l = JsonManagement.get_loction(gL);


        // 네이버 지도를 넣기 위한 LinearLayout 컴포넌트
        MapContainer = (LinearLayout) findViewById(R.id.MapContainer);
        // 네이버 지도 객체 생성

        mMapView = new NMapView(this);

        // 지도 객체로부터 컨트롤러 추출
        mMapController = mMapView.getMapController();

        // 네이버 지도 객체에 APIKEY 지정
        mMapView.setApiKey(API_KEY);

        // 생성된 네이버 지도 객체를 LinearLayout에 추가시킨다.
        MapContainer.addView(mMapView);
        // 지도를 터치할 수 있도록 옵션 활성화
        mMapView.setClickable(true);

        // 확대/축소를 위한 줌 컨트롤러 표시 옵션 활성화
        mMapView.setBuiltInZoomControls(true, null);

        // 지도에 대한 상태 변경 이벤트 연결
        mMapView.setOnMapStateChangeListener(this);

        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);

        final int markerBUS = NMapPOIflagType.BUS;
        final int[] busmaker = {NMapPOIflagType.IC1, NMapPOIflagType.IC2, NMapPOIflagType.IC3, NMapPOIflagType.IC4, NMapPOIflagType.IC5,
                NMapPOIflagType.IC4, NMapPOIflagType.IC3, NMapPOIflagType.IC2, NMapPOIflagType.IC1}; // 오류방지 추가
        final int markerArr = NMapPOIflagType.ARR;

        pathData = new NMapPathData(6);
        pathData.initPathData();
        tmapview = new TMapView(this);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setSKPMapApiKey("8abafa18-e715-38be-b488-cc384c7a73e5");

        final NMapPOIdata poiData = new NMapPOIdata(l.size() + 2, mMapViewerResourceProvider);
        poiData.beginPOIdata(l.size() + 2);
        if (busLocation.getLon() == null) { //bus 위치값이 null일경우 임의로 지정
            poiData.addPOIitem(128.6093782, 35.902852, "버스", markerBUS, 0);
            java.util.Map.Entry<String, String> busLat = new java.util.AbstractMap.SimpleEntry<>("latitude", "35.902852");
            java.util.Map.Entry<String, String> busLong = new java.util.AbstractMap.SimpleEntry<>("longitude", "128.6093782");
            startAndEndPoints.add(busLat);
            startAndEndPoints.add(busLong);
        } else {
            poiData.addPOIitem(Double.parseDouble(busLocation.getLon()), Double.parseDouble(busLocation.getLat()), "버스", markerBUS, 0);
            java.util.Map.Entry<String, String> busLat = new java.util.AbstractMap.SimpleEntry<>("latitude", busLocation.getLat());
            java.util.Map.Entry<String, String> busLong = new java.util.AbstractMap.SimpleEntry<>("longitude", busLocation.getLon());
            startAndEndPoints.add(busLat);
            startAndEndPoints.add(busLong);
        }

        for (int i = 0; i < l.size(); i++) {
            java.util.Map.Entry<String, String> startLatitude = new java.util.AbstractMap.SimpleEntry<>("latitude", l.get(i).getLat());
            java.util.Map.Entry<String, String> startLongitude = new java.util.AbstractMap.SimpleEntry<>("longitude", l.get(i).getLon());
            startAndEndPoints.add(startLatitude);
            startAndEndPoints.add(startLongitude);
            if (Integer.parseInt(p.getParent_location_id()) == i) {
                poiData.addPOIitem(Double.parseDouble(l.get(i).getLon()), Double.parseDouble(l.get(i).getLat()), l.get(i).getName(), markerArr, 0);
            } else {
                poiData.addPOIitem(Double.parseDouble(l.get(i).getLon()), Double.parseDouble(l.get(i).getLat()), l.get(i).getName(), busmaker[i], 0);
            }
        }

        poiData.endPOIdata();

        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        //asyncTask.execute(startAndEndPoints);

        try {
            asyncTask.execute(startAndEndPoints);
            //For synchronization
            asyncTask.get();
            updateText(act);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

       // Log.i("pair", "test");
        for (int i = 0; i < pairList.size(); i += 2) {
       //    Log.i("pair", pairList.get(i).getKey() + " " + Double.parseDouble(pairList.get(i + 1).getValue()));
        pathData.addPathPoint(Double.parseDouble(pairList.get(i).getValue()), Double.parseDouble(pairList.get(i + 1).getValue()), 0);
        }
        pathData.endPathData();  //경로받기종료

        mOverlayManager.createPathDataOverlay(pathData);


        mSecond = (TextView) findViewById(R.id.text1);
        mSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                totalTime=0;
                totalDistance=0;
                final NMapPOIdata poiData = new NMapPOIdata(l.size() + 2, mMapViewerResourceProvider);
                poiData.beginPOIdata(l.size() + 2);
                if (busLocation.getLon() == null) { //bus 위치값이 null일경우 임의로 지정
                    poiData.addPOIitem(128.6093782, 35.902852, "버스", markerBUS, 0);
                } else {
                    poiData.addPOIitem(Double.parseDouble(busLocation.getLon()), Double.parseDouble(busLocation.getLat()), "버스", markerBUS, 0);
                }

                for (int i = 0; i < l.size(); i++) {
                    if (Integer.parseInt(p.getParent_location_id()) == i) {
                        poiData.addPOIitem(Double.parseDouble(l.get(i).getLon()), Double.parseDouble(l.get(i).getLat()), l.get(i).getName(), markerArr, 0);
                    } else {
                        poiData.addPOIitem(Double.parseDouble(l.get(i).getLon()), Double.parseDouble(l.get(i).getLat()), l.get(i).getName(), busmaker[i], 0);
                    }
                }

                poiData.endPOIdata();

                poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
                poiDataOverlay.showAllPOIdata(0);
                poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

                mOverlayManager.clearOverlays();
                pathData.initPathData();
                pairList.clear();
                try{
                    busThread = new GetBusLocationThread();
                    busThread.start();
                    busThread.join();
                    busThread.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    startAndEndPoints.set(0, new java.util.AbstractMap.SimpleEntry<>("latitude", busLocation.getLat()));
                    startAndEndPoints.set(1, new java.util.AbstractMap.SimpleEntry<>("longitude", busLocation.getLon()));
                    poiData.removePOIitem(0);
                    poiData.addPOIitem(Double.parseDouble(busLocation.getLon()), Double.parseDouble(busLocation.getLat()), "버스", markerBUS, 0);
                    poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
                    poiDataOverlay.showAllPOIdata(0);
                    poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
                    for (int i = 0; i < startAndEndPoints.size(); i++)
                        Log.i("test", String.valueOf(startAndEndPoints.get(i)));
                    returnLocationAsync asyncTask = new returnLocationAsync();
                    asyncTask.execute(startAndEndPoints);
                    //For synchronization
                    asyncTask.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                //location information in pairList
                Log.i("pair", "test");
                for (int i = 0; i < pairList.size(); i += 2) {
                    Log.i("pair", pairList.get(i).getKey() + " " + Double.parseDouble(pairList.get(i + 1).getValue()));
                    pathData.addPathPoint(Double.parseDouble(pairList.get(i).getValue()), Double.parseDouble(pairList.get(i + 1).getValue()), 0);
                }
                pathData.endPathData();  //경로받기종료
                mOverlayManager.createPathDataOverlay(pathData);

                updateText(act);
            }
        });

    }
    public static void updateText(Activity act)
    {
        mSecond = (TextView) act.findViewById(R.id.text1);
        mDis = (TextView) act.findViewById(R.id.text2);

        act.runOnUiThread(new Runnable()
        {
            public void run()
            {
                mSecond.setText("약 " + (String.valueOf(totalTime / 60)) + "분 후 도착예정");
                if (totalDistance > 1000)
                    mDis.setText("약 " + (String.valueOf(totalDistance / 1000)) + "." + totalDistance % 1000 / 100 + " KM 남음");
                else
                    mDis.setText("약 " + (String.valueOf(totalDistance)) + " M 남음");

            }

        });
    }
    public class returnLocationAsync extends AsyncTask<java.util.List<java.util.Map.Entry<String, String>>, Void, Void> {
        @Override
        protected Void doInBackground(java.util.List<java.util.Map.Entry<String, String>>... location) {

            for (int i = 0; i < location[0].size() - 3; i += 2) {
                searchRoute(location[0].get(i + 1).getValue(), location[0].get(i).getValue(), location[0].get(i + 3).getValue(), location[0].get(i + 2).getValue());
                Log.i("test",location[0].get(i).getValue());
                Log.i("test",location[0].get(i + 1).getValue());
                Log.i("test",location[0].get(i+ 2).getValue());
                Log.i("test",location[0].get(i + 3).getValue());
            }
            return null;
        }


        protected void searchRoute(String startX, String startY, String endX, String endY) {
            Log.i("test","result"+startX);
            Log.i("test","result"+startY);
            Log.i("test","result"+endX);
            Log.i("test","result"+endY);
            try {
                URL url = new URL("https://apis.skplanetx.com/tmap/routes?version=1&endX=" + endX + "&endY=" + endY + "&startX=" + startX + "&startY=" + startY + "&reqCoordType=WGS84GEO&resCoordType=WGS84GEO&tollgateFareOption=1&roadType=32&directionOption=0&endRpFlag=16&endPoiId=67516&gpsTime=10000&angle=90&speed=60&uncetaintyP=3&uncetaintyA=3&uncetaintyAP=12&camOption=0&carType=0");  //Open the connection here, and remember to close it when job its done.
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

                totalTime += Integer.parseInt(jsonArray.getJSONObject(0).getJSONObject("properties").getString("totalTime"));
                totalDistance +=Integer.parseInt(jsonArray.getJSONObject(0).getJSONObject("properties").getString("totalDistance"));


                Log.i("NUMBER", "START"+startX + startY);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray jsonCoordinates = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates");
                    for (int j = 0; j < jsonCoordinates.length(); j++) {
                        if (!jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getString(0).startsWith("[")) {
                            java.util.Map.Entry<String, String> latitude = new java.util.AbstractMap.SimpleEntry<>("longitude", jsonCoordinates.getString(0));
                            java.util.Map.Entry<String, String> longitude = new java.util.AbstractMap.SimpleEntry<>("latitude", jsonCoordinates.getString(1));
                            pairList.add(latitude);
                            pairList.add(longitude);
                            Log.i("NUMBER", jsonCoordinates.getString(0));
                            Log.i("NUMBER", jsonCoordinates.getString(1));
                        } else {
                            java.util.Map.Entry<String, String> latitude = new java.util.AbstractMap.SimpleEntry<>("longitude", jsonCoordinates.getJSONArray(j).getString(0));
                            java.util.Map.Entry<String, String> longitude = new java.util.AbstractMap.SimpleEntry<>("latitude", jsonCoordinates.getJSONArray(j).getString(1));
                            pairList.add(latitude);
                            pairList.add(longitude);
                            Log.i("NUMBER", jsonCoordinates.getJSONArray(j).getString(0));
                            Log.i("NUMBER", jsonCoordinates.getJSONArray(j).getString(1));
                        }
                    }
                }
                Log.i("NUMBER", "END"+endX + endY);
                wr.close();
                serverAnswer.close();

            } catch (Exception e) {
                Log.e("Cuack", e.toString());
            }
        }

        protected void onPostExecute(){
        }
        @Override
        protected void onProgressUpdate(final Void... unused) {
            //Process the result here
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_position:
                startMyLocation();
                return true;

            case R.id.Menu_Myinfo:
                Intent intent= new Intent(this, Myinfo.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
                return true;
            case R.id.Menu_Kidsbus:
                Intent intent1= new Intent(this, SelectChild.class);
                intent1.putExtra("pid",pid);
                startActivity(intent1);
                return true;
            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setIcon(R.drawable.ic_kids)
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(MainActivity.this, Login.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
            d.setTitle("종료");
            d.setMessage("정말 종료 하시겠습니꺄?");
            d.setIcon(R.drawable.ic_kids);
            d.setPositiveButton("예",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    MainActivity.this.finish();
                }
            });
            d.setNegativeButton("아니요",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });
            d.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
        if (errorInfo == null) { // success
            mMapController.setMapCenter(
                    new NGeoPoint(Double.parseDouble(busLocation.getLon()),Double.parseDouble(busLocation.getLat())), 11);
        } else { // fail
            Log.e("NMAP", "onMapInitHandler: error="
                    + errorInfo.toString());
        }
    }
    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
                                                     NMapOverlayItem arg1, Rect arg2) {

        return null;
    }
    private void startMyLocation() {

        mMapLocationManager = new NMapLocationManager(this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        mMapCompassManager = new NMapCompassManager(this);

        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

        boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
        if (!isMyLocationEnabled) {
            Toast.makeText(
                    MainActivity.this,
                    "Please enable a My Location source in system settings",
                    Toast.LENGTH_LONG).show();

            Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(goToSettings);
            finish();
        } else {

        }
    }
    private void stopMyLocation() {
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);

                mMapCompassManager.disableCompass();

                mMapView.setAutoRotateEnabled(false, false);

                MapContainer.requestLayout();
            }
        }
    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

            if (errInfo != null) {
                Log.e("myLog", "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());
                Toast.makeText(MainActivity.this, errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            }else{
                Toast.makeText(MainActivity.this, placeMark.toString(), Toast.LENGTH_LONG).show();
            }

        }

    };


    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager,
                                         NGeoPoint myLocation) {

//         if (mMapController != null) {
//            mMapController.animateTo(myLocation);
//         }
            Log.d("myLog", "myLocation  lat " + myLocation.getLatitude());
            Log.d("myLog", "myLocation  lng " + myLocation.getLongitude());


            mMapController.setMapCenter(new NGeoPoint(myLocation.getLongitude(),myLocation.getLatitude()), 11);

//            findPlacemarkAtLocation(myLocation.getLongitude(), myLocation.getLatitude());
            //위도경도를 주소로 변환

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

            // stop location updating
            // Runnable runnable = new Runnable() {
            // public void run() {
            // stopMyLocation();
            // }
            // };
            // runnable.run();

            Toast.makeText(MainActivity.this,
                    "Your current location is temporarily unavailable.",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(
                NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(MainActivity.this,
                    "Your current location is unavailable area.",
                    Toast.LENGTH_LONG).show();

            stopMyLocation();
        }

    };

    /**
     * 지도 레벨 변경 시 호출되며 변경된 지도 레벨이 파라미터로 전달된다.
     */
    @Override
    public void onZoomLevelChange(NMapView mapview, int level) {
    }

    /**
     * 지도 중심 변경 시 호출되며 변경된 중심 좌표가 파라미터로 전달된다.
     */
    @Override
    public void onMapCenterChange(NMapView mapview, NGeoPoint center) {
    }

    /**
     * 지도 애니메이션 상태 변경 시 호출된다.
     * animType : ANIMATION_TYPE_PAN or ANIMATION_TYPE_ZOOM
     * animState : ANIMATION_STATE_STARTED or ANIMATION_STATE_FINISHED
     */
    @Override
    public void onAnimationStateChange(
            NMapView arg0, int animType, int animState) {
    }

    @Override
    public void onMapCenterChangeFine(NMapView arg0) {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
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

    private class GetThread extends Thread{ //모든 정류장 정보를 받아옴
        public void run()
        {
            try {
                gL=NetworkManagement.get("get_all_location_info");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetBusLocationThread extends Thread{ //모든 정류장 정보를 받아옴
        public void run()
        {
            try {
                String bLocation=NetworkManagement.get("get_current_bus_location");
                busLocation=JsonManagement.getBusLocation(bLocation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}