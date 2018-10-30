package com.portfolio_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.view.View;
import android.widget.AdapterView;

import com.portfolio_app.about.AboutFragment;
import com.portfolio_app.base.slidingmenu.BaseArrayItem;
import com.portfolio_app.base.slidingmenu.BaseSlidingMenuActivity;
import com.portfolio_app.base.slidingmenu.SlidingFileItem;
import com.portfolio_app.base.slidingmenu.SlidingFilesAdapter;
import com.portfolio_app.mvvm_sample.view.ui.MVVMFragment;

import java.util.ArrayList;

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
public class MainActivity extends BaseSlidingMenuActivity {
    public static final int SLIDING_ITEM_ABOUT = 1;
    public static final int SLIDING_ITEM_MVVM_SAMPLE = 2;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_base_sliding_menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_as_up);
        selectDrawerListItem(0);
    }

    @Override
    protected SlidingFilesAdapter getSlidingMenuAdapter() {
        SlidingFilesAdapter<BaseArrayItem> slidingFoldersAdapter = new SlidingFilesAdapter(this);
        ArrayList<BaseArrayItem> items = new ArrayList<>();

        addSlidingMenuItem(items, getString(R.string.fragment_mvvm_sample_title), R.drawable.ic_mvvm,
                SLIDING_ITEM_MVVM_SAMPLE);

        addSlidingMenuItem(items, getString(R.string.fragment_about_title), R.drawable.ic_about, SLIDING_ITEM_ABOUT);
        slidingFoldersAdapter.setItems(items);
        return slidingFoldersAdapter;
    }

    private SlidingFileItem addSlidingMenuItem(ArrayList<BaseArrayItem> items, String text, int imageResId,
                                               int itemID) {
        SlidingFileItem gridItem = new SlidingFileItem(text, imageResId);
        gridItem.setItemID(itemID);
        items.add(gridItem);
        return gridItem;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int itemId = (int) getSlidingMenuAdapter().getItemId(position);
        switch (itemId) {
            case SLIDING_ITEM_ABOUT:
                putFragment(new AboutFragment());
                break;
            case SLIDING_ITEM_MVVM_SAMPLE:
                putFragment(new MVVMFragment());
                break;

            default:
                break;
        }
        closeSlidingMenu();
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
