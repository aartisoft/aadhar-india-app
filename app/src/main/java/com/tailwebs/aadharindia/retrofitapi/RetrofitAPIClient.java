package com.tailwebs.aadharindia.retrofitapi;

import com.tailwebs.aadharindia.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitAPIClient {

    public static final String BASE_IMAGE_URL = "";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().
                    baseUrl(Constants.BASE_URL).
                    addConverterFactory(ScalarsConverterFactory.create()).
                    addConverterFactory(GsonConverterFactory.create()).
                    client(retroLogClient()).build();
        }
        return retrofit;
    }


    public static OkHttpClient retroLogClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder().
                connectTimeout(300, TimeUnit.SECONDS).
                readTimeout(300, TimeUnit.SECONDS).
                writeTimeout(300, TimeUnit.SECONDS).
                addInterceptor(interceptor).build();
    }




}
