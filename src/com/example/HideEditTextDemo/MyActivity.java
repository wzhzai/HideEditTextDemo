package com.example.HideEditTextDemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.*;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private QuickReturnListView mListView;
//    private View mHeader;
    private View mQuickReturnView;
//    private View mPlaceHolder;

    private int mCachedVerticalScrollRange;
    private int mQuickReturnHeight;

    private static final int STATE_ONSCREEN = 0;
    private static final int STATE_OFFSCREEN = 1;
    private static final int STATE_RETURNING = 2;
    private int mState = STATE_ONSCREEN;
    private int mScrollY;
    private int mMinRawY = 0;

    private TranslateAnimation anim;
    private Context mContext = this;
    private String[] mContent;
    private int mListMarginTop;
    private int mListMarginOther;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mListMarginTop = getResources().getDimensionPixelSize(R.dimen.list_margin_top);
        mListMarginOther = getResources().getDimensionPixelSize(R.dimen.list_margin_other);
        mContent = new String[30];
        for (int i = 0; i < 30; i++) {
            mContent[i] = "这是item " + i;
        }
//        mListView = (QuickReturnListView) findViewById(R.id.list);
        mListView = (QuickReturnListView) findViewById(R.id.list_view);
//        mHeader = LayoutInflater.from(mContext).inflate(R.layout.quick_return_listview_header, null);
        mQuickReturnView =  findViewById(R.id.sticky);
//        mPlaceHolder = mHeader.findViewById(R.id.placeholder);

//        mQuickReturnView.setText("Default");
//        mListView.addHeaderView(mHeader);


        MyAdapter myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);


        mListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mQuickReturnHeight = mQuickReturnView.getHeight();
                        mListView.computeScrollY();
                        mCachedVerticalScrollRange = mListView.getListHeight();
                    }
                });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @SuppressLint("NewApi")
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                mScrollY = 0;
                int translationY = 0;

                if (mListView.scrollYIsComputed()) {
                    mScrollY = mListView.getComputedScrollY();
                }

                int rawY = 0 - Math.min(mCachedVerticalScrollRange - mListView.getHeight(), mScrollY);
                Log.e("wzz height", "range=" + mCachedVerticalScrollRange + "; listHeight=" + mListView.getHeight() + "; scrollY=" + mScrollY
                        + "; range-listHeight=" + (mCachedVerticalScrollRange - mListView.getHeight()) + "; rowY=" + rawY + "; state=" + mState);

                switch (mState) {
                    case STATE_OFFSCREEN:
                        if (rawY <= mMinRawY) {
                            mMinRawY = rawY;
                        } else {
                            mState = STATE_RETURNING;
                        }
                        translationY = rawY;
                        break;

                    case STATE_ONSCREEN:
                        if (rawY < -mQuickReturnHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                        translationY = rawY;
                        break;

                    case STATE_RETURNING:
                        translationY = (rawY - mMinRawY) - mQuickReturnHeight;
                        Log.e("wzz returning", "rawY=" + rawY + "; mMinRawY=" + mMinRawY + "; mQuickReturnHeight=" + mQuickReturnHeight + "; translationY=" + translationY);
                        if (translationY > 0) {
                            translationY = 0;
                            mMinRawY = rawY - mQuickReturnHeight;
                        }

                        if (rawY > 0) {
                            mState = STATE_ONSCREEN;
                            translationY = rawY;
                        }

                        if (translationY < -mQuickReturnHeight) {
                            mState = STATE_OFFSCREEN;
                            mMinRawY = rawY;
                        }
                        break;
                }

                /** this can be used if the build is below honeycomb **/
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
                    anim = new TranslateAnimation(0, 0, translationY, translationY);
                    anim.setFillAfter(true);
                    anim.setDuration(0);
                    mQuickReturnView.startAnimation(anim);
                } else {
                    mQuickReturnView.setTranslationY(translationY);
                    if (mState == STATE_ONSCREEN) {
                        Log.e("wzz margin", "margin top=" + mListMarginTop);
                        mListMarginTop += translationY;
                    }
//                    mListView.setTranslationY(translationY);
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });


    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mContent.length;
        }

        @Override
        public Object getItem(int position) {
            return mContent[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.text);
            tv.setText(mContent[position]);
            return convertView;
        }
    }
}
