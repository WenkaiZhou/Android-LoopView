
# Loop View for Android
**[English](https://github.com/xuehuayous/Android-LoopView)** **[中文](https://github.com/xuehuayous/Android-LoopView/blob/master/README-zh.md)**

Android LoopView is a powerful widget for unlimited rotation picture, It provides some configuration options and good control the appearance and operational requirements.

**Simple** usage picture:  
![AdLoopView Simple Demo](https://raw.githubusercontent.com/xuehuayous/Android-LoopView/master/loopview_ad_simple.gif)

**Custom layout** usage picture:  
![AdLoopView Demo](https://raw.githubusercontent.com/xuehuayous/Android-LoopView/master/loopview_ad_custom.gif)

## Using LoopView in your application

If you are building with Gradle, simply add the following line to the `dependencies` section of your `build.gradle` file:

```
compile 'com.kevin:loopview:1.4.3'
```

## Simple Usage ##

### Configured as View in layout.xml ###
To add the LoopView to your application, specify `<com.kevin.loopview.BannerView` in your layout XML.

```
<com.kevin.loopview.BannerView
    android:id="@+id/main_act_banner"
    android:layout_width="match_parent"
    android:layout_height="192dp">
</com.kevin.loopview.BannerView>
```

### Configured Programmatically ###

```
BannerView mBannerView = (BannerView) this.findViewById(R.id.main_act_banner);
// set image loader, can set up any image engine.
mBannerView.setImageLoader(new ImageLoader() {
    @Override
    public void loadImage(ImageView imageView, String url, int placeholder) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
});
String json = LocalFileUtils.getStringFormAsset(this, "loopview_date.json");
LoopData loopData = new Gson().fromJson(json, LoopData.class);
mBannerView.setData(loopData);
// begin to loop
mBannerView.startAutoLoop();

mBannerView.setOnItemClickListener(new BaseLoopAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(View view, LoopData.ItemData itemData, int position) {
        // Open connection with browser
        Intent intent = new Intent();
        intent.setData(Uri.parse(itemData.link));
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }
});
```

## More configuration Usage ##

### XML Usage ###

If you decide to use BannerView as a view, you can define it in your xml layout like this:

```
xmlns:app="http://schemas.android.com/apk/res-auto"

<com.kevin.loopview.BannerView
    android:id="@+id/adloop_act_adloopview"
    android:layout_width="match_parent"
    android:layout_height="192dp"
    app:loop_interval="5000"
    app:loop_dotMargin="5dp"
    app:loop_autoLoop="[true|false]"
    app:loop_dotSelector="@drawable/ad_dots_selector"
    app:loop_placeholder="@mipmap/ic_launcher"
    app:loop_layout="@layout/ad_loopview_layout">
</com.kevin.loopview.BannerView>
```

### Programme Usage ###

```
// Set page switching transition time
mLoopView.setScrollDuration(1000);
// Set time interval
mLoopView.setInterval(3000);
// To initialize the data in a collection
mLoopView.setData(List<Map<String, String>> data);
// Initialized data in entity mode
mLoopView.setData(LoopData rotateData);
// Initialized data in a collection mode
mLoopView.setData(List<String> images);
// Initialized data in a collection mode
mLoopView.setData(List<String> images, List<String> links);
// Initialized data in a collection mode
mLoopView.setData(List<String> images, List<String> descs, List<String> links);
// Get the running loop date
mLoopView.getData();
// Begin to auto Loop
mLoopView.startAutoLoop();
// Begin to auto Loop delay
mLoopView.startAutoLoop(long delayTimeInMills);
// Stop to auto Loop
mLoopView.stopAutoLoop();
// Set a custom loop layout
mLoopView.setLoopLayout(int layoutResId);
```

### Notes: ###

In custom layout you must to use those ids `loop_view_pager` in ViewPager `loop_view_dots` in indicate point parent LinearLayout and `loop_view_desc` in description TextView;

Make sure you at least have `loop_view_pager`.

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