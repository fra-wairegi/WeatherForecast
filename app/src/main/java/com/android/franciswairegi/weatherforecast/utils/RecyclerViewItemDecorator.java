package com.android.franciswairegi.weatherforecast.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.android.franciswairegi.weatherforecast.R;

public class RecyclerViewItemDecorator extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Drawable mDivider;

    /**
     * Constructor to initialise the context and drawable to display the dividers
     * @param mContext Context of the Activity
     */
    public RecyclerViewItemDecorator(Context mContext){
        this.mContext = mContext;
        mDivider = ContextCompat.getDrawable(mContext, R.drawable.divider_line);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int dividerRight = parent.getWidth() - (int)mContext.getResources().getDimension(R.dimen.activity_horizontal_margin);
        int dividerLeft = parent.getPaddingLeft();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(c);
        }
    }
}
