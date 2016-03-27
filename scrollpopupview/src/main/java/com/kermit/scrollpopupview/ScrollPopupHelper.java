package com.kermit.scrollpopupview;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

/**
 * Created by Kermit on 15-9-16.
 * e-mail : wk19951231@163.com
 */
public class ScrollPopupHelper {

    private static final String TAG = "ScrollPopupHelper";

    private static final int SCROLL_TO_TOP = - 1;

    private static final int SCROLL_TO_BOTTOM = 1;

    private static final int SCROLL_DIRECTION_CHANGE_THRESHOLD = 5;

    private Activity mActivity;

    private LayoutInflater mInflater;

    private View popupView;

    private int mScrollDirection = 0;

    private int mPoppyViewHeight = - 1;

    private Position popupViewPosition;

    private boolean hasWrapper;

    public ScrollPopupHelper(Activity activity, Position popupViewPosition){
        this.mActivity = activity;
        this.popupViewPosition = popupViewPosition;
        mInflater = LayoutInflater.from(activity);
    }

    public ScrollPopupHelper(Activity activity){
        this(activity, Position.BOTTOM);
    }

    //scrollview
    public View createOnScrollView(ScrollPopupView scrollView, @LayoutRes int layoutId){
        popupView = mInflater.inflate(layoutId, null);
        initPopupViewOnScrollView(scrollView);
        return popupView;
    }

    private void initPopupViewOnScrollView(ScrollPopupView scrollView) {
        setPopupOnView(scrollView);
        scrollView.setOnScrollChangedListener(new ScrollPopupView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt) {
                onScrollPositionChanged(oldt, t);
            }
        });
    }


    //recyclerview
    public View createOnRecyclerView(RecyclerView recyclerView, @LayoutRes int layoutId){
        popupView = mInflater.inflate(layoutId, null);
        initPopupViewOnRecyclerView(recyclerView);
        return popupView;
    }

    private void initPopupViewOnRecyclerView(RecyclerView recyclerView) {
        setPopupOnView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                onScrollPositionChanged(0, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public ScrollPopupHelper hasWrapper(boolean hasWrapper){
        this.hasWrapper = hasWrapper;
        return this;
    }

    private void setPopupOnView(View view) {
        int index;
        ViewGroup viewGroup;
        ViewGroup.LayoutParams params;
        View tempView = view;

        //如果recyclerview外层还有一层需要放入(SwipeRefreshLayout)

        if (hasWrapper){
            viewGroup = (ViewGroup) view.getParent().getParent();
            tempView = (View) view.getParent();
            params = tempView.getLayoutParams();
            index = viewGroup.indexOfChild(tempView);
        }else {
            viewGroup = (ViewGroup) view.getParent();
            params = view.getLayoutParams();
            index = viewGroup.indexOfChild(tempView);
        }

        FrameLayout container = new FrameLayout(mActivity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = popupViewPosition == Position.BOTTOM ? Gravity.BOTTOM : Gravity.TOP;
        viewGroup.removeView(tempView);
        viewGroup.addView(container, index, params);
        container.addView(tempView);
        container.addView(popupView, layoutParams);
    }

    private void onScrollPositionChanged(int oldPosition, int newPosition){
        if (oldPosition < newPosition){
            newPosition = SCROLL_TO_TOP;
        }else{
            newPosition = SCROLL_TO_BOTTOM;
        }

        if (newPosition != mScrollDirection){
            mScrollDirection = newPosition;
            translateScrollPopupView();
        }
    }


    public void translateScrollPopupView(){
        popupView.animate()
                .translationY(getTranslateY())
                .setDuration(500);
        Log.e(TAG, "translateScrollPopupView: delta Y: " + getTranslateY());
    }

    private int getTranslateY(){

        int translateY = 0;
        if (mPoppyViewHeight < 0){
            mPoppyViewHeight = popupView.getMeasuredHeight();
        }
        Log.e(TAG, "getTranslateY: popupViewPosition: " + mPoppyViewHeight);
        switch (popupViewPosition){
            case BOTTOM:
                translateY = mScrollDirection == SCROLL_TO_TOP ? mPoppyViewHeight : 0;
                break;
            case TOP:
                translateY = mScrollDirection == SCROLL_TO_TOP? 0 : -mPoppyViewHeight;
                break;
        }

        return translateY;
    }

}
