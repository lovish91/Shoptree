package com.app.shoptree.shoptree;

import android.util.Log;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.http.POST;

import static com.facebook.FacebookSdk.getApplicationContext;


public class JsonParser {

    public static String getData(String uri) {

        BufferedReader reader = null;
        String result = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(25000);
            con.setRequestProperty("API_KEY","MyApiKey");
            con.setDoInput(true);
            con.connect();
            InputStream is = con.getInputStream();
            reader = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));
            String data = null;
            String webPage = "";
            while ((data = reader.readLine()) != null) {
                webPage += data + "\n";
            }
            result = webPage;
        } catch (SocketTimeoutException e){
            Toast.makeText(getApplicationContext(), "Socket Timeout", Toast.LENGTH_LONG).show();
        }
        catch (ConnectTimeoutException bug) {
            Toast.makeText(getApplicationContext(), "Connection Timeout", Toast.LENGTH_LONG).show();
        }
        catch (UnsupportedEncodingException e) {
            Log.e("Fail 1", e.toString());
        } catch (MalformedURLException e) {
            Log.e("Fail 2", e.toString());
            //e.printStackTrace();
        } catch (IOException e) {
            Log.e("Fail 3", e.toString());
        } catch (Exception e) {
            Log.e("Fail 4", e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("fail 2", e.toString());
                    //e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static String sendData (String uri) {
        DataOutputStream printout = null;
        String result = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.setReadTimeout(10000);
            con.setRequestMethod("POST");
            con.setConnectTimeout(25000);
            con.setRequestProperty("API_KEY", "MyApiKey");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            //BufferedOutputStream is = (BufferedOutputStream) con.getOutputStream();
            //writer = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            //String data = null;
            //String webPage = "";

            int responseCode=con.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_CREATED) {
                //String line;
                //BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                //while ((line=br.readLine()) != null) {
                    result = "success";

            }
            else {

                result=con.getResponseMessage();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public static String postData (String uri,JSONObject jsonObject) {
        DataOutputStream printout = null;
        String result = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.setReadTimeout(10000);
            con.setRequestMethod("POST");
            con.setConnectTimeout(25000);
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestProperty("Accept","application/json");
            con.setRequestProperty("API_KEY", "MyApiKey");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            DataOutputStream out = new  DataOutputStream(con.getOutputStream());
            out.writeBytes(jsonObject.toString());
            out.flush();
            out.close();

             result=con.getResponseMessage();

        } catch (SocketTimeoutException e){
            Toast.makeText(getApplicationContext(), "Socket Timeout", Toast.LENGTH_LONG).show();
        } catch (ConnectTimeoutException bug) {
            Toast.makeText(getApplicationContext(), "Connection Timeout", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public static String PutData (String uri,JSONObject jsonObject) {
        DataOutputStream printout = null;
        String status = null;
        String result = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(25000);
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestProperty("Accept","application/json");
            con.setRequestProperty("API_KEY", "MyApiKey");
            con.setDoInput(true);
            con.setDoOutput(true);


            DataOutputStream out = new  DataOutputStream(con.getOutputStream());
            out.writeBytes(jsonObject.toString());
            out.flush();
            out.close();


            int responseCode=con.getResponseCode();

            status = String.valueOf(con.getResponseCode());
            result= con.getResponseMessage();
            Log.i("STATUS", String.valueOf(con.getResponseCode()));
            Log.i("MSG" , con.getResponseMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public static String delData(String uri) {
    DataOutputStream printout = null;
    String result = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(25000);
            con.setRequestProperty("API_KEY","MyApiKey");
            con.setDoInput(true);
            con.connect();
        //BufferedOutputStream is = (BufferedOutputStream) con.getOutputStream();
        //writer = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        //String data = null;
        //String webPage = "";

        int responseCode=con.getResponseCode();
            Log.i("he", String.valueOf(con.getResponseCode()));

        if (responseCode == HttpsURLConnection.HTTP_CREATED) {
            //String line;
            //BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
            //while ((line=br.readLine()) != null) {
            result = "success";

        }
        else {

            result=String.valueOf(con.getResponseCode());

        }
    } catch (SocketTimeoutException e){
            Toast.makeText(getApplicationContext(), "Socket Timeout", Toast.LENGTH_LONG).show();
        } catch (ConnectTimeoutException bug) {
            Toast.makeText(getApplicationContext(), "Connection Timeout", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
        e.printStackTrace();
    }

        return result;
}
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
