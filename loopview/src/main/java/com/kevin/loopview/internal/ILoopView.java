package com.kevin.loopview.internal;

import java.util.List;
import java.util.Map;

/**
 * 版权所有：XXX有限公司
 *
 * ILoopView
 *
 * @author zhou.wenkai ,Created on 2015-1-20 19:27:44
 * Major Function：自定义控件可以自动跳动的ViewPager接口规范
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 * @author mender，Modified Date Modify Content:
 */
public interface ILoopView {

    /**
     * 设置页面切换过渡时间
     * @param duration 毫秒值
     */
    void setScrollDuration(long duration);

    /**
     * Set the custom layout to be inflated for the loop views.
     *
     * @param layoutResId Layout id to be inflated
     */
    void setLoopLayout(int layoutResId);

    /**
     * 设置页面切换时间间隔
     */
    void setInterval(long interval);

    /**
     * 集合方式初始化轮转大图
     * @param data
     */
    void setLoopViewPager(List<Map<String, String>> data);

    /**
     * 对象方式初始化轮转大图
     * @param rotateData
     */
    void setLoopViewPager(LoopData rotateData);

    /**
     * Json方式初始化轮转大图
     * @param jsonData
     */
    void setLoopViewPager(String jsonData);

    /**
     * 集合方式刷新数据
     * @param data
     */
    void refreshData(final List<Map<String, String>> data);

    /**
     * 对象方式刷新数据
     * @param loopData
     */
    void refreshData(LoopData loopData);

    /**
     * Json方式刷新数据
     * @param jsonData
     */
    void refreshData(String jsonData);

    /**
     * 获取数据
     * @return
     */
    LoopData getLoopData();

    /**
     * 开始自动跳转
     */
    void startAutoLoop();

    /**
     * 开始自动跳转
     *
     * @param delayTimeInMills 延时
     */
    void startAutoLoop(long delayTimeInMills);

    /**
     * 停止自动跳转
     */
    void stopAutoLoop();

    /**
     * 释放资源
     */
    void releaseResources();

}