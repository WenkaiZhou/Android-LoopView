package com.kevin.loopview.internal;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 版权所有：XXX有限公司
 *
 * LoopViewScroller
 *
 * @author zhou.wenkai ,Created on 2015-1-18 16:02:17
 * Major Function：<b>viewPager跳转控制器</b>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 * @author mender，Modified Date Modify Content:
 */
public class LoopViewScroller extends Scroller{

    /** 滑动速度 */
    private long mScrollDuration = 1500;

    /**
     * 设置滑动速度
     * @param duration
     */
    public void setScrollDuration(long duration){
        this.mScrollDuration = duration;
    }

    public LoopViewScroller(Context context) {
        super(context);
    }

    public LoopViewScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @SuppressLint("NewApi")
    public LoopViewScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int)mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, (int)mScrollDuration);
    }

    public void initViewPagerScroll(ViewPager viewPager) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}