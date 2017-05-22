package com.example.cheng.combined_1;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cheng on 2017/5/22.
 */

public class MyDragView extends ViewGroup {


    private static final int ROW_COUNT=3;

    private float mDensity;
    private int mRowSpacing;
    private int mColumnSpacing;
    private Context mContext;

    private MyDragViewAdapter adapter;
    private int mRowCount=0;
    private int mHeightValue=0;
    private int mViewWidth=0;
    private int mViewHeight=0;

    private String tag="MyDragView";


    public MyDragView(Context context)
    {
        super(context);

        init(context);
    }

    public MyDragView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context);
    }

    public MyDragView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context)
    {
        DisplayMetrics display=new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(display);

        mDensity=display.density;

        mRowSpacing=(int)mDensity*20;
        mColumnSpacing =(int)mDensity*20;

        mContext=context;
    }


    public void setAdapter(MyDragViewAdapter adapter)
    {
        this.adapter = adapter;

        if(adapter.getCount()>0)
        {
            mRowCount=adapter.getCount()%ROW_COUNT==0?adapter.getCount()/ROW_COUNT:adapter.getCount()/ROW_COUNT+1;

            for(int i=0;i<adapter.getCount();i++)
            {
                addItemView(adapter.getView(i),i);
            }
        }
    }

    /**
     * 封装一层
     *
     * @param view
     * @param i
     */
    private void addItemView(View view,int i)
    {
       final MyItemView myItemView=new MyItemView(mContext);
        myItemView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        myItemView.setIndex(i);
        myItemView.addView(view);

        addView(myItemView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        int widthValue=0;
        int heightValue=0;

        if(getChildCount()>0)
        {
            int parentWidthValue=MeasureSpec.getSize(widthMeasureSpec);
            int parentHeightValue=MeasureSpec.getSize(heightMeasureSpec);

            LayoutParams params=getLayoutParams();
            int paramsWidth=params.width;
            if(paramsWidth>0)
            {
                widthValue=paramsWidth;
            }else if(paramsWidth==LayoutParams.MATCH_PARENT)
            {
                widthValue=parentWidthValue;
            }else  if(paramsWidth==LayoutParams.WRAP_CONTENT)
            {
                widthValue=parentWidthValue;
            }

            mViewWidth=(widthValue-(ROW_COUNT+1)*mRowSpacing)/ROW_COUNT;
            mViewHeight=mViewWidth;

            int paramsHeight=params.height;
            if(paramsHeight>0)
            {
                heightValue=paramsHeight;
            }else if(paramsHeight== LayoutParams.MATCH_PARENT)
            {
                heightValue=parentHeightValue;
            }else if(paramsHeight==LayoutParams.WRAP_CONTENT)
            {
                heightValue=mRowCount*mViewHeight+(mRowCount+1)* mColumnSpacing;
            }

            int count=getChildCount();
            mRowCount=count%ROW_COUNT==0?count/ROW_COUNT:count/ROW_COUNT+1;

            int childMesureSpecWidth=MeasureSpec.makeMeasureSpec(mViewWidth,MeasureSpec.EXACTLY);
            int childMesureSpecHeight=MeasureSpec.makeMeasureSpec(mViewHeight,MeasureSpec.EXACTLY);
            measureChildren(childMesureSpecWidth,childMesureSpecHeight);
        }

        setMeasuredDimension(widthValue,heightValue);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        if(getChildCount()>0)
        {
            //顺序不变的情况
//            for(int i=0;i<mRowCount;i++)
//            {
//                for(int j=0;j<ROW_COUNT;j++)
//                {
//
//                    log("===i*ROW_COUNT+j============"+(i*ROW_COUNT+j));
//
//                    if(i*ROW_COUNT+j<getChildCount())
//                    {
//                        View view=getChildAt(i*ROW_COUNT+j);
//
//                        int left=j*mViewWidth+(j+1)*mRowSpacing;
//                        int top=i*mViewHeight+(i+1)*mColumnSpacing;
//
//                        view.layout(left,top,left+mViewWidth,top+mViewHeight);
//                    }
//                }
//            }

            for(int i=0;i<getChildCount();i++)
            {
                MyItemView view =(MyItemView)getChildAt(i);

                int index=view.getIndex();

                int row=index/ROW_COUNT;
                int column=index%ROW_COUNT;


                int left=column*mViewWidth+(column+1)*mRowSpacing;
                int top=row*mViewHeight+(row+1)*mColumnSpacing;

                view.layout(left,top,left+mViewWidth,top+mViewHeight);
            }

        }

    }

    interface MyDragViewAdapter
    {
        int getCount();
        View getView(int i);
    }

    private void log(String s)
    {
        Log.d(tag,tag+"========="+s);
    }


}
