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
package com.kevin.loopview.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * RecyclerBitmap
 *
 * @author zwenkai@foxmail.com, Created on 2015-1-20 19:27:44
 *         Major Function：<b>The helper of recycler bitmap of ImageView</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

public class RecyclerBitmap {

    /**
     * 是否释放背景图
     * true:释放;
     * false:不释放
     */
    private boolean flagWithBackgroud = false;

    /**
     * @param flagWithBackgroud 是否同时释放背景图
     */
    public RecyclerBitmap(boolean flagWithBackgroud) {
        this.flagWithBackgroud = flagWithBackgroud;
    }

    /**
     * 释放Imageview占用的图片资源
     * 用于退出时释放资源，调用完成后，请不要刷新界面
     *
     * @param layout 需要释放图片的布局
     */
    public void recycle(View layout) {
        if (layout instanceof ViewGroup) {
            recycleViewGroup((ViewGroup) layout);
        } else if (layout instanceof ImageView) {
            recycleImageView((ImageView) layout);
        }
    }

    public void recycleViewGroup(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            //获得该布局的所有子布局
            View subView = layout.getChildAt(i);
            //判断子布局属性，如果还是ViewGroup类型，递归回收
            recycle(subView);
        }
    }

    public void recycleImageView(ImageView imageview) {
        //回收占用的Bitmap
        recycleImageViewBitMap(imageview);
        //如果flagWithBackgroud为true,则同时回收背景图
        if (flagWithBackgroud) {
            recycleBackgroundBitMap(imageview);
        }
    }

    private void recycleBackgroundBitMap(ImageView view) {
        if (view != null) {
            BitmapDrawable bd = (BitmapDrawable) view.getBackground();
            rceycleBitmapDrawable(bd);
        }
    }

    private void recycleImageViewBitMap(ImageView imageView) {
        if (imageView != null) {
            BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
            rceycleBitmapDrawable(bd);
        }
    }

    private void rceycleBitmapDrawable(BitmapDrawable bd) {
        if (bd != null) {
            Bitmap bitmap = bd.getBitmap();
            rceycleBitmap(bitmap);
        }
        bd = null;
    }

    private void rceycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}