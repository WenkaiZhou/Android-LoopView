package com.kevin.loopview.internal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * 版权所有：XXX有限公司</br>
 *
 * LoopImageView </br>
 *
 * @author zhou.wenkai ,Created on 2015-1-18 16:02:17</br>
 * @Description Major Function：<b>可以加载网络图片,并带有二级缓存的ImageView</b> </br>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 * @author mender，Modified Date Modify Content:
 */
public class LoopImageView extends ImageView {
	
	/** 缓存路径 */
	private static final String DISK_CACHE_PATH = "/loopview_cache/";
	/** 连接超时时间 */
    private static final int CONNECT_TIMEOUT 	= 5000;
    /** 读取时间超时 */
    private static final int READ_TIMEOUT    	= 10000;
	/** 线程池最大线程数 */
    private static final int LOADING_THREADS 	= 4;
    private static ExecutorService threadPool 	= Executors.newFixedThreadPool(LOADING_THREADS);
    
    private static ImageCache imageCache;
    
    private ImageTask currentTask;

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
        setImage(new SmartImage(url), null, null);
    }
    
    public void setImageUrl(String url, final Integer loadingResource) {
        setImage(new SmartImage(url), loadingResource, null);
    }

    public void setImageUrl(String url, final Integer loadingResource, final Integer fallbackResource) {
        setImage(new SmartImage(url), loadingResource, fallbackResource);
    }

    public void setImage(final SmartImage image, final Integer loadingResource, final Integer fallbackResource) {
        // 设置加载中显示的图片
        if(loadingResource != null){
            setImageResource(loadingResource);
        }
        // 如果存在图片加载线程则关闭
        if(currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }
        // 创建一个新的图片加载线程
        currentTask = new ImageTask(getContext(), image);
        currentTask.setOnCompleteHandler(currentTask.new OnCompleteHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if(bitmap != null) {
                    setImageBitmap(bitmap);
                } else {
                    // Set fallback resource
                    if(fallbackResource != null) {
                        setImageResource(fallbackResource);
                    }
                }
            }
        });
        // 开启执行线程池
        threadPool.execute(currentTask);
    }
    
    public class ImageTask implements Runnable {
        private static final int BITMAP_READY = 0;

        private boolean cancelled = false;
        private OnCompleteHandler onCompleteHandler;
        private SmartImage image;
        private Context context;

        @SuppressLint("HandlerLeak")
		public class OnCompleteHandler extends Handler {
        	
            @Override
            public void handleMessage(Message msg) {
                Bitmap bitmap = (Bitmap)msg.obj;
                onComplete(bitmap);
            }

            public void onComplete(Bitmap bitmap){
            };
        }

        public ImageTask(Context context, SmartImage image) {
            this.image = image;
            this.context = context;
        }

        @Override
        public void run() {
            if(image != null) {
                complete(image.getBitmap(context));
                context = null;
            }
        }

        public void setOnCompleteHandler(OnCompleteHandler handler){
            this.onCompleteHandler = handler;
        }

        public void cancel() {
            cancelled = true;
        }

        public void complete(Bitmap bitmap){
            if(onCompleteHandler != null && !cancelled) {
                onCompleteHandler.sendMessage(onCompleteHandler.obtainMessage(BITMAP_READY, bitmap));
            }
        }
    }
    
    public class SmartImage {
    	
        private String url;

        public SmartImage(String url) {
            this.url = url;
        }

        public Bitmap getBitmap(Context context) {
            if(imageCache == null) {
            	imageCache = new ImageCache(context);
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

        public void removeFromCache(String url) {
            if(imageCache != null) {
                imageCache.remove(url);
            }
        }
    }
    
    public class ImageCache {

        private ConcurrentHashMap<String, SoftReference<Bitmap>> memoryCache;
        private String diskCachePath;
        private boolean diskCacheEnabled = false;
        private ExecutorService writeThread;

        public ImageCache(Context context) {
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
                            bitmap.compress(CompressFormat.PNG, 100, ostream);
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

}
