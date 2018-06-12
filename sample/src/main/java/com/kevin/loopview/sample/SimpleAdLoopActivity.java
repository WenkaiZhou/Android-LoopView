package com.kevin.loopview.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.kevin.loopview.AdLoopView;
import com.kevin.loopview.internal.BaseLoopAdapter;
import com.kevin.loopview.internal.LoopData;
import com.kevin.loopview.sample.utils.LocalFileUtils;

/**
 * 版权所有：XXX有限公司</br>
 *
 * SimpleAdLoopActivity </br>
 *
 * @author zhou.wenkai ,Created on 2015-10-20 14:32:13</br>
 * @Description Major Function：<b>简单广告轮播控件的使用</b> </br>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 * @author mender，Modified Date Modify Content:
 */
public class SimpleAdLoopActivity extends Activity implements BaseLoopAdapter.OnItemClickListener{

    AdLoopView mLoopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_adloopview);

        initViews();
        initEvents();
    }

    private void initViews() {
        mLoopView = findViewById(R.id.main_act_adloopview);
        initLoopView();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SimpleAdLoopActivity.this, AdLoopActivity.class));
            }
        });
    }

    /**
     * 初始化LoopView
     *
     * @return void
     * @date 2015-10-9 21:32:12
     */
    private void initLoopView() {
        String json = LocalFileUtils.getStringFormAsset(this, "loopview_date.json");
        LoopData loopData = new Gson().fromJson(json, LoopData.class);
        if(null != loopData) {
            mLoopView.refreshData(loopData);
            mLoopView.startAutoLoop();
        }
    }

    /**
     * 初始化事件
     *
     * @return void
     * @date 2015-10-20 14:05:47
     */
    private void initEvents() {
        mLoopView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(View view, int position, int realPosition) {
        LoopData loopData = mLoopView.getLoopData();
        String url = loopData.items.get(position).link;

        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }
}
