package com.app.shoptree.shoptree.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.TestModel;
import com.app.shoptree.shoptree.model.UserInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lovishbajaj on 04/09/17.
 */

public class SharedPrefs {
    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String USERINFO = "userinfo";
    public static final String CARTINFO = "cart";
    public static String CARTID ;

    public SharedPrefs() {
        super();
    }


    public UserInfo getUserInfo(Context context){
        UserInfo userInfo = new UserInfo();
        userInfo = null;
        String userinfo="";
        SharedPreferences prfs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prfs.contains(USERINFO)){
            userinfo = prfs.getString(USERINFO,null);
            Gson gson = new Gson();
            userInfo = gson.fromJson(userinfo,
                    UserInfo.class);
        }
        return userInfo;
    }
    public void setUserInfo(UserInfo userInfo,Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String userinfo = gson.toJson(userInfo);

        editor.putString(USERINFO, userinfo);
        editor.apply();
    }

    public  void userLogout(Context context){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(USERINFO).apply();
    }
    public void SaveCart(Context context, ArrayList<TestModel> cartList){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String cartinfo = gson.toJson(cartList);

        editor.putString(CARTINFO, cartinfo);
        editor.apply();
    }
    public void addCartItem(Context context, TestModel cartModel) {
        ArrayList<TestModel> cart_items = getCart(context);
        if (cart_items == null) {
            cart_items = new ArrayList<>();
        }
        cart_items.add(cartModel);
        SaveCart(context, cart_items);
    }
    public ArrayList<TestModel> getCart(Context context){
        SharedPreferences settings;
        //Always return null not null to prevent crash;
        ArrayList<TestModel> ret = new ArrayList<>();

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (settings.contains(CARTINFO)){
            String jsonFavorites = settings.getString(CARTINFO, null);
            Gson gson = new Gson();
            TestModel[] cartItems = gson.fromJson(jsonFavorites,
                    TestModel[].class);

            List<TestModel> articles = Arrays.asList(cartItems);
            ret.addAll(articles);
        }
        return ret;
    }
    public int getCartCount(Context context){
        int count = 0;
        SharedPreferences settings;
        //Always return null not null to prevent crash;
        ArrayList<TestModel> ret = new ArrayList<>();

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        if (settings.contains(CARTINFO)) {
            String jsonFavorites = settings.getString(CARTINFO, null);
            Gson gson = new Gson();
            TestModel[] cartItems = gson.fromJson(jsonFavorites,
                    TestModel[].class);

            List<TestModel> articles = Arrays.asList(cartItems);
            count = articles.size();
    }
    return count;
    }
    public String getsetTempCartID(Context context){
        String tempcart;
        SharedPreferences.Editor editor = context.getSharedPreferences(CARTID, MODE_PRIVATE).edit();
        tempcart = String.valueOf (System.currentTimeMillis());
        editor.putString("cart_id",tempcart);
        editor.apply();
        return tempcart;
    }
    public String getTempCartID(Context context){
        String tempcart;
        SharedPreferences prfs = context.getSharedPreferences(CARTID, Context.MODE_PRIVATE);
        tempcart = prfs.getString("cart_id", "null");
        if (tempcart.equals ("null")){
            SharedPreferences.Editor editor = context.getSharedPreferences(CARTID, MODE_PRIVATE).edit();
            tempcart = String.valueOf (System.currentTimeMillis());
            editor.putString("cart_id",tempcart);
            editor.apply();
        }


        return tempcart;
    }



}

