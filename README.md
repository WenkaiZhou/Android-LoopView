
# Loop View for Android

Android LoopView is a powerful widget for unlimited rotation picture, It provides some configuration options and good control the appearance and operational requirements.

![Sample AdLoopView Demo](https://raw.githubusercontent.com/xuehuayous/Android-LoopView/master/loopview_ad_sample.gif)

![AdLoopView Demo](https://raw.githubusercontent.com/xuehuayous/Android-LoopView/master/loopview_ad.gif)

## Simple Use ##

### Configured as View in layout.xml ###
To add the LoopView to your application, specify `<com.kevin.loopview.AdLoopView` in your layout XML.

	<com.kevin.loopview.AdLoopView
        android:id="@+id/main_act_adloopview"
        android:layout_width="match_parent"
        android:layout_height="192dp">
    </com.kevin.loopview.AdLoopView>

### Configured Programmatically ###

	AdLoopView mLoopView = (AdLoopView) this.findViewById(R.id.main_act_adloopview);
	String json = LocalFileUtils.getStringFormAsset(this, "loopview_date.json");
    // Use JsonTool to parse JSON data to entity
	LoopData loopData = JsonTool.toBean(json, LoopData.class);
	// set AdLoopView date use entity
    mLoopView.refreshData(loopData);
	// begin to loop
    mLoopView.startAutoLoop();

	mLoopView.setOnClickListener(new BaseLoopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PagerAdapter parent, View view, 
				int position, int realPosition) {
                // get the loop data and the action link
                LoopData loopData = mLoopView.getLoopData();
                String url = loopData.items.get(position).link;

                // Open connection with browser
                Intent intent = new Intent();
                intent.setData(Uri.parse(url));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });


## XML Usage ##

If you decide to use AdLoopView as a view, you can define it in your xml layout like this:

    <com.kevin.loopview.AdLoopView
        android:id="@+id/adloop_act_adloopview"
        android:layout_width="match_parent"
        android:layout_height="192dp"
        kevin:loop_interval="5000"
        kevin:loop_dotMargin="5dp"
        kevin:loop_autoLoop="[true|false]"
        kevin:loop_dotSelector="@drawable/ad_dots_selector">
    </com.kevin.loopview.AdLoopView>

## License

    Copyright 2011, 2012 Kevin zhou

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.