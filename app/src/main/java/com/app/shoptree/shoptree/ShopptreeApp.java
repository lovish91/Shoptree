package com.app.shoptree.shoptree;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.shoptree.shoptree.Adapter.Cart_Adapter;
import com.app.shoptree.shoptree.Utilities.ApiInterface;
import com.app.shoptree.shoptree.Utilities.RetroFit;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.database.MyDb;
import com.app.shoptree.shoptree.model.CartModel;

import com.app.shoptree.shoptree.model.TestModel;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.network.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by lovishbajaj on 31/08/17.
 */

public class ShopptreeApp extends Application {
    public static String CARTID;
    SharedPrefs sharedPrefs;
    String errorcrash;
    private ApiInterface apiInterface;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        boolean fabricInitialized = Fabric.isInitialized();
        if (fabricInitialized) {
            Crashlytics.log(errorcrash);
        }
        sharedPrefs = new SharedPrefs();
//       Fabric.with(this, new Crashlytics());
        SharedPreferences prfs = getSharedPreferences(CARTID, Context.MODE_PRIVATE);
        String cartid = prfs.getString("cart_id", "");
        Log.i("JS  ",cartid.toString());
        //prfs.edit().clear().commit();


        if (cartid ==""){
            SharedPreferences.Editor editor = getSharedPreferences(CARTID, Context.MODE_PRIVATE).edit();
            String tempcart = String.valueOf (System.currentTimeMillis());
            editor.putString("cart_id",tempcart);
            editor.apply();
            //cartid = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
            Log.i("JSON",tempcart.toString());
        }
        if (isConnected()){
            //new HttpAsyncTask().execute("https://shopptree.com/api/Api_Carts?Cartid=001");
            loadCart();
        }

        /*SharedPreferences.Editor editor = getSharedPreferences(CARTID, MODE_PRIVATE).edit();
        String tempcart = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        editor.putString("cart_id",tempcart);
        editor.apply();
         //cartid = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        Log.i("JSON",cartid.toString());*/
    }
    /*
    private class HttpAsyncTask extends AsyncTask<String, Void, ArrayList<CartModel>> {
        @Override
        protected ArrayList<CartModel> doInBackground(String... urls) {
            ArrayList<CartModel> categoryModelArrayList = new ArrayList<>();
            categoryModelArrayList.clear();
            String abc ;
            //Log.i("jsondata ",abc);


            try {
                        abc = JsonParser.getData(urls[0]);

                        JSONArray jsonArray = new JSONArray(abc);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.length()>0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String productid = jsonObject.getString("ProductID");
                                String pmid = jsonObject.getString("PMID");
                                String name = jsonObject.getString("ProName");
                                String desc = jsonObject.getString("ProDescription");
                                String img = jsonObject.getString("ProPhotoMain");
                                String quantity = jsonObject.getString("CartQty");
                                Double cartRate = Double.valueOf(jsonObject.getString("CartRate"));
                                Double cartAmount = Double.valueOf(jsonObject.getString("CartAmount"));
                                //String unit = jsonObject.getString("")

                                CartModel categoryModel = new CartModel(productid, pmid, name, "0", quantity, cartRate, cartAmount, img, "0");
                                categoryModelArrayList.add(categoryModel);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (NetworkOnMainThreadException e){
                e.printStackTrace();
            }


            return categoryModelArrayList;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<CartModel> result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //sharedPrefs.SaveCart(getBaseContext(),result);

        }
    }
    */
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    protected void loadCart(){
        apiInterface = RetroFit.getClient().create(ApiInterface.class);
        apiInterface.getCart("001").enqueue(new Callback<ArrayList<TestModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TestModel>> call, Response<ArrayList<TestModel>> response) {
                Log.d("mat", String.valueOf(response.code()));
                ArrayList<TestModel> testModels = response.body();

                MyDb myDb = new MyDb(getBaseContext());
                myDb.open();
                myDb.deleteAllData();
                for (TestModel a :testModels){
                    int i=myDb.checkAvailable(a.getPMID());
                    if (i==1){
                        Log.d("ma0", String.valueOf(response.code()));
                    }else {
                        myDb.insertData(a.getProductID(),a.getPMID(),a.getProName(),a.getProPhotoMain(),String.valueOf(a.getCartRate()),a.getCartQty());
                        Log.d("insert", String.valueOf(response.code()));

                    }
                }
                myDb.close();

                //sharedPrefs.SaveCart(getBaseContext(), testModels);

            }

            @Override
            public void onFailure(Call<ArrayList<TestModel>> call, Throwable t) {

            }
        });
    }
}
