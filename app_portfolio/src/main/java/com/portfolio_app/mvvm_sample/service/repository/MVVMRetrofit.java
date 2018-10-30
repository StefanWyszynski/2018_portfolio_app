package com.portfolio_app.mvvm_sample.service.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Copyright 2018, The Portfolio project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Stefan Wyszynski
 *
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
