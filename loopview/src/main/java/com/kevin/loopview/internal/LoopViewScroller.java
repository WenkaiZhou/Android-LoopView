/*
 * Copyright (c) 2018 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevin.loopview.internal;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * LoopViewScroller
 *
 * @author zwenkai@foxmail.com, Created on 2015-1-18 16:02:17
 *         Major Function：<b>viewPager跳转控制器</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

public class LoopViewScroller extends Scroller {

    /**
     * 滑动速度
     */
    private long mScrollDuration = 1500;

    /**
     * 设置滑动速度
     *
     * @param duration
     */
    public void setScrollDuration(long duration) {
        this.mScrollDuration = duration;
    }

    public LoopViewScroller(Context context) {
        super(context);
    }

    public LoopViewScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int) mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, (int) mScrollDuration);
    }

    public void initViewPagerScroll(ViewPager viewPager) {
        if (viewPager == null) {
            return;
        }
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}