package com.app.shoptree.shoptree.Utilities;

import com.app.shoptree.shoptree.model.CategoryModel;
import com.app.shoptree.shoptree.model.LoginModel;
import com.app.shoptree.shoptree.model.TestModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lovishbajaj on 03/10/17.
 */

public interface ApiInterface {

    @GET("Api_Categories")
    Call<List<CategoryModel>> getCategories();

    @POST("Api_signin")
    Call<String> UserLogin(@Body JSONObject loginModel);

    @GET("Api_Carts")
    Call<ArrayList<TestModel>> getCart(@Query("cartid") String cartId);


}
