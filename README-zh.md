
# Loop View for Android

LoopView 是一个强大的轮转大图控件，并且提供了许多配置方法来达到您的显示效果和需求。  

**简单示例**  
![AdLoopView Simple Demo](https://raw.githubusercontent.com/xuehuayous/Android-LoopView/master/loopview_ad_simple.gif)

**自定义布局示例**  
![AdLoopView Demo](https://raw.githubusercontent.com/xuehuayous/Android-LoopView/master/loopview_ad_custom.gif)

## 在项目中使用 LoopView

如果您的项目使用 Gradle 构建, 只需要在您的`build.gradle`文件添加下面一行到 `dependencies` :

```
compile 'com.kevin:loopview:1.3.1'
```

## 简单使用 ##

### 在layout.xml 中配置LoopView ###
在Layout文件添加`<com.kevin.loopview.BannerView`

	<com.kevin.loopview.BannerView
        android:id="@+id/main_act_banner"
        android:layout_width="match_parent"
        android:layout_height="192dp">
    </com.kevin.loopview.AdLoopView>

### 在代码中配置 ###

```
BannerView mBannerView = (BannerView) this.findViewById(R.id.main_act_banner);
// 设置 image loader, 可以设置任何图片加载引擎
mBannerView.setImageLoader(new ImageLoader() {
    @Override
    public void loadImage(ImageView imageView, String url, int placeholder) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
});
String json = LocalFileUtils.getStringFormAsset(this, "loopview_date.json");
LoopData loopData = new Gson().fromJson(json, LoopData.class);
// 通过对象的方式设置数据
mBannerView.setData(loopData);
// 开始播放
mBannerView.startAutoLoop();

mBannerView.setOnItemClickListener(new BaseLoopAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(View view, LoopData.ItemData itemData, int position) {
        // 通过系统浏览器打开跳转链接
        Intent intent = new Intent();
        intent.setData(Uri.parse(itemData.link));
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }
});
```

## 更多配置 ##

### XML 配置 ###

在XML中使用BannerView，可以有如下配置：

```
<com.kevin.loopview.BannerView
    android:id="@+id/adloop_act_adloopview"
    android:layout_width="match_parent"
    android:layout_height="192dp"
    kevin:loop_interval="5000"
    kevin:loop_dotMargin="5dp"
    kevin:loop_autoLoop="[true|false]"
    kevin:loop_dotSelector="@drawable/ad_dots_selector"
    kevin:loop_placeholder="@mipmap/ic_launcher"
    kevin:loop_layoutId="@layout/ad_loopview_layout">
</com.kevin.loopview.AdLoopView>
```

### 在代码中配置 ###

```
// 设置ViewPager页面切换时间
mLoopView.setScrollDuration(1000);
// 设置轮转时间间隔
mLoopView.setInterval(3000);
// 以数据实体的方式初始化数据
mLoopView.setData(LoopData loopData);
// 以集合的方式初始化数据(图片集合)
mLoopView.setData(List<String> images);
// 以集合的方式初始化数据(图片集合、链接集合)
mLoopView.setData(List<String> images, List<String> links);
// 以集合的方式初始化数据(图片集合、描述集合、链接集合)
mLoopView.setData(List<String> images, List<String> descs, List<String> links);
// 获取配置的轮转大图数据
mLoopView.getData();
// 开始自动轮转
mLoopView.startAutoLoop();
// 在指定时间延迟后自动轮转
mLoopView.startAutoLoop(long delayTimeInMills);
// 停止自动轮转
mLoopView.stopAutoLoop();
// 设置自定义布局
mLoopView.setLoopLayout(int layoutResId);
```

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