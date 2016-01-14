
# Loop View for Android

LoopView 是一个强大的轮转大图控件，并且提供了许多配置方法来达到您的显示效果和需求。  

**简单示例**  
![AdLoopView Simple Demo](https://raw.githubusercontent.com/xuehuayous/Android-LoopView/master/loopview_ad_simple.gif)

**自定义布局示例**  
![AdLoopView Demo](https://raw.githubusercontent.com/xuehuayous/Android-LoopView/master/loopview_ad_custom.gif)

## 在项目中使用 LoopView

如果您的项目使用 Gradle 构建, 只需要在您的`build.gradle`文件添加下面一行到 `dependencies` :

```
	compile 'com.kevin:loopview:1.0.4'
```

## 简单使用 ##

### 在layout.xml 中配置LoopView ###
在Layout文件添加`<com.kevin.loopview.AdLoopView` 

	<com.kevin.loopview.AdLoopView
        android:id="@+id/main_act_adloopview"
        android:layout_width="match_parent"
        android:layout_height="192dp">
    </com.kevin.loopview.AdLoopView>

### 在代码中配置 ###

	AdLoopView mLoopView = (AdLoopView) this.findViewById(R.id.main_act_adloopview);
	String json = LocalFileUtils.getStringFormAsset(this, "loopview_date.json");
    // 使用 JsonTool 封装 JSON 数据到实体对象
	LoopData loopData = JsonTool.toBean(json, LoopData.class);
	// 通过对象的方式设置数据
    mLoopView.refreshData(loopData);
	// 开始轮转
    mLoopView.startAutoLoop();

	// 设置点击监听
	mLoopView.setOnClickListener(new BaseLoopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PagerAdapter parent, View view, 
				int position, int realPosition) {
                // 获取数据
                LoopData loopData = mLoopView.getLoopData();
                String url = loopData.items.get(position).link;

                // 通过系统浏览器打开跳转链接
                Intent intent = new Intent();
                intent.setData(Uri.parse(url));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });


## 更多配置 ##

### XML 配置 ###

在XML中使用AdLoopView，可以有如下配置：

    <com.kevin.loopview.AdLoopView
        android:id="@+id/adloop_act_adloopview"
        android:layout_width="match_parent"
        android:layout_height="192dp"
        kevin:loop_interval="5000"
        kevin:loop_dotMargin="5dp"
        kevin:loop_autoLoop="[true|false]"
        kevin:loop_dotSelector="@drawable/ad_dots_selector"
		kevin:loop_defaultImg="@mipmap/ic_launcher"
		kevin:loop_layoutId="@layout/ad_loopview_layout">
    </com.kevin.loopview.AdLoopView>

### 在代码中配置 ###

	// 设置ViewPager页面切换时间
	mLoopView.setScrollDuration(1000);
	// 设置轮转时间间隔
	mLoopView.setInterval(3000);
	// 以集合的方式初始化数据
	mLoopView.setLoopViewPager(List<Map<String, String>> data);
	// 以JSON的方式初始化数据
	mLoopView.setLoopViewPager(String jsonData);
	// 以数据实体的方式初始化数据
	mLoopView.setLoopViewPager(LoopData rotateData);
	// 以集合的方式刷新数据
	mLoopView.refreshData(final List<Map<String, String>> data);
	// 以数据实体的方式刷新数据
	mLoopView.refreshData(LoopData loopData);
	// 以JSON的方式刷新数据
	mLoopView.refreshData(String jsonData);
	// 获取配置的轮转大图数据
	mLoopView.getLoopData();
	// 开始自动轮转
	mLoopView.startAutoLoop();
	// 在指定时间延迟后自动轮转
	mLoopView.startAutoLoop(long delayTimeInMills);
	// 停止自动轮转
	mLoopView.stopAutoLoop();
	// 设置自定义布局
	mLoopView.setLoopLayout(int layoutResId);

### 注意: ###

在自定义布局时您必须使用以下 id，ViewPager的id为 `loop_view_pager`  指示点父控件的id为`loop_view_dots` 并且该父控件为LinearLayout 以及 描述文字的id为`loop_view_desc`;

至少保证要包含id为 `loop_view_pager`的ViewPager

## License

    Copyright 2015 Kevin zhou

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.