package com.portfolio_app.mvvm_sample.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portfolio_app.mvvm_sample.service.repository.MVVMRetrofitService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MVVMFragmentRetrofitModule {

    public static final String HTTP_GAMESDEV_AYZ_PL = "https://reqres.in";

    @Provides
    @MVVMFragmentScope
    public Gson provideGson() {
        return new GsonBuilder().setLenient().create();
    }

    @Provides
    @MVVMFragmentScope
    public Retrofit provideRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(HTTP_GAMESDEV_AYZ_PL)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @MVVMFragmentScope
    public MVVMRetrofitService provideMVVMRetrofitService(Retrofit retrofit) {
        return retrofit.create(MVVMRetrofitService.class);
    }


}
