package com.portfolio_app.mvvm_sample.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.portfolio_app.PortfolioApp;
import com.portfolio_app.R;
import com.portfolio_app.SimpleIdlingResource;
import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.base.PortfolioFragmentBase;
import com.portfolio_app.mvvm_sample.di.MVVMFragmentComponent;
import com.portfolio_app.mvvm_sample.service.model.UserInfo;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.view.adapter.UserListAdapter;
import com.portfolio_app.mvvm_sample.viewmodel.MVVMModelView;
import com.portfolio_app.mvvm_sample.viewmodel.MVVMModelViewFactory;

import java.util.Arrays;

import javax.inject.Inject;

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
public class MVVMFragment extends PortfolioFragmentBase {
    @Inject
    MVVMModelViewFactory mvvmModelViewFactory;
    private MVVMModelView mvvmModelView;
    private MVVMFragmentComponent mvvmFragmentComponent;

    private LinearLayout progressConainer;
    private RecyclerView userList;
    private UserListAdapter userListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mvvm_example, container, false);

        progressConainer = view.findViewById(R.id.progressHolder);
        prepareUserListRecyclerView(view);
        hideProgressBar();
        return view;
    }

    private void prepareUserListRecyclerView(View view) {
        userList = view.findViewById(R.id.user_list);
        userList.setLayoutManager(new LinearLayoutManager(getContext()));
        userListAdapter = new UserListAdapter(getContext());
        userList.setAdapter(userListAdapter);

    }

    private void injectFragment() {
        if (mvvmFragmentComponent == null) {
            mvvmFragmentComponent = PortfolioApp.getAppComponent().mvvmFragmentComponentBuilder().build();
            mvvmFragmentComponent.inject(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(getString(R.string.fragment_mvvm_sample_title));
        setSubTitleWithHtmlSmallTag(getString(R.string.fragment_mvvm_sample_subtitle));
        bindViewModelData();
        updateUserListDataAdapter(null);
    }


    private void bindViewModelData() {
        mvvmModelView = ViewModelProviders.of(this, mvvmModelViewFactory).get(MVVMModelView.class);
        mvvmModelView.getWeekLiveData().observe(this, downloadResult -> {
            hideProgressBar();
            updateUserListDataAdapter(downloadResult);

            setItleState(true);
        });
        downloadUserListData();
    }

    public void downloadUserListData() {
        showProgressBar();
        setItleState(false);
        mvvmModelView.downloadUserList(mvvmFragmentComponent);
    }

    private void setItleState(boolean b) {
        SimpleIdlingResource idlingResource = (SimpleIdlingResource) getMainActivity().getIdlingResource();
        if (idlingResource != null) {
            idlingResource.setIdleState(b);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_refresh) {
            downloadUserListData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressBar() {
        progressConainer.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressConainer.setVisibility(View.GONE);
    }

    private void updateUserListDataAdapter(DownloadResult<UserList> downloadResult) {
        if (downloadResult != null && downloadResult.result != null &&
                downloadResult.status != DownloadResult.ResultStatus.FAILURE) {
            userListAdapter.setUserList(downloadResult.result.data);
        } else {
            UserInfo emptyUser = new UserInfo();
            emptyUser.first_name = "No data available. Click on refresh button or swipe to refresh";
            userListAdapter.setUserList(Arrays.asList(emptyUser));
        }
    }
}
