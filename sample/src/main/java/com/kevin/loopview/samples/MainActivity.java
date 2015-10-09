package com.kevin.loopview.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kevin.loopview.AdLoopView;
import com.kevin.loopview.internal.LoopData;
import com.kevin.loopview.samples.utils.LocalFileUtils;
import com.kevin.loopview.utils.JsonTool;

public class MainActivity extends Activity {

    AdLoopView mLoopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mLoopView = (AdLoopView) this.findViewById(R.id.main_act_adloopview);
        initRotateView();
    }

    /**
     * 初始化LoopView
     *
     * @return void
     * @date 2015-10-9 21:32:12
     */
    private void initRotateView() {
        String json = LocalFileUtils.getStringFormAsset(this, "loopview_date.json");
        LoopData loopData = JsonTool.toBean(json, LoopData.class);
        if(null != loopData) {
            mLoopView.refreshDatas(loopData);
            mLoopView.startAutoLoop();
        }
    }
}
