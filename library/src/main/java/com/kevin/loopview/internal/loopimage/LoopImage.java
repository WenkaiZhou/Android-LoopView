package com.kevin.loopview.internal.loopimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by zhouwk on 2015/11/2 0002.
 */
public class LoopImage {

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;

    private static LoopImageCache imageCache;

    private String url;

    public LoopImage(String url) {
        this.url = url;
    }

    public Bitmap getBitmap(Context context) {
        if(imageCache == null) {
            imageCache = new LoopImageCache(context);
        }

        // 首先加载缓存中的图片
        Bitmap bitmap = null;
        if(url != null) {
            bitmap = imageCache.get(url);
            if(bitmap == null) {
                bitmap = getBitmapFromUrl(url);
                if(bitmap != null){
                    imageCache.put(url, bitmap);
                }
            }
        }

        return bitmap;
    }

    private Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;

        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            bitmap = BitmapFactory.decodeStream((InputStream) conn.getContent());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static void removeFromCache(String url) {
        if(imageCache != null) {
            imageCache.remove(url);
        }
    }
}
