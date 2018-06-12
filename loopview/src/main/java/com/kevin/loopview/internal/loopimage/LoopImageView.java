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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * LoopImageView
 *
 * @author zwenkai@foxmail.com, Created on 2015-1-18 16:02:17
 *         Major Function：<b>可以加载网络图片,并带有二级缓存的ImageView</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

public class LoopImageView extends ImageView {

    /** 线程池最大线程数 */
    private static final int LOADING_THREADS 	= 4;
    private static ExecutorService threadPool 	= Executors.newFixedThreadPool(LOADING_THREADS);

    private LoopImageTask currentTask;

    public LoopImageView(Context context) {
        super(context);
    }

    public LoopImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoopImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(String url) {
        setImage(new LoopImage(this, url), null, null);
    }

    public void setImageUrl(String url, final Integer loadingResource) {
        setImage(new LoopImage(this, url), loadingResource, null);
    }

    public void setImageUrl(String url, final Integer loadingResource, final Integer fallbackResource) {
        setImage(new LoopImage(this, url), loadingResource, fallbackResource);
    }

    public void setImage(final LoopImage image, final Integer loadingResource, final Integer fallbackResource) {
        // 设置下载的时候显示的图片
        if(loadingResource != null && loadingResource != 0){
            setImageResource(loadingResource);
        }

        // 停止正在为该SmartImageView下载网络图片的线程
        if(currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }

        // 创建一个新的线程
        currentTask = new LoopImageTask(getContext(), image); // SmartImageTask实现Runnable方法
        currentTask.setOnCompleteHandler(new LoopImageTask.OnCompleteHandler() {

            @Override
            public void onComplete(Bitmap bitmap) {
                if(bitmap != null) {
                    setImageBitmap(bitmap);
                } else {
                    // 如果下载失败,责显示预设的加载失败时显示的图片
                    if(fallbackResource != null && fallbackResource != 0) {
                        setImageResource(fallbackResource);
                    }
                }
            }
        });

        // 将该下载图片的线程添加到线程池中并执行
        threadPool.execute(currentTask);
    }

    public static ExecutorService getThreadPool() {
        return threadPool;
    }
}