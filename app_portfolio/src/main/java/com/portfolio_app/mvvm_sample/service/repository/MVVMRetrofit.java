package com.portfolio_app.mvvm_sample.service.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Stefan Wyszynski
 */
public class MVVMRetrofit {

    public static final String HTTP_GAMESDEV_AYZ_PL = "https://reqres.in";
    private final MVVMRetrofitService service;

    public MVVMRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
//        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//        builder.cache(
//                new Cache(context.getCacheDir(), CACHE_SIZE_BYTES));
//        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTP_GAMESDEV_AYZ_PL)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        service = retrofit.create(MVVMRetrofitService.class);
    }

    public MVVMRetrofitService getService() {
        return service;
    }

}
