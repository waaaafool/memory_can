package can.live_assitcance;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import can.main_delete.MainActivity;
import can.sms.Appcontext;

public class AppUsedService extends Service {
    //    private int anHour =8*60*60*1000;// 这是8小时的毫秒数 为了少消耗流量和电量，8小时自动更新一次
    private int anHour = 14 * 24 * 60 * 60 * 1000;// 这是2周的毫秒数,2周自动更新一次
    private String contentText;
    private int id = MainActivity.getNotifyId();
    private int i = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    /**
     * 采用AlarmManager机制
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (contentText == null) contentText = live_assitance.getContentText();
                i++;
                if (i != 1) {
                    sendNotify(contentText);
                }
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent intent2 = new Intent(this, AutoUpdateReceiver0.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent2, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //消息发送到通知栏
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void sendNotify(String contentText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知渠道
            CharSequence name = "MenoCan";
            String description = "AppUsed";
            String channelId = "channelId1";//渠道id
            int importance = NotificationManager.IMPORTANCE_DEFAULT;//重要性级别
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
            mChannel.setDescription(description);//渠道描述
            mChannel.enableLights(true);//是否显示通知指示灯
            mChannel.enableVibration(true);//是否振动
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);//创建通知渠道
            //第二个参数与channelId对应
            Notification.Builder builder = new Notification.Builder(AppUsedService.this, channelId);
            //icon title text必须包含，不然影响桌面图标小红点的展示
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("最近使用时间最长的3个app")
                    .setContentText(contentText)
                    .setNumber(3); //久按桌面图标时允许的此条通知的数量

            //            Intent intent= new Intent(this,NotificationActivity.class);
            //            PendingIntent ClickPending = PendingIntent.getActivity(this, 0, intent, 0);
            //            builder.setContentIntent(ClickPending);
            notificationManager.notify(id++, builder.build());
            //            Notification notify = builder.build();
        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(AppUsedService.this);
            //icon title text必须包含，不然影响桌面图标小红点的展示
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("最近使用时间最长的3个app")
                    .setContentText(contentText)
                    .setNumber(3); //久按桌面图标时允许的此条通知的数量
            notificationManager.notify(id++, builder.build());
        }
    }


}


