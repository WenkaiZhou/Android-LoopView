package com.kevin.loopview.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kevin.loopview.R;
import com.kevin.loopview.utils.JsonTool;

/**
 * 版权所有：XXX有限公司
 *
 * BaseLoopView
 *
 * @author zhou.wenkai ,Created on 2015-1-14 19:30:18
 * @author mender，Modified Date Modify Content:
 * Major Function：<b>自定义控件可以自动跳动的ViewPager</b>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 */
public abstract class BaseLoopView extends RelativeLayout implements ILoopView {

    /** ViewPager */
    protected ViewPager mViewPager;
    /** 设置的自定义布局id */
    protected int mLoopLayoutId;
    /** ViewPager数据适配器 */
    private BaseLoopAdapter adapter;
    /** 底部指示点父控件 */
    protected LinearLayout dotsView;
    /** 描述文字 */
    protected TextView descText;
    /** 指示点的位置 */
    protected int currentPosition = -1;
    /** 指示点距离 */
    protected float mDotMargin;
    /** 自动跳转的时间间隔 */
    protected long mInterval;
    /** 指示点选择器 */
    protected int mDotSelector;
    /** 默认图片 */
    protected int mDefaultImgId;
    /** 是否自动跳转 */
    private boolean autoLoop = false;

    /** 触摸时是否停止自动跳转 */
    private boolean stopScrollWhenTouch = true;
    /** 当前状态是否是由于触摸而停止 */
    private boolean isStoppedByTouch = false;
    /** 当前状态是否是由于不可见而停止 */
    private boolean isStoppedByInvisible = false;
    /** 当前状态是否为自动跳转 */
    private boolean isAutoScroll = true;

    /** 自动跳转的方向为自右向左 */
    public static final int LEFT = 0;
    /** 自动跳转的方向为自左向右 */
    public static final int RIGHT = 1;
    /** 自动跳转方向,默认自左向右 */
    protected int direction = RIGHT;

    /** 数据实体对象 */
    protected LoopData mLoopData;

    private Handler mHandler;
    /** 条目点击的接口回调 */
    protected BaseLoopAdapter.OnItemClickListener mOnItemClickListener;
    /** 自动跳转状态的接口回调 */
    protected OnLoopListener mOnLoopListener;
    /** 滑动控制器 */
    private LoopViewScroller mScroller;

    private float mDownX;
    private float mDownY;

    public BaseLoopView(Context context) {
        this(context, null);
    }

    public BaseLoopView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BaseLoopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 设置默认属性
        final float defaultDotMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        final int defaultInterval = 3000;

        // 设置样式属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopView);

        mDotMargin = a.getDimension(R.styleable.LoopView_loop_dotMargin, defaultDotMargin);
        mInterval = a.getInt(R.styleable.LoopView_loop_interval, defaultInterval);
        autoLoop = a.getBoolean(R.styleable.LoopView_loop_autoLoop, false);
        mDotSelector = a.getResourceId(R.styleable.LoopView_loop_dotSelector, R.drawable.loop_view_dots_selector);
        mDefaultImgId = a.getResourceId(R.styleable.LoopView_loop_defaultImg, 0);
        mLoopLayoutId = a.getResourceId(R.styleable.LoopView_loop_layout, 0);

        a.recycle();

        initRealView();
    }

    /**
     * 设置监听
     */
    protected void setViewListener() {

        // 设置viewPager监听
        setOnPageChangeListener();

        // 数据适配器点击事件监听回调
        adapter.setOnItemClickListener(new BaseLoopAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(PagerAdapter parent, View view, int position, int realPosition) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(parent, view, position, realPosition);
                }
            }
        });

    }

    /**
     * 设置页面切换过渡时间
     *
     * @param duration
     */
    @Override
    public void setScrollDuration(long duration) {
        mScroller = new LoopViewScroller(getContext());
        mScroller.setScrollDuration(duration);
        mScroller.initViewPagerScroll(mViewPager);
    }

    /**
     * 设置页面切换时间间隔
     */
    @Override
    public void setInterval(long interval) {
        this.mInterval = interval;
    }

    /**
     * 获取页面切换时间间隔
     *
     * @return
     */
    public long getInterval() {
        return mInterval;
    }

    /**
     * 获取ViewPager
     *
     * @return
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 获取封装数据
     *
     */
    @Override
    public LoopData getLoopData() {
        return mLoopData;
    }

    /**
     * 获取当前指示位置
     *
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 集合方式初始化轮转大图
     *
     * @param data
     */
    @Override
    public void setLoopViewPager(List<Map<String, String>> data) {
        if (null == data || data.size() == 0) return;
        if (mLoopData == null) {
            mLoopData = new LoopData();
            mLoopData.items = new ArrayList(data.size());
        }
        for (Map<String, String> map : data) {
            LoopData.ItemData itemDatas =
                    mLoopData.new ItemData(map.get("id"), map.get("imageURL"),
                            map.get("link"), map.get("descText"), map.get("type"));
            mLoopData.items.add(itemDatas);
        }

        initLoopViewPager();
    }

    /**
     * 对象方式初始化轮转大图
     *
     * @param loopData
     */
    @Override
    public void setLoopViewPager(LoopData loopData) {
        if (null == loopData) return;
        mLoopData = loopData;

        initLoopViewPager();
    }

    /**
     * Json方式初始化轮转大图
     *
     * @param jsonData
     */
    @Override
    public void setLoopViewPager(String jsonData) {
        if (null == jsonData) return;
        mLoopData = JsonTool.toBean(jsonData, LoopData.class);
        initLoopViewPager();
    }

    /**
     * 集合方式刷新数据
     *
     * @param data
     */
    public void refreshData(final List<Map<String, String>> data) {
        if (null == data || data.size() == 0) return;
        stopAutoLoop();
        removeAllViews();
        initRealView();
        mLoopData.items.clear();
        for (Map<String, String> map : data) {
            LoopData.ItemData itemData =
                    mLoopData.new ItemData(map.get("id"), map.get("imageURL"),
                            map.get("link"), map.get("descText"), map.get("type"));
            mLoopData.items.add(itemData);
        }
        initLoopViewPager();
        invalidate();
    }

    /**
     * 对象方式刷新数据
     *
     * @param loopData
     */
    public void refreshData(LoopData loopData) {
        if (null == loopData) return;
        stopAutoLoop();
        removeAllViews();
        initRealView();
        mLoopData = null;
        mLoopData = loopData;
        initLoopViewPager();
        invalidate();
    }

    /**
     * Json方式刷新数据
     *
     * @param jsonData
     */
    public void refreshData(String jsonData) {
        if (TextUtils.isEmpty(jsonData)) return;
        stopAutoLoop();
        removeAllViews();
        initRealView();
        mLoopData = null;
        mLoopData = JsonTool.toBean(jsonData, LoopData.class);
        initLoopViewPager();
        invalidate();
    }

    private void initLoopViewPager() {
        adapter = initAdapter();
        adapter.setDefaultImgId(mDefaultImgId);
        mViewPager.setAdapter(adapter);
        initDots(mLoopData.items.size());                     // 初始化指示点
        if(null != descText) {
            String descStr = mLoopData.items.get(0).descText;
            if(!TextUtils.isEmpty(descStr)) {
                descText.setText(descStr);                    // 初始化描述信息
            }
        }
        setViewListener();                                    // 初始化点击监听事件
        int startPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mLoopData.items.size();
        mViewPager.setCurrentItem(startPosition, false);      // 设置当前显示的位置
        if (mHandler == null) {
            mHandler = new LoopHandler(this, (Activity)getContext());
        }

        if (autoLoop) {
            startAutoLoop();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        // 当Y方向滑动距离大于X方向滑动距离时不获取滚动事件
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(ev.getY() - mDownY) > Math.abs(ev.getX() - mDownX)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * stopScrollWhenTouch为TRUE时
     * 按下操作停止轮转
     * 抬起操作继续轮转
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(stopScrollWhenTouch) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(isAutoScroll) {
                        stopAutoLoop();
                        isStoppedByTouch = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if(isStoppedByTouch) {
                        startAutoLoop(mInterval);
                        isStoppedByTouch = false;
                    }
                    break;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        // 当不可见的时候停止自动跳转
        switch (visibility) {
            case VISIBLE:
                if(isStoppedByInvisible) {
                    startCurrentAutoLoop();
                    isStoppedByInvisible = false;
                }
                break;
            case INVISIBLE:
            case GONE:
                if(isAutoScroll) {
                    stopAutoLoop();
                    isStoppedByInvisible = true;
                }
                break;
        }
    }

    /**
     * 开始自动跳转
     */
    public void startAutoLoop() {
        startAutoLoop(mInterval);
    }

    /**
     * 开始自动跳转
     *
     * @param delayTimeInMills 延时
     */
    public void startAutoLoop(long delayTimeInMills) {
        if (null == mLoopData || mLoopData.items.size() <= 1) return;
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * 发送跳转消息
     *
     * @param delayTimeInMills 延时
     */
    public void sendScrollMessage(long delayTimeInMills) {
        /** 先移除消息,保证最多只有一个消息 */
        removeAllMessages();
        mHandler.sendEmptyMessageDelayed(0, delayTimeInMills);
    }

    /**
     * 开始自动跳转
     * 由于控件嵌套入RecyclerView时由不可见到可见出现页面切换时间间隔为0现象
     */
    public void startCurrentAutoLoop() {
        if (null == mLoopData || mLoopData.items.size() <= 1) return;
        isAutoScroll = true;
        /** 先移除消息,保证最多只有一个消息 */
        removeAllMessages();
        mHandler.sendEmptyMessage(1);
    }

    /**
     * 移除所有消息
     */
    public void removeAllMessages() {
        if(null != mHandler) {
            mHandler.removeMessages(0);
            mHandler.removeMessages(1);
        }
    }

    /**
     * 停止自动跳转
     */
    @Override
    public void stopAutoLoop() {
        isAutoScroll = false;
        if (mHandler != null) {
            removeAllMessages();
        }
    }

    /**
     * 判断是否在自动轮播
     *
     * @return
     */
    public boolean isAutoScroll() {
        return isAutoScroll;
    }

    /**
     * 获取自动跳转方向
     *
     * @return
     */
    public int getDirection() {
        return direction;
    }

    /**
     * 注册点击监听的方法
     * @param l
     */
    public void setOnClickListener(BaseLoopAdapter.OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    /**
     * 设置跳转监听
     * @param l
     */
    public void setOnLoopListener(OnLoopListener l) {
        this.mOnLoopListener = l;
    }

    /**
     * 初始化View
     */
    protected abstract void initRealView();

    /**
     * 初始化轮转ViewPager
     */
    protected abstract BaseLoopAdapter initAdapter();

    /**
     * 初始化指示器
     *
     * @param size
     */
    protected abstract void initDots(int size);

    /**
     * 设置viewPager监听
     */
    protected abstract void setOnPageChangeListener();

    /**
     * 释放资源
     */
    public void releaseResources() {
        if(adapter != null) {
            adapter.releaseResources();
        }
        if (mHandler != null) {
            removeAllMessages();
            mHandler = null;
        }
    }

    /**
     * OnLoopListener
     *
     * <b>定义一个接口,当Adapter被点击的时候作为回调被调用</b>
     */
    public interface OnLoopListener {

        /**
         * LoopView 跳转到第一个时候会被调用
         *
         * @param realPosition	当前的绝对位置
         */
        void onLoopToStart(int realPosition);

        /**
         * LoopView 跳转到最后一个时候会被调用
         *
         * @param realPosition	当前的绝对位置
         */
        void onLoopToEnd(int realPosition);
    }

}