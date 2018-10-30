package com.portfolio_app.mvvm_sample.service.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author Stefan Wyszynski
 */
public class UserList {
    public int page;
    public int per_page;
    public int total;
    public int total_pages;
    public List<UserInfo> data;

    public static UserList fromJson(String json) {
        return new Gson().fromJson(json, UserList.class);
    }
}
