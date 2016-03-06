package com.kevin.loopview.internal;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kevin.loopview.utils.RecycleBitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 版权所有：XXX有限公司
 *
 * BaseLoopAdapter
 *
 * @author zhou.wenkai ,Created on 2015-1-14 22:16:35
 * Major Function：<b>自定义控件可以自动跳动的ViewPager数据适配器基类</b>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 * @author mender，Modified Date Modify Content:
 */
public abstract class BaseLoopAdapter extends PagerAdapter {

    /** 上下文 */
    protected Context	mContext;
    /** ViewPager填充数据 */
    protected LoopData mLoopData;
    /** 条目点击的监听回调 */
    protected OnItemClickListener mOnItemClickListener;
    protected ViewPager mViewPager;
    /** 默认图片 */
    protected int defaultImgId;
    /** 轮转缓存集合 */
    Map<Integer, SoftReference<View>> instantiateViewMap = new HashMap();

    public BaseLoopAdapter(Context context, LoopData loopData, ViewPager viewPager) {
        super();
        this.mContext = context;
        this.mLoopData = loopData;
        this.mViewPager = viewPager;
        init();
    }

    /**
     * 初始化设置
     */
    protected void init() {
    }

    public void setLoopData(LoopData loopData) {
        this.mLoopData = loopData;
    }

    /**
     * 设置默认图片
     *
     * @param defaultImgId
     */
    public void setDefaultImgId(int defaultImgId) {
        this.defaultImgId = defaultImgId;
    }

    @Override
    public int getCount() {
        if(mLoopData.items.size() <= 1){
            return mLoopData.items.size();
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view;
        final int index = position % mLoopData.items.size();
        String imageUrl = mLoopData.items.get(index).imgUrl;

        // 首先读取软引用,如果不存在则初始化该View
        if (instantiateViewMap.containsKey(index)) {
            SoftReference<View> reference = instantiateViewMap.remove(index);
            if (null == reference || null == reference.get()) {
//              ToastUtils.showShortToast(mContext, "被回收了...");
                view = instantiateItemView(imageUrl, index);
            } else {
                view = reference.get();
//              ToastUtils.showShortToast(mContext, "读取软引用...");
            }
        } else {
//			ToastUtils.showShortToast(mContext, "初始化...");
            view = instantiateItemView(imageUrl, index);
        }

        if(mOnItemClickListener != null) {
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) { // 条目点击监听回调
                    mOnItemClickListener.onItemClick(BaseLoopAdapter.this, view, index, position);
                }

            });
        }

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        final int index = position % mLoopData.items.size();
        SoftReference reference = new SoftReference(view);
        instantiateViewMap.put(index, reference);
    }

    /**
     * 释放资源
     */
    public void releaseResources() {
        mLoopData = null;
        mOnItemClickListener = null;

        RecycleBitmap recycleBitmap = new RecycleBitmap(true);
        Iterator<Map.Entry<Integer, SoftReference<View>>> it = instantiateViewMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, SoftReference<View>> entry = it.next();
            SoftReference<View> reference = entry.getValue();
            View view = reference.get();
            if(view != null) {
                recycleBitmap.recycle(view);
            }
        }

        instantiateViewMap.clear();
    }

    /**
     * 控件操作
     * @param imageUrl
     */
    public abstract View instantiateItemView(String imageUrl, int position);

    /**
     * 注册回调监听
     *
     * 注：因为ViewPager没有条目点击的回调方法,所以这里参照<code>ListView</code>的监听设置方式
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 定义一个接口,当Adapter被点击的时候作为回调被调用
     */
    public interface OnItemClickListener {

        /**
         * PagerAdapter 的每一条目被点击的时候会被回调
         *
         * @param parent 		被点击的View的Adapter
         * @param view			被点击的View
         * @param position 		被点击的相对位置
         * @param realPosition	被点击的绝对位置
         */
        void onItemClick(PagerAdapter parent, View view, int position, int realPosition);
    }

}