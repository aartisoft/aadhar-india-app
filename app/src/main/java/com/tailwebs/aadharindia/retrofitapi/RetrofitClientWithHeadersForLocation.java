package com.tailwebs.aadharindia.retrofitapi;

import com.tailwebs.aadharindia.utils.Constants;
import com.tailwebs.aadharindia.utils.GlobalValue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientWithHeadersForLocation {

    public static final String BASE_IMAGE_URL = "";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {


        OkHttpClient client = new OkHttpClient.Builder()

                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        return chain.proceed(chain.request()
                                .newBuilder()
                                .addHeader("secret", GlobalValue.secret)
                                .addHeader("secret-id", GlobalValue.secret_id)
                                .addHeader("lat", GlobalValue.latitude)
                                .addHeader("long", GlobalValue.longitude)
                                .build());
                    }
                })
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;
    }

}
