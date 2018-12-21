package can.sms;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;

public class HelloService extends Service {
    public static final Uri                      SMS_MESSAGE_URI = Uri.parse("content://sms");
    private static      SmsDatabaseChaneObserver mSmsDatabaseChaneObserver;
    private String endTime = "9999-12-30 00:00:00";

    private void registerSmsDatabaseChangeObserver(ContextWrapper contextWrapper) {
        //因为，某些机型修改rom导致没有getContentResolver
        try {

            System.out.println("启动监听");
            mSmsDatabaseChaneObserver = new SmsDatabaseChaneObserver(contextWrapper.getContentResolver(), mHandler);
            contextWrapper.getContentResolver().registerContentObserver(SMS_MESSAGE_URI, true, mSmsDatabaseChaneObserver);
        } catch (Throwable b) {
        }
    }

    private void unregisterSmsDatabaseChangeObserver(ContextWrapper contextWrapper) {
        try {

            System.out.println("停止监听");
            contextWrapper.getContentResolver().unregisterContentObserver(mSmsDatabaseChaneObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                ArrayList<String> ary = (ArrayList<String>)msg.obj;
                if (msg.what == 1) {
                    System.out.println("--=-------------------------------------------写入数据库了！！！----");
                    System.out.println(ary.get(0));
                    writeToDatabase(ary);
                }
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
}

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterSmsDatabaseChangeObserver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //System.out.println("启动Service,休眠10秒");
        registerSmsDatabaseChangeObserver(this);
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Notification notification = new Notification(R.drawable.ic_launcher,getString(R.string.app_name), System.currentTimeMillis());
//        getString(R.string.app_name), System.currentTimeMillis());
//        PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
//                new Intent(this, AppMain.class), 0);
//        notification.setLatestEventInfo(this, "uploadservice", "请保持程序在后台运行",
//                pendingintent);
//        startForeground(0x111, notification);

        return START_STICKY;
    }


    private void writeToDatabase(ArrayList<String> info) {

        DBManager mgr = null;
        mgr = new DBManager(this);
        String deadTime;
        deadTime = info.get(2);
        if (info.get(2) == null) {
            deadTime = endTime;
        }
        Memo memo = new Memo(info.get(0),deadTime, mgr.getParcel_priority(1), 1,
                1, 1, 1, 1, 0, info.get(1));
        mgr.insert_Memo(memo);
    }
}
