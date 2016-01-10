package com.simonmeng.demo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.simonmeng.demo.R;

/**
 * Created by Administrator on 2016/1/10.
 */
public class RoundedCornersListView extends ListView{
    public RoundedCornersListView(Context context) {
        super(context);
    }

    public RoundedCornersListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoundedCornersListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int itemnum = pointToPosition(x, y);
                if (itemnum == AdapterView.INVALID_POSITION)
                    break;
                else {
                    if (itemnum == 0) {
                        if (itemnum == (getAdapter().getCount() - 1)) {
                            //只有一项
                            setSelector(R.drawable.listview_rounded_corners);
                        } else {
                            //第一项
                            setSelector(R.drawable.listview_rounded_corners_top);
                        }
                    } else if (itemnum == (getAdapter().getCount() - 1))
                        //最后一项
                        setSelector(R.drawable.listview_rounded_corners_bottom);
                    else {
                        //中间项
                        setSelector(R.drawable.listview_rounded_corners_center);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}

