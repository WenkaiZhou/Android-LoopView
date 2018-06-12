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
package com.kevin.loopview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kevin.loopview.internal.BaseLoopAdapter;
import com.kevin.loopview.internal.LoopData;

/**
 * BannerAdapter
 *
 * @author zwenkai@foxmail.com, Created on 2015-5-19 22:28:29
 *         Major Function：<b>一般数据适配器</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

public class BannerAdapter extends BaseLoopAdapter {

    public BannerAdapter(Context context, LoopData loopData,
                         ViewPager viewPager) {
        super(context, loopData, viewPager);
    }

    /**
     * 控件操作
     *
     * @param imageUrl
     */
    public View instantiateItemView(String imageUrl, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        mImageLoader.loadImage(imageView, imageUrl, mPlaceholderId);
        return imageView;
    }

}