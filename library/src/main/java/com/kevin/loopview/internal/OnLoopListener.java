package com.kevin.loopview.internal;

/**
 * 版权所有：XXX有限公司</br>
 * 
 * OnLoopListener </br>
 * 
 * @author zhou.wenkai ,Created on 2015-1-18 12:21:13</br>
 * @Description Major Function：<b>定义一个接口,当Adapter被点击的时候作为回调被调用</b> </br>
 * 
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 * @author mender，Modified Date Modify Content:
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
