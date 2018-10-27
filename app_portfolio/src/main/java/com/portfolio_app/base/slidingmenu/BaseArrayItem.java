package com.portfolio_app.base.slidingmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
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
