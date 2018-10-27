package com.portfolio_app.mvvm_sample.view.ui;

import android.arch.lifecycle.Observer;
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

import com.portfolio_app.R;
import com.portfolio_app.base.DownloadResult;
import com.portfolio_app.base.PortfolioFragmentBase;
import com.portfolio_app.mvvm_sample.service.model.UserInfo;
import com.portfolio_app.mvvm_sample.service.model.UserList;
import com.portfolio_app.mvvm_sample.view.adapter.UserListAdapter;
import com.portfolio_app.mvvm_sample.viewmodel.MVVMSampleModelView;

import java.util.Arrays;

/**
 * @author Stefan Wyszynski
 */
public class MVVMSampleFragment extends PortfolioFragmentBase {
    private MVVMSampleModelView mvvmSampleModelView;
    private LinearLayout progressConainer;
    private RecyclerView userList;

    private UserListAdapter userListAdapter;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(getString(R.string.fragment_mvvm_sample_title));
        setSubTitleWithHtmlSmallTag(getString(R.string.fragment_mvvm_sample_subtitle));
        bindViewModelData();
        updateUserListDataAdapter(null);
    }

    private void bindViewModelData() {
        mvvmSampleModelView = ViewModelProviders.of(this).get(MVVMSampleModelView.class);
        mvvmSampleModelView.getWeekLiveData().observe(this, new Observer<DownloadResult<UserList>>() {
            @Override
            public void onChanged(@Nullable DownloadResult<UserList> downloadResult) {
                hideProgressBar();
                updateUserListDataAdapter(downloadResult);
            }
        });
        downloadUserListData();
    }

    public void downloadUserListData() {
        showProgressBar();
        mvvmSampleModelView.downloadUserList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_refresh) {
            downloadUserListData();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showProgressBar() {
        progressConainer.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressConainer.setVisibility(View.GONE);
    }

    private void updateUserListDataAdapter(DownloadResult<UserList> downloadResult) {

        if (downloadResult != null && downloadResult.result != null) {
            userListAdapter.setUserList(downloadResult.result.data);
        } else {
            UserInfo emptyUser = new UserInfo();
            emptyUser.first_name = "No data available. Click on refresh button or swipe to refresh";
            userListAdapter.setUserList(Arrays.asList(emptyUser));
        }
    }
}
