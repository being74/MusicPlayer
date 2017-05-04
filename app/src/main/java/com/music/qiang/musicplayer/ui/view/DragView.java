package com.music.qiang.musicplayer.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by user on 2017/4/26.
 */
public class DragView extends ImageView {

    private int lastX, lastY;

    public DragView(Context context) {
        super(context);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initData() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();
        Log.d("xuqiang", "~~~~~~~~~~x = " + x + " y = " + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                Log.d("xuqiang", "-----------lastX = " + lastX + " lastY = " + lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离
                int offX = x - lastX;
                int offY = y - lastY;
                Log.d("xuqiang", "offX = " + offX + " offY = " + offY);
                Log.d("xuqiang", "getLeft() = " + getLeft() + " getTop() = " + getTop() + " getRight() = " + getRight() + " getBottom() = " + getBottom());
                //调用layout方法来重新放置它的位置  ，其他的和第6个方法是一样的，只是下面调用的这行代码不一样的罢了。
                layout(getLeft() + offX, getTop() + offY, getRight() + offX, getBottom() + offY);
                break;
        }
        return true;
    }
}
