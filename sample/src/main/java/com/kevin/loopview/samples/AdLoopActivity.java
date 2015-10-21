package com.kevin.loopview.samples;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.kevin.loopview.AdLoopView;
import com.kevin.loopview.internal.BaseLoopAdapter;
import com.kevin.loopview.internal.LoopData;
import com.kevin.loopview.samples.utils.LocalFileUtils;
import com.kevin.loopview.utils.JsonTool;

/**
 * 版权所有：XXX有限公司</br>
 *
 * AdLoopActivity </br>
 *
 * @author zhou.wenkai ,Created on 2015-10-20 14:32:13</br>
 * @Description Major Function：<b>广告轮播控件的使用</b> </br>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 * @author mender，Modified Date Modify Content:
 */
public class AdLoopActivity extends Activity implements BaseLoopAdapter.OnItemClickListener{

    AdLoopView mLoopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adloopview);

        initViews();
        initEvents();
    }

    private void initViews() {
        mLoopView = (AdLoopView) this.findViewById(R.id.adloop_act_adloopview);
        initRotateView();
    }

    /**
     * 初始化LoopView
     *
     * @return void
     * @date 2015-10-9 21:32:12
     */
    private void initRotateView() {
        // 设置自定义布局
//        mLoopView.setLoopLayout(R.layout.ad_loopview_layout);
        // 设置数据
        String json = LocalFileUtils.getStringFormAsset(this, "loopview_date.json");
        LoopData loopData = JsonTool.toBean(json, LoopData.class);
        if(null != loopData) {
            mLoopView.refreshData(loopData);
        }
        // 设置页面切换过度事件
        mLoopView.setScrollDuration(2000);
        // 设置页面切换时间间隔
        mLoopView.setInterval(3000);

    }

    /**
     * 初始化事件
     *
     * @return void
     * @date 2015-10-20 14:05:47
     */
    private void initEvents() {
        mLoopView.setOnClickListener(this);
    }


    @Override
    public void onItemClick(PagerAdapter parent, View view, int position, int realPosition) {
        LoopData loopData = mLoopView.getLoopData();
        String url = loopData.items.get(position).link;

        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }
}
