package com.project.kidsbus_app;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import javax.net.ssl.HttpsURLConnection;
/**
 * Created by yongs on 2016-11-16.
 */
public class NetworkManagement {

    public NetworkManagement(){
    }
    //post
    public static String BusLocation (String lat,String lon) throws IOException, JSONException {
        try {
            String address = "http://155.230.118.252:5001/kidsbus/post_current_bus_location";

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(address);

            StringEntity input = new StringEntity("{\"latitude\":\""+lat+"\",\"longitude\":\""+lon+"\"}");
            Log.e("SE","{\"latitude\":\""+lat+"\",\"longitude\":\""+lon+"\"}");
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
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

    //post
    public static String post_parent (String address,String name,String id,String pw,String date,String phNumber,String location_id) throws IOException, JSONException {
        try {
            address = "http://155.230.118.252:5001/kidsbus/post/register_parent";
            String eName=URLEncoder.encode(name);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    address);
            StringEntity input = new StringEntity("{ \"parent\" : {" +
                    "\"name\" : \""+eName+"\"," +
                    " \"account\" : \""+id+"\"," +
                    " \"password\" : \""+pw+"\"," +
                    " \"birth_date\": \""+date+"\","+
                    " \"phone_number\": \""+phNumber+"\","+
                    " \"location_id\": "+location_id+"}}"  );
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
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
    //post
    public static String post_child (String address,String name,String gender,String date,String pid) throws IOException, JSONException {
        try {
            address = "http://155.230.118.252:5001/kidsbus/post/register_child";
            String eName=URLEncoder.encode(name);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    address);
            StringEntity input = new StringEntity("{ \"child\" : {\"name\" : \""+eName+"\", \"gender\" : \""+gender+"\", \"birth_date\" : \""+date+"\", \"parent_id\": "+pid+"}}" );
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
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
    public static String get(String get_addr) {
        try {
            String basic_addr = "http://155.230.118.252:5001/kidsbus/";
            URL reqUrl = new URL(basic_addr+get_addr);
            HttpURLConnection urlConn = (HttpURLConnection) reqUrl.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Accept", "*/*");

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
        return "실패";
    }

    public static int check(String id,String pw) {
        try {
            //http://155.230.118.252:5001/kidsbus/login/sonhj97/1234
            String basic_addr = "http://155.230.118.252:5001/kidsbus/login/";
            URL reqUrl = new URL(basic_addr+id+"/"+pw);
            HttpURLConnection urlConn = (HttpURLConnection) reqUrl.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Accept", "*/*");

            int resCode = urlConn.getResponseCode();
            System.out.println("resCode" + resCode);

            return resCode;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String del_child(String cid) {
        try {
            String basic_addr = "http://155.230.118.252:5001/kidsbus/get/delete_child/";
            URL reqUrl = new URL(basic_addr+cid);
            HttpURLConnection urlConn = (HttpURLConnection) reqUrl.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Accept", "*/*");

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
        return "실패";
    }
}
