package baikal.web.footballapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {
//    public static final String BASE_URL = "https://footballapi.ibb.su";
    public static final String BASE_URL = "https://football.bw2api.ru";

    public Logger log = LoggerFactory.getLogger(Controller.class);


    public static FootballApi getApi() {
        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(chain -> )
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        return retrofit.create(FootballApi.class);
    }


}
