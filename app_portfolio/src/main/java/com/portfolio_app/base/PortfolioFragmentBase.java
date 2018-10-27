package com.portfolio_app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.portfolio_app.MainActivity;
import com.portfolio_app.R;
import com.portfolio_app.base.utils.UtilsForString;
import com.portfolio_app.mvvm_sample.service.model.database.DBUsersManager;
import com.portfolio_app.mvvm_sample.service.model.database.DBUsersTable;
import com.portfolio_app.services.ObjectsProvider;

/**
 * @author Stefan Wyszynski
 */
public abstract class PortfolioFragmentBase extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.refresh, menu);
    }

    public void setTitle(String title) {
        getActivity().setTitle(title);
    }

    public void setSubTitle(String subtitle) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }

    public void setSubTitleWithHtmlSmallTag(String subtitle) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(UtilsForString.getTextFromHtml
                ("<small>" + subtitle +
                        "</small>"));
    }

    public MainActivity getMainActivity() {
        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            return (MainActivity) activity;
        }
        return null;
    }

    public DBUsersTable getDBUsersTable() {
        DBUsersManager dbUsersManager = ObjectsProvider.getInstance().get(DBUsersManager.class);
        if (dbUsersManager != null) {
            return dbUsersManager.getSetting(getClass().getSimpleName());
        } else {
            return null;
        }
    }
}
