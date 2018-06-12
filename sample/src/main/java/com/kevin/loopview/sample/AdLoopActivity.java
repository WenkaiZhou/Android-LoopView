package com.kevin.loopview.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kevin.loopview.BannerView;
import com.kevin.loopview.internal.BaseLoopAdapter;
import com.kevin.loopview.internal.ImageLoader;
import com.kevin.loopview.internal.LoopData;
import com.kevin.loopview.sample.utils.LocalFileUtils;

/**
 * 版权所有：XXX有限公司</br>
 * <p>
 * AdLoopActivity </br>
 *
 * @author zhou.wenkai ,Created on 2015-10-20 14:32:13</br>
 * @author mender，Modified Date Modify Content:
 * @Description Major Function：<b>广告轮播控件的使用</b> </br>
 * <p>
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 */
public class AdLoopActivity extends Activity implements BaseLoopAdapter.OnItemClickListener {

    BannerView mBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adloopview);

        initViews();
        initEvents();
    }

    private void initViews() {
        mBannerView = findViewById(R.id.adloop_act_adloopview);
        mBannerView.setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, int placeholder) {
                Glide.with(imageView.getContext()).load(url).into(imageView);
            }
        });
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
        LoopData loopData = new Gson().fromJson(json, LoopData.class);

        if (null != loopData) {
            mBannerView.refreshData(loopData);
        }
        // 设置页面切换过度事件
        mBannerView.setScrollDuration(2000);
        // 设置页面切换时间间隔
        mBannerView.setInterval(3000);
    }

    /**
     * 初始化事件
     *
     * @return void
     * @date 2015-10-20 14:05:47
     */
    private void initEvents() {
        mBannerView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, LoopData.ItemData itemData, int position) {
        LoopData loopData = mBannerView.getData();
        String url = loopData.items.get(position).link;

        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }

}
