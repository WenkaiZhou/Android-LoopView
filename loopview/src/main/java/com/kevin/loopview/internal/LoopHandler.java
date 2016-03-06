package com.kevin.loopview.internal;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 版权所有：XXX有限公司
 *
 * LoopHandler
 *
 * @author zhou.wenkai ,Created on 2015-11-2 17:58:21
 * Major Function：<b>LoopHandler</b>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
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

        switch (msg.what) {
            case 0:
                Activity activity = mActivity.get();
                BaseLoopView loopView = mLoopView.get();
                if (activity != null && loopView != null) {
                    if (!loopView.isAutoScroll()) return;
                    int change = (loopView.getDirection() == BaseLoopView.LEFT) ? -1 : 1;
                    loopView.getViewPager().setCurrentItem(loopView.getViewPager().getCurrentItem() + change, true);
                    loopView.sendScrollMessage(loopView.getInterval());
                } else {
                    removeMessages(0);
                    if(loopView != null) {
                        loopView.releaseResources();
                    }
                }
            default:
                break;
        }
    }

}