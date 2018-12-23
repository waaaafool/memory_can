package can.live_assitcance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class WeatherService extends Service {
//    private int anHour =8*60*60*1000;// 这是8小时的毫秒数 为了少消耗流量和电量，8小时自动更新一次
    private int anHour = 24*60*60*1000;// 这是一天的毫秒数,1天自动更新一次
    private String API = live_assitance.getAPI();
    private String cityName = live_assitance.getCityName();
    public WeatherService() {
    }

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
                if (cityName==null) cityName = "福州市";
                new can.live_assitcance.live_assitance.MyWeather().execute(API + cityName);
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent intent2 = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent2, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


