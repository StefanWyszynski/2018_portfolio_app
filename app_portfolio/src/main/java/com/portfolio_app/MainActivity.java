package com.portfolio_app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.portfolio_app.about.AboutFragment;
import com.portfolio_app.base.slidingmenu.BaseArrayItem;
import com.portfolio_app.base.slidingmenu.BaseSlidingMenuActivity;
import com.portfolio_app.base.slidingmenu.SlidingFileItem;
import com.portfolio_app.base.slidingmenu.SlidingFilesAdapter;
import com.portfolio_app.mvvm_sample.view.ui.MVVMSampleFragment;

import java.util.ArrayList;

/**
 * @author Stefan Wyszynski
 */
public class MainActivity extends BaseSlidingMenuActivity {
    public static final int SLIDING_ITEM_ABOUT = 1;
    public static final int SLIDING_ITEM_TIMELINE = 2;

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
                SLIDING_ITEM_TIMELINE);

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
                putFragment(new AboutFragment(), false, true, R.anim.fade_in, R.anim.fade_out);
                break;
            case SLIDING_ITEM_TIMELINE:
                putFragment(new MVVMSampleFragment(), false, true, R.anim.fade_in, R.anim.fade_out);
                break;

            default:
                break;
        }
        closeSlidingMenu();
    }
}
