package com.example.HideEditTextDemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by WANGZHENGZE on 2014/12/22.
 */
public class PushDismissListView extends ListView {

    private View mEditMainView;

    private int mMainHeight;

    private OnScrollListener mOnScrollListener;

    private float mTouchY;

    private GestureDetector mGestureDetector;

    public void setEditMainView(View editMainView) {
        mEditMainView = editMainView;
    }

    public void setMainHeight(int mainHeight) {
        mMainHeight = mainHeight;
    }

    public PushDismissListView(Context context) {
        super(context);
        init();
    }

    public PushDismissListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PushDismissListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return super.onDoubleTapEvent(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                Log.e("wzz", "down!!!!");
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.e("wzz", "Scrolling!!!!");
//                int height = mEditMainView.getHeight();
//                if (height > 0) {
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mEditMainView.getLayoutParams();
//                    int changedHeight = (int) (height - distanceY);
//                    if (changedHeight <= 0 ) {
//                        changedHeight = 0;
//                    }
//                    params.height = changedHeight;
//                    mEditMainView.setLayoutParams(params);
//                }
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }
        });

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (mEditMainView != null) {
//                    mMainHeight = mEditMainView.getHeight();
//                }
//                if (mMainHeight != 0) {
//                    setSelection(0);
//                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int height = mEditMainView.getHeight();
                if (height > 0) {
                    float deltaY = ev.getRawY() - mTouchY;
                    mTouchY = ev.getRawY();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mEditMainView.getLayoutParams();
                    int changedHeight = (int) (height + deltaY);
                    if (changedHeight <= 0 ) {
                        changedHeight = 0;
                    }
                    params.height = changedHeight;
                    mEditMainView.setLayoutParams(params);
                    return true;
                }
        }
        return super.onTouchEvent(ev);
    }
}
