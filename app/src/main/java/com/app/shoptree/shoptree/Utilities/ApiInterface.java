package com.app.shoptree.shoptree.Utilities;

import com.app.shoptree.shoptree.model.Address;
import com.app.shoptree.shoptree.model.CategoryModel;
import com.app.shoptree.shoptree.model.CityModel;
import com.app.shoptree.shoptree.model.LoginModel;
import com.app.shoptree.shoptree.model.Order;
import com.app.shoptree.shoptree.model.OrderDetailModel;
import com.app.shoptree.shoptree.model.ProductModel;
import com.app.shoptree.shoptree.model.StateModel;
import com.app.shoptree.shoptree.model.TestModel;
import com.app.shoptree.shoptree.model.UserInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lovishbajaj on 03/10/17.
 */

public interface ApiInterface {

    @GET("Api_Categories")
    Call<List<CategoryModel>> getCategories();

    @GET("Api_Products")
    Call<ArrayList<ProductModel>> getProducts(@Query ("id")String id,@Query ("page")int page );

    @POST("Api_signin")
    Call<UserInfo> UserLogin(@Body JsonObject jsonObject);

    @GET("Api_Carts")
    Call<ArrayList<TestModel>> getCart(@Query("cartid") String cartId);

    @GET("Api_UpdateCart")
    Call<String> deleteCartItem (@Query("CartID") String CartID, @Query("PMID") String PMID);

    @GET("api_States/")
    Call <List<StateModel>> getStates();

    @GET ("api_cities")
    Call<ArrayList<CityModel>> getCities (@Query("StateId")String StateId);

    @GET ("api_signin/")
    Call<ResponseBody> updateCart(@Query("Cartid") String Cartid, @Query ("Userid") String userid);

    @POST("api_Order")
    Call<ResponseBody> placeOrder (@Body JsonObject jsonObject);

    @GET("api_OrderDetails")
    Call<OrderDetailModel> getOrderDetail (@Query ("UserID")String UserID,@Query ("OrderID")String OrderID);

    @GET("api_OrderDetails")
    Call<List<Order>> getOrders(@Query("UserID") String id);

    @GET("api_verifyMobile")
    Call <String> requestOTP (@Query ("mobile") String mobile,@Query ("email")String email);

    @POST("api_verifyMobile")
    Call <ResponseBody> registerMobileNo(@Body JsonObject jsonObject);


}
