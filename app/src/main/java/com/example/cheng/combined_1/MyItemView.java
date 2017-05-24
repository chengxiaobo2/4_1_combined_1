package com.example.cheng.combined_1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by cheng on 2017/5/22.
 */

public class MyItemView extends ViewGroup
{
    private int index=0;
    private int originIndex=0;

    public MyItemView(Context context) {
        super(context);
    }

    public MyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureChildren(widthMeasureSpec,heightMeasureSpec);

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        if(getChildCount()>0)
        {
            int w=getChildAt(0).getMeasuredWidth();
            int h=getChildAt(0).getMeasuredHeight();
            getChildAt(0).layout(0,0,w,h);
        }
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }


    public int getOriginIndex() {
        return originIndex;
    }

    public void setOriginIndex(int originIndex) {
        this.originIndex = originIndex;
    }
}
