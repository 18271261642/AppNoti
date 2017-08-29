package com.android.appnoti;

import android.app.Application;
import android.content.Intent;

import com.android.appnoti.access.MyAccessService;
import com.android.appnoti.notification.MyNotification;

/**
 * Created by sunjianhua on 2017/8/29.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startServices();
    }

    private void startServices() {
        Intent ints = new Intent(this, MyNotification.class);
        startService(ints);

        Intent ins = new Intent(this, MyAccessService.class);
        startService(ins);
    }
}
