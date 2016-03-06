package com.kevin.loopview.internal.loopimage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * 版权所有：XXX有限公司
 *
 * LoopImageView
 *
 * @author zhou.wenkai ,Created on 2015-1-18 16:02:17
 * Major Function：<b>可以加载网络图片,并带有二级缓存的ImageView</b>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
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