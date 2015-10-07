package com.kevin.loopview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


import com.kevin.loopview.internal.BaseLoopAdapter;
import com.kevin.loopview.internal.LoopData;

/**
 * 版权所有：XXX有限公司</br>
 * 
 * AdLoopAdapter </br>
 * 
 * @author zhou.wenkai ,Created on 2015-5-19 22:28:29</br>
 * @Description Major Function：<b>一般数据适配器</b> </br>
 * 
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 * @author mender，Modified Date Modify Content:
 */
public class AdLoopAdapter extends BaseLoopAdapter {

//	private DisplayImageOptions options;
	
	public AdLoopAdapter(Context context, LoopData loopData,
						 ViewPager viewPager) {
		super(context, loopData, viewPager);
		
//        options = new DisplayImageOptions.Builder()
//	        .showImageOnLoading(R.mipmap.ic_launcher)
//	        .showImageOnFail(R.mipmap.ic_launcher)

//	        .cacheInMemory(true)
//	        .cacheOnDisk(true)
//	        .bitmapConfig(Bitmap.Config.RGB_565)
//	        .build();
	}

	/**
	 * 控件操作
	 * @param imageUrl
	 */
	public View instantiateItemView(String imageUrl, int position) {
		// 天气控件
//		if("weather".equals(mRotateData.items.get(position).type)) {
//			WeatherView mWeatherView = new WeatherView(mContext);
//			ImageLoader.getInstance().displayImage(imageUrl, mWeatherView.rootImageView, options);
//			return mWeatherView;
//		}

		ImageView imageview = new ImageView(mContext);
		imageview.setScaleType(ScaleType.FIT_XY);
		int imageViewWidth = ViewGroup.LayoutParams.MATCH_PARENT;
		int imageViewHeight = ViewGroup.LayoutParams.MATCH_PARENT;
		imageview.setLayoutParams(new ViewGroup.LayoutParams(imageViewWidth, imageViewHeight));
//		ImageLoader.getInstance().displayImage(imageUrl, imageview, options);
		return imageview;
	}
    
}