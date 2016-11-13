package com.example.pancake.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Thread() {
                    @Override
                    public void run() {
                        String basic_addr = "http://155.230.118.252:5001/kidsbus/get_parent_id_by_name/";
                        try {
                            String parameter = URLEncoder.encode(new String("호재".getBytes("UTF-8")));
                            String get_addr = basic_addr+parameter;
                            get(get_addr);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        /*String post_addr = "http://192.168.123.180:5001/kidsbus/post";
                        try {
                            post(post_addr);
                            Log.i("test","post");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                }.start();
            }
        });

    }
    //post
    public String post(String address) throws IOException, JSONException {
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    address);

            StringEntity input = new StringEntity("{\"kids\":1,\"bus\":\"hojae\"}");
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return "Success";
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    //get
    public String get(String url) {
        try {
            URL reqUrl = new URL(url);
            HttpURLConnection urlConn = (HttpURLConnection) reqUrl.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Accept", "*/*");

            int resCode = urlConn.getResponseCode();
            System.out.println("resCode" + resCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String input;
            StringBuffer sb = new StringBuffer();

            while ((input = reader.readLine()) != null) {
                sb.append(input);
            }
            Log.i("test",sb.toString());
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
