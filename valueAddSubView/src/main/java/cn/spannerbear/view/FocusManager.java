package cn.spannerbear.view;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * @author SpannerBear
 * @time 2016/11/9 0009  16:23
 * @desc 焦点的辅助类
 *
 */
public class FocusManager {
    private final Activity mContext;
    private View mRootView;
    private FocusTouchListener mTouchListener;
    
    /**
     * 获取实例
     * * @param context   .
     *
     * @param rootView 想要在点击时获取焦点的View,推荐填入根布局
     * @param views    其他想要点击时获取焦点的view
     * @return 实例
     */
    public static FocusManager getInstance(Activity context, View rootView, View... views) {
        return new FocusManager(context, rootView, views);
    }
    
    private FocusManager(final Activity context, View rootView, View... views) {
        mContext = context;
        mRootView = rootView;
        setTouch(mRootView);
        for (View view : views) {
            setTouch(view);
        }
    }
    
    private void setTouch(View view) {
        if (mTouchListener == null)
            mTouchListener = new FocusTouchListener();
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(mTouchListener);
    }
    
    private class FocusTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            KeyboardUtils.hideSoftInput(mContext);
            View currentFocus = mContext.getCurrentFocus();
            //处理列表中的控件需要点击两次才生效的问题
            if (currentFocus != v && v.isClickable() && event.getAction() == MotionEvent.ACTION_DOWN) {
                v.requestFocus();
                return false;
            }
            
            if (currentFocus instanceof EditText) {
                mRootView.requestFocus();
            }
            return false;
        }
    }
}
