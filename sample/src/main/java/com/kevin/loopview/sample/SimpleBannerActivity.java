package com.kevin.loopview.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kevin.loopview.BannerView;
import com.kevin.loopview.internal.BaseLoopAdapter;
import com.kevin.loopview.internal.BaseLoopView;
import com.kevin.loopview.internal.ImageLoader;
import com.kevin.loopview.internal.LoopData;
import com.kevin.loopview.sample.utils.LocalFileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版权所有：XXX有限公司</br>
 * <p>
 * SimpleBannerActivity </br>
 *
 * @author zhou.wenkai ,Created on 2015-10-20 14:32:13</br>
 * @author mender，Modified Date Modify Content:
 * @Description Major Function：<b>简单广告轮播控件的使用</b> </br>
 * <p>
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 */
public class SimpleBannerActivity extends Activity {

    BannerView mBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_banner);

        initViews();
        initEvents();
    }

    private void initViews() {
        mBannerView = findViewById(R.id.main_act_banner);
        mBannerView.setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, int placeholder) {
                Glide.with(imageView.getContext()).load(url).into(imageView);
            }
        });
        initLoopView();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SimpleBannerActivity.this, AdLoopActivity.class));
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

        List<Map<String, String>> dataList = new ArrayList<>();
        for (LoopData.ItemData itemData : loopData.items) {
            Map<String, String> map = new HashMap<>();
            map.put("img", itemData.img);
            map.put("desc", itemData.desc);
            map.put("link", itemData.link);
            dataList.add(map);
        }

        mBannerView.setData(dataList);
        mBannerView.startAutoLoop();
    }

    /**
     * 初始化事件
     *
     * @return void
     * @date 2015-10-20 14:05:47
     */
    private void initEvents() {
        mBannerView.setOnItemClickListener(new BaseLoopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, LoopData.ItemData itemData, int position) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(itemData.link));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });

        mBannerView.setOnLoopListener(new BaseLoopView.OnLoopListener() {

            @Override
            public void onLoopToStart(int position, int realPosition) {

            }

            @Override
            public void onLoopToNext(int position, int realPosition) {
                Toast.makeText(SimpleBannerActivity.this, "onLoopToNext = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoopToEnd(int position, int realPosition) {

            }
        });
    }

}
