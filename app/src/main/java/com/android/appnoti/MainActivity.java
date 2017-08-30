package com.android.appnoti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.appnoti.access.MyAccessService;
import com.android.appnoti.notification.MyNotification;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int OPEN_NOTI_REQUEST_CODE = 1001;
    private static final int OPEN_ACCIES_REQUEST_CODE = 1002;

    @BindView(R.id.openNotiBtn)
    Button openNotiBtn;
    @BindView(R.id.openAccessBtn)
    Button openAccessBtn;
    @BindView(R.id.showMsgTv)
    TextView showMsgTv;
    @BindView(R.id.accessShowTv)
    TextView accessShowTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //注册广播接收者
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyNotification.ACTION);
        intentFilter.addAction(MyAccessService.ACCESS_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);


    }

    @OnClick({R.id.openNotiBtn, R.id.openAccessBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.openNotiBtn:  //打开通知
                if (!Utils.isEnabled(MainActivity.this)) {    //如果通知栏未打开
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivityForResult(intent, OPEN_NOTI_REQUEST_CODE);
                } else {
                    Utils.showTost(MainActivity.this, "已打开通知");
                }
                break;
            case R.id.openAccessBtn:
                if (Utils.isAccessibilitySettingsOn(MainActivity.this)) {    //判断辅助功能是否打开
                    Intent intentr = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intentr, 0);
                } else {
                    Utils.showTost(MainActivity.this, "已打开辅助功能");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //卸载广播
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_NOTI_REQUEST_CODE && Utils.isEnabled(MainActivity.this)) {
            Utils.showTost(MainActivity.this, "已打开通知");
        }
        if (requestCode == OPEN_ACCIES_REQUEST_CODE && Utils.isAccessibilitySettingsOn(MainActivity.this)) {
            Utils.showTost(MainActivity.this, "已打开辅助功能");
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 接收广播
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "----action-" + action);
            if (!Utils.isEmpty(action)) {
                //通过通知获取消息内容
                if (action.equals(MyNotification.ACTION)) {
                    String packname = intent.getStringExtra("packname");
                    String msg = intent.getStringExtra("msg");
                    showMsgTv.setText("通过通知获取消息:"+"packname:" + packname + "\n" + "msg:" + msg);
                }
                //通过辅助功能获取消息内容
                if (action.equals(MyAccessService.ACCESS_ACTION)) {
                    String pacName = intent.getStringExtra("packName");
                    String msg = intent.getStringExtra("msg");
                    accessShowTv.setText("通过辅助服务获取消息:"+"packName:"+pacName+"\n"+"msg:"+msg);
                }
            }
        }
    };
}
