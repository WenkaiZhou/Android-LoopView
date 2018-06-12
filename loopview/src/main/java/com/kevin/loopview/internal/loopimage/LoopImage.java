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
package com.kevin.loopview.internal.loopimage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * LoopImage
 *
 * @author zwenkai@foxmail.com, Created on 2015-11-2 17:21:56
 *         Major Function：<b>LoopImage</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */


public class LoopImage {

    /**
     * 连接超时时间
     */
    private static final int CONNECT_TIMEOUT = 5000;
    /**
     * 读取时间超时
     */
    private static final int READ_TIMEOUT = 10000;
    private LoopImageView imageView;

    private LoopImageCache imageCache;
    private String url;

    public LoopImage(LoopImageView imageView, String url) {
        this.imageView = imageView;
        this.url = url;
    }

    public Bitmap getBitmap(Context context) {
        if (imageCache == null) {
            imageCache = new LoopImageCache(context);
        }

        // 首先加载缓存中的图片
        Bitmap bitmap = null;
        if (url != null) {
            bitmap = imageCache.get(url);
            if (bitmap == null) {
                bitmap = getBitmapFromUrl(url);
                if (bitmap != null) {
                    imageCache.put(url, bitmap);
                }
            }
        }

        return bitmap;
    }

    private Bitmap getBitmapFromUrl(final String url) {
        Bitmap bitmap = null;

        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            InputStream is = (InputStream) conn.getContent();
            // 图片适配View
            LoopImageUtils.ImageSize actualImageSize = LoopImageUtils.getImageSize(is);
            LoopImageUtils.ImageSize imageViewSize = LoopImageUtils.getImageViewSize(imageView);
            int inSampleSize = LoopImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
            try {
                is.reset();
            } catch (IOException e) {
                conn = new URL(url).openConnection();
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                is = (InputStream) conn.getContent();
            }
            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inJustDecodeBounds = false;
            ops.inSampleSize = inSampleSize;
            bitmap = BitmapFactory.decodeStream(is, null, ops);
        } catch (Exception e) {
            if(imageView.getContext() instanceof Activity) {
                regetBitmap();
            }
        }
        return bitmap;
    }

    /**
     * 如果图片下载失败, 3秒之后再次访问
     */
    private void regetBitmap() {

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    final Bitmap bitmap = getBitmapFromUrl(url);
                    if (bitmap != null) {

                        ((Activity) imageView.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                        imageCache.put(url, bitmap);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void removeFromCache(String url) {
        if (imageCache != null) {
            imageCache.remove(url);
        }
    }
}