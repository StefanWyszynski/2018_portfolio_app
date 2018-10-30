package com.portfolio_app.base.slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
 */
public abstract class BaseArrayAdapter<T extends BaseArrayItem> extends ArrayAdapter<T> {
    protected Context context;

    public BaseArrayAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getItemID();
    }

    public void setItems(ArrayList<T> items) {
        clear();
        addAll(items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = item.getView(inflater, parent);
        }
        IBaseArrayItemHolder viewHolder = (IBaseArrayItemHolder) convertView.getTag();
        item.fillInViewHolder(viewHolder);
        return convertView;
    }
}
