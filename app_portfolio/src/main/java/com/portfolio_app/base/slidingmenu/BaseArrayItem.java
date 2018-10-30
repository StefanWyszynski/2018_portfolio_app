package com.portfolio_app.base.slidingmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public abstract class BaseArrayItem<T extends IBaseArrayItemHolder> {
    protected int itemID;

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public abstract int getLayoutID();

    public abstract T createViewHolder(View view);

    public abstract void fillInViewHolder(T viewHolder);

    public View getView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutID(), parent, false);
        view.setTag(createViewHolder(view));
        return view;
    }
}
