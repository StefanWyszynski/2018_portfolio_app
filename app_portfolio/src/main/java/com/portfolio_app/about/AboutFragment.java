package com.portfolio_app.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.portfolio_app.R;
import com.portfolio_app.base.utils.UtilsForString;

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
 */
public class AboutFragment extends Fragment {
    String versionName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_layout, container, false);
        setVersionInfo(view);
        return view;
    }

    private void setVersionInfo(View view) {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView textViewVersionInfo = view.findViewById(R.id.version);
        textViewVersionInfo.setText(String.format("Version: %s", versionName));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getString(R.string.fragment_about_title));
        String subtitle = getString(R.string.fragment_about_subtitle);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(UtilsForString.getTextFromHtml
                ("<small>" + subtitle +
                        "</small>"));
    }
}
