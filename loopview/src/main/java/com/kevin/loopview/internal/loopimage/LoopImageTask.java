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

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

/**
 * LoopImageTask
 *
 * @author zwenkai@foxmail.com, Created on 2015-11-2 16:53:45
 *         Major Function：<b>LoopImageTask</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

public class LoopImageTask implements Runnable {
    private static final int BITMAP_READY = 0;

    private boolean cancelled = false;
    private OnCompleteHandler onCompleteHandler;
    private LoopImage image;
    private Context context;

    public static class OnCompleteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            onComplete(bitmap);
        }

        public void onComplete(Bitmap bitmap){};
    }

    public abstract static class OnCompleteListener {
        public abstract void onComplete();
        /***
         *  Convient method to get Bitmap after image is loaded.
         *  Override this method to get handle of bitmap
         *  Added overloaded implementation to make it backward compatible with previous versions
         */
        public void onComplete(Bitmap bitmap){
            onComplete();
        }
    }

    public LoopImageTask(Context context, LoopImage image) {
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