package com.example.cheng.combined_1;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 2017/5/22.
 */

public class MyDragView extends ViewGroup {


    private static final int ROW_COUNT=3;


    private GestureDetector gestureDetector;
    private GestureLongPress gestureLongPress;
    private MyDragViewAdapter adapter;

    private View mCurrentMoveView=null;
    private View mSelectedView=null;
    private int mSelectedIndex=-1;
    private int mCurrentSelectedIndex=-1;
    private int mDownX;
    private int mDownY;
    private int mlastX;
    private int mlastY;

    private int mLongPress =-1;
    private boolean mIsBeingAnimation=false;

    private float mDensity;
    private int mRowSpacing;
    private int mColumnSpacing;
    private Context mContext;
    private int mRowCount=0;
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

        gestureDetector= new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e)
            {
                log("=======onLongPress======"+e.getX());
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        gestureLongPress=new GestureLongPress() {

            @Override
            public void onLongPress(int x, int y)
            {
                //判断是不是点中某个View

                log("=======onLongPress=ziji====="+x+"==============="+y);

                int index=getSelectedIndex(x,y);

                if(index!=-1)
                {
                    for(int i=0;i<getChildCount();i++)
                    {
                        MyItemView myItemView=((MyItemView)getChildAt(i));
                        if(myItemView.getIndex()==index)
                        {
                            myItemView.setAlpha(0.5f);

                            addItemView(adapter.getView(myItemView.getOriginIndex()),myItemView.getOriginIndex(),myItemView.getIndex());

                            mCurrentMoveView=getChildAt(getChildCount()-1);

                            AnimatorSet set=new AnimatorSet();
                            set.playTogether(ObjectAnimator.ofFloat(mCurrentMoveView,"scaleX",1.0f,1.1f),ObjectAnimator.ofFloat(mCurrentMoveView,"scaleY",1.0f,1.1f));
                            set.setDuration(300);
                            set.start();

                            Vibrator vibrator=(Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(new long[]{100,200},-1);

                            mSelectedView=myItemView;

                            mSelectedIndex=myItemView.getIndex();
                            mCurrentSelectedIndex=mSelectedIndex;

                            break;
                        }
                    }
                }
                log("======index============="+index);

            }
        };
    }

    /**
     * 根据坐标 确定选中的 index
     *
     * @param x
     * @param y
     * @return
     */
    private int getSelectedIndex(int x,int y)
    {
        boolean isInView=true;
        int index=-1;

        int indexColumn=x/(mViewWidth+mColumnSpacing);
        if(x%(mViewWidth+mColumnSpacing)<mColumnSpacing)
        {
            isInView=false;
        }

        int indexRow=y/(mViewHeight+mRowSpacing);
        if(x%(mViewHeight+mRowSpacing)<mRowSpacing)
        {
            isInView=false;
        }

        if(isInView)
        {
            index=indexRow*ROW_COUNT+indexColumn;

            if(index>=adapter.getCount())
            {
                index=-1;
            }
        }

        log("======getSelectedIndex============="+index);
        return index;
    }



    public void setAdapter(MyDragViewAdapter adapter)
    {
        this.adapter = adapter;

        if(adapter.getCount()>0)
        {
            mRowCount=adapter.getCount()%ROW_COUNT==0?adapter.getCount()/ROW_COUNT:adapter.getCount()/ROW_COUNT+1;

            for(int i=0;i<adapter.getCount();i++)
            {
                addItemView(adapter.getView(i),i,i);
            }
        }

//        //TODO test
//
//        this.post(new Runnable() {
//            @Override
//            public void run() {
//
//                MyItemView myItemView=(MyItemView)getChildAt(0);
//                Animator animator=ObjectAnimator.ofFloat(myItemView,"translationX",0,-308);
//                animator.setDuration(1000);
//                animator.start();
//
//                log("==============post============");
//            }
//        });

    }

    /**
     * 封装一层
     */
    private void addItemView(View view,int originIndex,int index)
    {
       final MyItemView myItemView=new MyItemView(mContext);
        myItemView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        myItemView.setIndex(index);
        myItemView.setOriginIndex(originIndex);

        myItemView.addView(view);

        addView(myItemView);
    }

    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        gestureDetector.onTouchEvent(event);

        int x=(int) event.getX();
        int y=(int) event.getY();

        switch (event.getAction()&MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                mDownX=(int) event.getX();
                mDownY=(int) event.getY();

                mLongPress =-1;
                mCurrentMoveView=null;

                log("====ACTION_DOWN=====mDownX=="+mDownX+"=====mDownY==="+mDownY);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(mLongPress ==-1)
                        {
                            gestureLongPress.onLongPress(mDownX,mDownY);
                            mLongPress =1;
                        }

                    }
                },680);
                break;

            case MotionEvent.ACTION_MOVE:

                if(mLongPress ==-1&&!(mlastX==x&&mlastY==y))
                {
                    mLongPress =0;
                    log("====ACTION_MOVE=====x=="+x+"=====y==="+y);

                }else if (mLongPress ==0)
                {

                }else if(mLongPress ==1)
                {
                    if(mCurrentMoveView!=null)
                    {
                        mCurrentMoveView.setTranslationX(x-mDownX);
                        mCurrentMoveView.setTranslationY(y-mDownY);

                        int index=getSelectedIndex(x,y);

                        if(index!=-1&&!mIsBeingAnimation&index!=mSelectedIndex)
                        {
                            log("=========移动=====================");

                            mCurrentSelectedIndex=index;

                            List<Animator> animators=new ArrayList<Animator>();

                            if (mCurrentSelectedIndex > mSelectedIndex)
                            { //往后面移动
                                for (int i = 0; i < adapter.getCount(); i++) {
                                    MyItemView myItemView = (MyItemView) getChildAt(i);
                                    int viewIndex = myItemView.getIndex();
                                    if (viewIndex > mSelectedIndex && viewIndex <= mCurrentSelectedIndex) {
                                        myItemView.setIndex(viewIndex - 1);

                                        int [] xy=getXY(viewIndex-1);
                                        int [] xyl=getXY(viewIndex);

                                        animators.add(ObjectAnimator.ofFloat(myItemView,"translationX",0,xy[0]-xyl[0]));
                                        animators.add(ObjectAnimator.ofFloat(myItemView,"translationY",0,xy[1]-xyl[1]));

                                    }


                                }
                            } else if (mCurrentSelectedIndex < mSelectedIndex) { //往前面移动
                                for (int i = 0; i < adapter.getCount(); i++) {
                                    MyItemView myItemView = (MyItemView) getChildAt(i);
                                    int viewIndex = myItemView.getIndex();
                                    if (viewIndex >= mCurrentSelectedIndex && viewIndex < mSelectedIndex) {
                                        myItemView.setIndex(viewIndex + 1);

                                        int[] xy = getXY(viewIndex + 1);
                                        int[] xyl = getXY(viewIndex);

                                        log("translateX"+"=========="+ (xy[0] - xyl[0])+"====translationY======="+(xy[1] - xyl[1]));

                                        animators.add(ObjectAnimator.ofFloat(myItemView, "translationX", 0, xy[0] - xyl[0]));
                                        animators.add(ObjectAnimator.ofFloat(myItemView, "translationY", 0, xy[1] - xyl[1]));
                                    }
                                }
                            }

                            int[] xy = getXY(mCurrentSelectedIndex);
                            int[] xyl = getXY(mSelectedIndex);

                            animators.add(ObjectAnimator.ofFloat(mSelectedView, "translationX", 0, xy[0] - xyl[0]));
                            animators.add(ObjectAnimator.ofFloat(mSelectedView, "translationY", 0, xy[1] - xyl[1]));

                            ((MyItemView) mSelectedView).setIndex(mCurrentSelectedIndex);

                            AnimatorSet set=new AnimatorSet();
                            set.playTogether(animators);
                            set.setDuration(300);
                            set.setInterpolator(new DecelerateInterpolator());
                            set.addListener(new MyAnimationListener());
                            set.start();

                            mSelectedIndex=mCurrentSelectedIndex;
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                mLongPress=0;

                mDownX=0;
                mDownY=0;

                if(mCurrentMoveView!=null)
                {
                    removeView(mCurrentMoveView);
                }

                mCurrentMoveView=null;

                if(mSelectedView!=null)
                {
                    mSelectedView.setAlpha(1.0f);
                }

                break;
        }

        mlastX=x;
        mlastY=y;
        return true;
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

            int count=adapter.getCount();
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

                if(view!=mCurrentMoveView)
                {
                    view.setTranslationX(0);
                    view.setTranslationY(0);
                }

                view.layout(left,top,left+mViewWidth,top+mViewHeight);

                log("============onLayout=============");
            }

        }

    }

    private int[] getXY(int index)
    {
        int row=index/ROW_COUNT;
        int column=index%ROW_COUNT;


        int left=column*mViewWidth+(column+1)*mRowSpacing;
        int top=row*mViewHeight+(row+1)*mColumnSpacing;

        log("=========getXY========"+left+"=========="+top);

        return new int[]{left,top};
    }

    interface MyDragViewAdapter
    {
        int getCount();
        View getView(int i);
    }

    interface GestureLongPress
    {
        void onLongPress(int x,int y);
    }

    class MyAnimationListener implements Animator.AnimatorListener
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
            mIsBeingAnimation=true;

            log("=======onAnimationStart=========");
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            mIsBeingAnimation=false;
            requestLayout();

            log("=======onAnimationEnd=========");
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            mIsBeingAnimation=false;
            requestLayout();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private void log(String s)
    {
        Log.d(tag,tag+"========="+s);
    }

}
