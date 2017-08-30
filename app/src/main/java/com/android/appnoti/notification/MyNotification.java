package com.android.appnoti.notification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.android.appnoti.Utils;
import com.android.appnoti.constant.Constants;


/**
 * Created by sunjianhua on 2017/8/29.
 */

@SuppressLint("OverrideAbstract")
public class MyNotification extends NotificationListenerService {

    private static final String TAG = "MyNotification";
    public static final String ACTION = "com.android.appnoti.notification.MyNotification";

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        String packageName = sbn.getPackageName();
        CharSequence ticket = sbn.getNotification().tickerText;
        Log.i(TAG,"------name---"+packageName+"---"+ticket.toString());
        if(!Utils.isEmpty(packageName) && !Utils.isEmpty(ticket.toString())){
            if(packageName.equals(Constants.QQ_PACKAGE_NAME)){  //QQ消息
                sendMsgBroadcast(packageName,ticket.toString());
            }else if( packageName.equals(Constants.WECHAT_PACKAGE_NAME)){   //微信消息
                sendMsgBroadcast(packageName,ticket.toString());
            }


        }

    }
    //发送广播
    private void sendMsgBroadcast(String packageName, String s) {
        Intent ints = new Intent(ACTION);
        Bundle bundle = new Bundle();
        bundle.putString("packname",packageName);
        bundle.putString("msg",s);
        ints.putExtras(bundle);
        sendBroadcast(ints);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
