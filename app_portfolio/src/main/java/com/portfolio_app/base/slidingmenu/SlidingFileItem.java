package com.portfolio_app.base.slidingmenu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.portfolio_app.R;

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
public class SlidingFileItem extends BaseArrayItem {
    public String title;
    public int imgResID;

    public SlidingFileItem(String title, int imgResID) {
        super();
        this.title = title;
        this.imgResID = imgResID;
    }

    @Override
    public int getLayoutID() {
        return R.layout.sliding_menu_item;
    }

    @Override
    public IBaseArrayItemHolder createViewHolder(View view) {
        SlidingItemHolder slidingItemHolder = new SlidingItemHolder();
        slidingItemHolder.txtTitle = (TextView) view.findViewById(R.id.first_name_title);
        slidingItemHolder.imageItem = (ImageView) view.findViewById(R.id.avatar);
        return slidingItemHolder;
    }

    @Override
    public void fillInViewHolder(IBaseArrayItemHolder viewHolder) {
        SlidingItemHolder holder = (SlidingItemHolder) viewHolder;
        holder.txtTitle.setText(title);
        holder.imageItem.setImageResource(imgResID);
    }

    public class SlidingItemHolder implements IBaseArrayItemHolder {
        public TextView txtTitle;
        public ImageView imageItem;
    }
}
