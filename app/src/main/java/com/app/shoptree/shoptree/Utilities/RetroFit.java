package com.app.shoptree.shoptree.Utilities;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lovishbajaj on 03/10/17.
 */

public class RetroFit {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder()
                        .header("API_KEY","MyApiKey");

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://shopptree.com/api/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
