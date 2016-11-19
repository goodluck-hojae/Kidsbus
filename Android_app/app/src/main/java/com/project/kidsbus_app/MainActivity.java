
package com.project.kidsbus_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
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

public class MainActivity extends NMapActivity implements OnMapStateChangeListener {


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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gtest = (TextView) findViewById(R.id.get_test);

//      아래 3개 주석처리 안하면 로그인시 지도 activity로 넘어가지지가 않음.
        Intent intent = getIntent();
        pid = (String) intent.getSerializableExtra("pid");
        gtest.setText(pid + "<- 받아왔숑!");
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

        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);

        NMapPathData pathData = new NMapPathData(6);
        poiData.beginPOIdata(2);
            //요기 좌표 입력해주면, 그 좌표가 표시됩니다.
//        poiData.addPOIitem(128.60919123308707,36.29273110371902, "끝", markerId, 0);    //요기 좌표 입력해주면, 그 좌표가 표시됩니다.

        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        pathData.initPathData();
        tmapview = new TMapView(this);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setSKPMapApiKey("8abafa18-e715-38be-b488-cc384c7a73e5");
        returnLocationAsync asyncTask = new returnLocationAsync();
        asyncTask.execute();
        try {
            //For synchronization
            asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //location information in pairList
        Log.i("pair","test");
        for(int i=0;i<pairList.size();i+=2) {
            Log.i("pair", pairList.get(i).getKey() + " " + Double.parseDouble(pairList.get(i + 1).getValue()));
            pathData.addPathPoint(Double.parseDouble(pairList.get(i).getValue()), Double.parseDouble(pairList.get(i + 1).getValue()), 0);
        }
        pathData.endPathData();
        poiData.endPOIdata();
        pathData.endPathData(); //경로받기종료

//
//       pathData.addPathPoint(128.611971, 35.885180, 0);  //요기 좌표를 입력하면 경로를 찍습니다.
//        pathData.addPathPoint(128.611238, 35.884982, 0);

        NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);

//        poiDataOverlay.showAllPathData(0);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
    public class returnLocationAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {try {
            String startX="129.053922";
            String startY="35.198362";
            String endX="128.583052";
            String endY="35.798838";
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

            String totalTime = jsonArray.getJSONObject(0).getJSONObject("properties").getString("totalTime");
            String totalDistance = jsonArray.getJSONObject(0).getJSONObject("properties").getString("totalDistance");
            Log.i("info", totalTime);
            Log.i("info", totalDistance);
            for(int i=0; i<jsonArray.length(); i++){
                JSONArray jsonCoordinates = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates");

                for(int j=0; j<jsonCoordinates.length(); j++) {
                    if (!jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getString(0).startsWith("[")) {
                        java.util.Map.Entry<String,String> latitude=new java.util.AbstractMap.SimpleEntry<>("longitude",jsonCoordinates.getString(0));
                        java.util.Map.Entry<String,String> longitude=new java.util.AbstractMap.SimpleEntry<>("latitude",jsonCoordinates.getString(1));
                        pairList.add(latitude);
                        pairList.add(longitude);
                        Log.i("NUMBER", jsonCoordinates.getString(0));
                        Log.i("NUMBER", jsonCoordinates.getString(1));
                    }else {
                        java.util.Map.Entry<String,String> latitude=new java.util.AbstractMap.SimpleEntry<>("longitude",jsonCoordinates.getJSONArray(j).getString(0));
                        java.util.Map.Entry<String,String> longitude=new java.util.AbstractMap.SimpleEntry<>("latitude",jsonCoordinates.getJSONArray(j).getString(1));
                        pairList.add(latitude);
                        pairList.add(longitude);
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
            return null;
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
                    new NGeoPoint(128.6112496058999,36.284932029952614), 11);
        } else { // fail
            Log.e("NMAP", "onMapInitHandler: error="
                    + errorInfo.toString());
        }
    }
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

}