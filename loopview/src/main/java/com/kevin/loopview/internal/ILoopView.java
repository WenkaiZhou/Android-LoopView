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
package com.kevin.loopview.internal;

import java.util.List;

/**
 * ILoopView
 *
 * @author zwenkai@foxmail.com, Created on 2015-1-20 19:27:44
 *         Major Function：<b>自定义控件可以自动跳动的ViewPager接口规范</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

public interface ILoopView {

    /**
     * 设置页面切换过渡时间
     *
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
     * 初始化轮转大图
     *
     * @param images
     */
    void setData(List<String> images);

    /**
     * 对象方式初始化轮转大图
     *
     * @param rotateData
     */
    void setData(LoopData rotateData);

    /**
     * 初始化轮转大图
     *
     * @param images
     * @param links
     */
    void setData(List<String> images, List<String> links);

    /**
     * 初始化轮转大图
     *
     * @param images
     * @param descs
     * @param links
     */
    void setData(List<String> images, List<String> descs, List<String> links);

    /**
     * 获取数据
     *
     * @return
     */
    LoopData getData();

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