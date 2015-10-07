package com.kevin.loopview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kevin.loopview.internal.BaseLoopAdapter;
import com.kevin.loopview.internal.BaseLoopView;

/**
 * 版权所有：XXX有限公司</br>
 * 
 * AdLoopView </br>
 * 
 * @author zhou.wenkai ,Created on 2015-1-14 19:30:18</br>
 * @Description Major Function：<b>自定义控件可以自动跳动的ViewPager</b> </br>
 * 
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 * @author mender，Modified Date Modify Content:
 */
public class AdLoopView extends BaseLoopView {
	
	public AdLoopView(Context context) {
		this(context, null);
	}
	
	public AdLoopView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public AdLoopView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	protected void initRealView() {
		setScrollDuration(1000);	// 设置页面切换时间

		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.ad_loopview_layout, null);
		// ViewPager
		mViewPager = (ViewPager) view.findViewById(R.id.ad_loopview_viewpager);
	    // 指示点父控件
	    dotsView = (LinearLayout) view.findViewById(R.id.ad_loopview_dots);
	    // 描述文字
	    descText = (TextView) view.findViewById(R.id.ad_loopview_desc);

		this.addView(view);
	}
	
	@Override
	protected BaseLoopAdapter initAdapter() {
		return new AdLoopAdapter(getContext(), mLoopData, mViewPager);
	}
	
	/** 初始化指示点 */
	@Override
	protected void initDots(int size) {
		dotsView.removeAllViews();
		for(int i=0; i<size; i++){
			ImageView dot = new ImageView(getContext());
			dot.setBackgroundResource(R.drawable.rotateview_dots_selector);
			int dotWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
			int dotHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
			LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(dotWidth, dotHeight);
			dotParams.setMargins(0, (int)mDotMargin, (int)mDotMargin, (int)mDotMargin);
			if(i == 0){
				dot.setEnabled(true);
			}else{
				dot.setEnabled(false);
			}
			dotsView.addView(dot, dotParams);
		}
	}

	@Override
	protected void setOnPageChangeListener() {
		// 数据适配器滑动监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				int i = position % mLoopData.items.size();
				dotsView.getChildAt(i).setEnabled(true);
				if(currentPosition != -1) {
					dotsView.getChildAt(currentPosition).setEnabled(false);}
				currentPosition = i;
				if(!TextUtils.isEmpty(mLoopData.items.get(i).descText)) {
					if(descText.getVisibility() != View.VISIBLE)
						descText.setVisibility(View.VISIBLE);
					String imageDesc = mLoopData.items.get(i).descText;
					descText.setText(imageDesc);
				} else {
					if(descText.getVisibility() == View.VISIBLE)
						descText.setVisibility(View.GONE);
				}
				
				// 跳转到头部尾部的监听回调
				if(mOnLoopListener != null) {
					if(i == 0) {
						mOnLoopListener.onLoopToStart(position);
					} else if(i == mLoopData.items.size() -1) {
						mOnLoopListener.onLoopToEnd(position);
					}
				}
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {}
			
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
		
	}

}
