package com.android.appnoti.access;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.android.appnoti.constant.Constants;

/**
 * Created by sunjianhua on 2017/8/29.
 */

public class MyAccessService extends AccessibilityService {

    public static final String ACCESS_ACTION = "com.android.appnoti.access.MyAccessService";


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
            Parcelable parcelable = event.getParcelableData();
            Log.i("ACC","------pac--"+event.getPackageName());
            if(parcelable instanceof Notification){
                String msg = event.getText().toString();
                String packName = event.getPackageName().toString();
                if(packName.equals(Constants.QQ_PACKAGE_NAME)){
                    sendAccessDataBroadcast(msg,packName);
                }else if(packName.equals(Constants.WECHAT_PACKAGE_NAME)){
                    sendAccessDataBroadcast(msg,packName);
                }
            }
        }
    }

    //发送广播
    private void sendAccessDataBroadcast(String msg, String packName) {
        Intent intent = new Intent(ACCESS_ACTION);
        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        bundle.putString("packName",packName);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    @Override
    public void onInterrupt() {

    }
}
