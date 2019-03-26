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

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * LoopHandler
 *
 * @author zwenkai@foxmail.com, Created on 2015-11-2 17:58:21
 * Major Function：<b>LoopHandler</b>
 * <p/>
 * Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

public class LoopHandler extends Handler {

    private final WeakReference<Activity> mActivity;
    private final WeakReference<BaseLoopView> mLoopView;

    public LoopHandler(BaseLoopView loopView, Activity activity) {
        this.mLoopView = new WeakReference<>(loopView);
        this.mActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        Activity activity = mActivity.get();
        BaseLoopView loopView = mLoopView.get();
        if (activity != null && loopView != null) {
            if (!loopView.isAutoScroll()) {
                return;
            }

            switch (msg.what) {
                case 0: // 自动跳转
                    int change = (loopView.getDirection() == BaseLoopView.LEFT) ? -1 : 1;
                    loopView.getViewPager().setCurrentItem(loopView.getViewPager().getCurrentItem() + change, true);
                    loopView.sendScrollMessage(loopView.getInterval());
                    break;
                case 1: // 由不可见到可见跳转一次
                    loopView.getViewPager().setCurrentItem(loopView.getViewPager().getCurrentItem() - 1, false);
                    loopView.getViewPager().setCurrentItem(loopView.getViewPager().getCurrentItem() + 1, false);
                    loopView.sendScrollMessage(loopView.getInterval());
                    break;
                default:
                    // :Can't reach;
                    break;
            }

        } else {
            removeMessages(0);
            removeMessages(1);
            if (loopView != null) {
                loopView.releaseResources();
            }
        }
    }

}