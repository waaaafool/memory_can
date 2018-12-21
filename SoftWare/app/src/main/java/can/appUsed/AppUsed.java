package can.appUsed;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import can.memorycan.R;

//Switch暂时和天气的Switch绑定一起，需要修改
public class AppUsed extends AppCompatActivity {
    private TextView tv1;
    private String pckName;
    private String appName;
    private Long totalTime;
    private Integer launchTime;
    private Switch switch1;
    private String contentText;
    private static final int K = 3;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binID();
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//权限打开
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    runAppUsed();
                    runSendNotify(contentText);
                }
//                else{
//                    tv1.setText("Switch按钮未开");
//                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void runAppUsed(){
        if (isNoOption()){//判断是否有"有权查看使用情况的应用程序"这个选项
            while(true){
                if (isNoSwitch()){//判断"有权查看使用情况的应用程序"选项的打开状态
                    contentText = "运行时间最长的3个app为：";
                    appUsed(K);//小米和魅族手机没有Settings.ACTION_USAGE_ACCESS_SETTINGS
//                    tv1.setText(contentText);
                    break;
                }else{
                    try {
                        Toast.makeText(AppUsed.this,"请打开允许查看使用情况的权限",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    } catch (Exception e) {
                        Toast.makeText(AppUsed.this,"无法开启允许查看使用情况的应用界面",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    switch1.setChecked(false);
                    break;
                }
            }

        }else{
            switch1.setChecked(false);
            Toast.makeText(AppUsed.this, "您的手机没有设置选项“有权查看使用情况的应用程序”，无法使用该功能", Toast.LENGTH_SHORT)
                    .show();
        }
//                    contentText = "运行时间最长的3个app为：";
//                    appUsed(K);//小米和魅族手机没有Settings.ACTION_USAGE_ACCESS_SETTINGS
//                    tv1.setText(contentText);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void appUsed(int k) {
        UsageStatsManager usm = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_WEEK, -2);//
        long startTime = calendar.getTimeInMillis();
/**
 * 最近两周启动过所用app的List
 * queryUsageStats第一个参数是根据后面的参数获取合适数据的来源，有按天，按星期，按月，按年等。
 *  UsageStatsManager.INTERVAL_BEST
 *   UsageStatsManager.INTERVAL_DAILY 按天
 *   UsageStatsManager.INTERVAL_WEEKLY 按星期
 *   UsageStatsManager.INTERVAL_MONTHLY 按月
 *   UsageStatsManager.INTERVAL_YEARLY 按年
 */
        List<UsageStats> list = usm.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY,startTime,endTime);//所有应用的list
        List<AppInfo> myList = new ArrayList<AppInfo>();//非系统应用的list
//需要注意的是5.1以上，如果不打开此设置，queryUsageStats获取到的是size为0的list；
        if(list.size()==0)
        {
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//                try {
//                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//                } catch (Exception e) {
//                    Toast.makeText(this,"无法开启允许查看使用情况的应用界面",Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//            }
            Toast.makeText(this,"List集合为空",Toast.LENGTH_LONG).show();
        }else{
            for(UsageStats usageStats :list){
                pckName = usageStats.getPackageName();//获取包名
                PackageManager pm = AppUsed.this.getPackageManager();//获取应用名
                try {
                    appName = pm.getApplicationLabel(pm.getApplicationInfo(pckName,PackageManager.GET_META_DATA)).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(usageStats.getPackageName(),PackageManager.GET_META_DATA);
                    if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                        //app是系统应用
                        continue;
                    }else{
                        //app是自载应用
                        totalTime = usageStats.getTotalTimeInForeground();//获取总共运行的时间
                    }
                    try{
                        Field field = usageStats.getClass().getDeclaredField("mLaunchCount");//获取应用启动次数，UsageStats未提供方法来获取，只能通过反射来拿到
                        launchTime = field.getInt(usageStats);
                        AppInfo myApp = new AppInfo(pckName, appName, totalTime, launchTime);
                        myList.add(myApp);
                    }catch (Exception e) {
                        e.printStackTrace();;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        //List排序
        Collections.sort(myList, new Comparator<AppInfo>(){
            public int compare(AppInfo a1, AppInfo a2) {
                if(a1.getMyTotalTime() > a2.getMyTotalTime()){
                    return 1;
                }
                if(a1.getMyTotalTime() == a2.getMyTotalTime()){
                    return 0;
                }
                return -1;
            }
        });

//        System.out.println("排序后的结果：");
//        for( int i = 0 ; i < myList.size() ; i++) {
//            System.out.println("应用名："+myList.get(i).getMyAppName());
//            System.out.println("运行时间："+myList.get(i).getMyTotalTime());
//            System.out.println(" ");
//        }
        for( int i = myList.size()-1; i>=myList.size()-k; i--) {
            if (TextUtils.isEmpty(contentText)){
                contentText = myList.get(i).getMyAppName();
            }else {
                contentText = contentText + myList.get(i).getMyAppName();
            }
            if (i!=myList.size()-k){
                contentText = contentText + "、";
            }
        }
    }

    //判断是否有"有权查看使用情况的应用程序"这个选项
    private boolean isNoOption() {
        PackageManager packageManager = getApplicationContext()
                .getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    //判断"有权查看使用情况的应用程序"选项的打开状态
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean isNoSwitch() {
        long ts = System.currentTimeMillis();
        @SuppressLint("WrongConstant") UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext()
                .getSystemService("usagestats");
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    //绑定控件id
    private void binID() {
//        tv1 = findViewById(R.id.tv1);
        switch1 = findViewById(R.id.switch12);//暂时和天气的Switch绑定一起，需要修改
    }

    //消息发送到通知栏
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotify(String contentText){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知渠道
            CharSequence name = "MenoCan";
            String description = "AppUsed";
            String channelId="channelId1";//渠道id
            int importance = NotificationManager.IMPORTANCE_DEFAULT;//重要性级别
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
            mChannel.setDescription(description);//渠道描述
            mChannel.enableLights(true);//是否显示通知指示灯
            mChannel.enableVibration(true);//是否振动
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);//创建通知渠道
            //第二个参数与channelId对应
            Notification.Builder builder = new Notification.Builder(AppUsed.this,channelId);
            //icon title text必须包含，不然影响桌面图标小红点的展示
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("用户最近两周使用app情况")
                    .setContentText(contentText)
                    .setNumber(3); //久按桌面图标时允许的此条通知的数量
            //            Intent intent= new Intent(this,NotificationActivity.class);
            //            PendingIntent ClickPending = PendingIntent.getActivity(this, 0, intent, 0);
            //            builder.setContentIntent(ClickPending);
            notificationManager.notify(1,builder.build());
            //            Notification notify = builder.build();
        }else{
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(AppUsed.this);
            //icon title text必须包含，不然影响桌面图标小红点的展示
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("用户最近两周使用app情况")
                    .setContentText(contentText)
                    .setNumber(3); //久按桌面图标时允许的此条通知的数量
            notificationManager.notify(2,builder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 进入设置系统应用权限界面
    private void goToSet(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void runSendNotify(String contentText){
        if (isNotificationEnabled(this)){
            sendNotify(contentText);
        }else{
            Toast.makeText(AppUsed.this,"请打开通知栏的权限，否则无法实现通知功能",Toast.LENGTH_LONG).show();
            goToSet();
            runSendNotify(contentText);
        }
    }

}
