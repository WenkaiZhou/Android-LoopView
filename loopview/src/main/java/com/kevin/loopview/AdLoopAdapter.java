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
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kevin.loopview.internal.BaseLoopAdapter;
import com.kevin.loopview.internal.LoopData;
import com.kevin.loopview.internal.loopimage.LoopImageView;

import java.lang.reflect.Method;

/**
 * AdLoopAdapter
 *
 * @author zwenkai@foxmail.com, Created on 2015-5-19 22:28:29
 *         Major Function：<b>一般数据适配器</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

public class AdLoopAdapter extends BaseLoopAdapter {

    public AdLoopAdapter(Context context, LoopData loopData,
                         ViewPager viewPager) {
        super(context, loopData, viewPager);
    }

    /**
     * 控件操作
     *
     * @param imageUrl
     */
    public View instantiateItemView(String imageUrl, int position) {
        LoopImageView mImageView = new LoopImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        if (!TextUtils.isEmpty(imageUrl)) {
            // 优先选用Picasso加载图片
            try {
                Class clazz = Class.forName("com.squareup.picasso.Picasso");
                Method with = clazz.getMethod("with", Context.class);
                Object obj = with.invoke(null, mContext);
                Method load = obj.getClass().getMethod("load", String.class);
                obj = load.invoke(obj, imageUrl);
                if (defaultImgId != 0) {
                    Method error = obj.getClass().getMethod("error", int.class);
                    obj = error.invoke(obj, defaultImgId);
                }
                Method into = obj.getClass().getMethod("into", ImageView.class);
                into.invoke(obj, mImageView);
            } catch (Exception e) {
                // 选用Glide加载图片
                try {
                    Class clazz = Class.forName("com.bumptech.glide.Glide");
                    Method with = clazz.getMethod("with", Context.class);
                    Object obj = with.invoke(null, mContext);
                    Method load = obj.getClass().getMethod("load", String.class);
                    obj = load.invoke(obj, imageUrl);
                    if (defaultImgId != 0) {
                        Method error = obj.getClass().getMethod("error", int.class);
                        obj = error.invoke(obj, defaultImgId);
                    }
                    Method into = obj.getClass().getMethod("into", ImageView.class);
                    into.invoke(obj, mImageView);
                } catch (Exception e1) {
                    mImageView.setImageUrl(imageUrl, defaultImgId, defaultImgId);
                }
            }
        }
        return mImageView;
    }

}