package com.kevin.loopview.internal.loopimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhouwk on 2015/11/2 0002.
 */
public class LoopImageCache {

    /** 缓存路径 */
    private static final String DISK_CACHE_PATH = "/loopview_cache/";

    private ConcurrentHashMap<String, SoftReference<Bitmap>> memoryCache;
    private String diskCachePath;
    private boolean diskCacheEnabled = false;
    private ExecutorService writeThread;

    public LoopImageCache(Context context) {
        // 设置内存缓存
        memoryCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

        // 设置SDCard缓存
        Context appContext = context.getApplicationContext();
        diskCachePath = appContext.getCacheDir().getAbsolutePath() + DISK_CACHE_PATH;

        File outFile = new File(diskCachePath);
        outFile.mkdirs();

        diskCacheEnabled = outFile.exists();

        // 设置一个线程来读取缓存图片
        writeThread = Executors.newSingleThreadExecutor();
    }

    public Bitmap get(final String url) {
        Bitmap bitmap = null;

        // 在内存中获取图片
        bitmap = getBitmapFromMemory(url);

        // 在SDCard上获取图片
        if(bitmap == null) {
            bitmap = getBitmapFromDisk(url);

            // 将图片写入内存缓存
            if(bitmap != null) {
                cacheBitmapToMemory(url, bitmap);
            }
        }

        return bitmap;
    }

    public void put(String url, Bitmap bitmap) {
        cacheBitmapToMemory(url, bitmap);
        cacheBitmapToDisk(url, bitmap);
    }

    public void remove(String url) {
        if(url == null){
            return;
        }

        // 在内存中移除指定地址的图片
        memoryCache.remove(getCacheKey(url));

        // 在SDCard上移除指定地址的图片
        File f = new File(diskCachePath, getCacheKey(url));
        if(f.exists() && f.isFile()) {
            f.delete();
        }
    }

    public void clear() {
        // 在内存中移除所有图片
        memoryCache.clear();

        // 在SDCard上移所有图片
        File cachedFileDir = new File(diskCachePath);
        if(cachedFileDir.exists() && cachedFileDir.isDirectory()) {
            File[] cachedFiles = cachedFileDir.listFiles();
            for(File f : cachedFiles) {
                if(f.exists() && f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    private void cacheBitmapToMemory(final String url, final Bitmap bitmap) {
        memoryCache.put(getCacheKey(url), new SoftReference<Bitmap>(bitmap));
    }

    private void cacheBitmapToDisk(final String url, final Bitmap bitmap) {
        writeThread.execute(new Runnable() {
            @Override
            public void run() {
                if(diskCacheEnabled) {
                    BufferedOutputStream ostream = null;
                    try {
                        ostream = new BufferedOutputStream(new FileOutputStream(new File(diskCachePath, getCacheKey(url))), 2*1024);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(ostream != null) {
                                ostream.flush();
                                ostream.close();
                            }
                        } catch (IOException e) {}
                    }
                }
            }
        });
    }

    private Bitmap getBitmapFromMemory(String url) {
        Bitmap bitmap = null;
        SoftReference<Bitmap> softRef = memoryCache.get(getCacheKey(url));
        if(softRef != null){
            bitmap = softRef.get();
        }

        return bitmap;
    }

    private Bitmap getBitmapFromDisk(String url) {
        Bitmap bitmap = null;
        if(diskCacheEnabled){
            String filePath = getFilePath(url);
            File file = new File(filePath);
            if(file.exists()) {
                bitmap = BitmapFactory.decodeFile(filePath);
            }
        }
        return bitmap;
    }

    private String getFilePath(String url) {
        return diskCachePath + getCacheKey(url);
    }

    private String getCacheKey(String url) {
        if(url == null){
            throw new RuntimeException("Null url passed in");
        } else {
            return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
        }
    }
}