package com.kermit.scrollpopupview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Kermit on 15-9-16.
 * e-mail : wk19951231@163.com
 */
public class ScrollPopupView extends ScrollView {

    private OnScrollChangedListener mListener;

    public interface OnScrollChangedListener{
        void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener){
        this.mListener = listener;
    }

    public ScrollPopupView(Context context) {
        super(context);
    }

    public ScrollPopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollPopupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null){
            mListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
