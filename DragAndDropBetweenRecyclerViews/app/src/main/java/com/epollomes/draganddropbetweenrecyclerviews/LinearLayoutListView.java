package com.epollomes.draganddropbetweenrecyclerviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class LinearLayoutListView extends LinearLayout {

    RecyclerView recyclerView;

    public LinearLayoutListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public LinearLayoutListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public LinearLayoutListView(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public void setRecyclerView(RecyclerView lv) {
        recyclerView = lv;
    }

}
