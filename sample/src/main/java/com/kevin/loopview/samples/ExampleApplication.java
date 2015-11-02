package com.kevin.loopview.samples;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by zhouwk on 2015/11/2 0002.
 */
public class ExampleApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}