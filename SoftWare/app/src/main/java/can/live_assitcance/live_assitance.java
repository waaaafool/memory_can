package can.live_assitcance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.User;
import can.appUsed.AppInfo;
import can.memorycan.R;
import can.sms.HelloService;
import can.sms.TicketService;


import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import can.aboutsqlite.Memo;
import can.sms.Appcontext;



public class live_assitance extends AppCompatActivity {
    private User user;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationListener mLocationListener = new live_assitance.MyAMapLocationListener();
    public AMapLocationClientOption mLocationOption = null;
    static String cityName;
    private static String API = "https://free-api.heweather.com/s6/weather?key=3888986748b04aa591001efe452dcd30&location=";
    private String pckName;
    private String appName;
    private Long totalTime;
    private Integer launchTime;
    private static String contentText;
    private static final int K = 3;
    private Switch  sw1,sw2,sw3,sw4,sw5,sw6,sw7;//天气预报按钮sw5，智能分析按钮sw7
    private Switch sw8;//悬浮窗
    private static int user_id=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        ImageButton ig4;
        final Spinner sp1,sp2,sp3;
        final int[] percel={0,0,0};
        final int[] trip={0,0,0};
        final int[] weather={0,0,0};


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_assitance);
        /*
         * 获取各个权限的值
         * */
        SharedPreferences sp= getSharedPreferences("sp_demo",Context.MODE_PRIVATE);
        user_id=sp.getInt("user_id",1);

        ig4=findViewById(R.id.imageButton4);
        ig4.setOnClickListener(new tomain());

        final DBManager mgr = new DBManager(live_assitance.this);
        user=mgr.returnauser(1);

        trip[0]=user.getTrip_on();
        trip[1]=user.getTrip_priority();
        trip[2]=user.getParcel_paper();

        percel[0]=user.getTrip_on();
        percel[1]=user.getParcel_priority();
        percel[2]=user.getParcel_paper();

        weather[0]=user.getWeather_on();
        weather[1]=user.getParcel_priority();
        weather[2]=user.getParcel_paper();



        final String sdy[]={" "," "," "};
        /*
         * trip的spinner的设置
         * */
        if(trip[2]==0){
            sdy[0]="无";
        }
        else if(trip[2]==1){
            sdy[0]="低";
        }
        else if(trip[2]==2){
            sdy[0]="中";
        }
        else if(trip[2]==3){
            sdy[0]="高";
        }
        /*
         * parcel的spinner的设置
         * */
        if(percel[2]==0){
            sdy[1]="无";
        }
        else if(percel[2]==1){
            sdy[1]="低";
        }
        else if(percel[2]==2){
            sdy[1]="中";
        }
        else if(percel[2]==3){
            sdy[1]="高";
        }
        /*
         * weather的spinner的设置
         * */
        if(weather[2]==0){
            sdy[2]="无";
        }
        else if(weather[2]==1){
            sdy[2]="低";
        }
        else if(weather[2]==2){
            sdy[2]="中";
        }
        else if(weather[2]==3){
            sdy[2]="高";
        }

        /*
        * 对switch的对象获取
        * */
        sw1=findViewById(R.id.switch14);
        sw2=findViewById(R.id.switch4);
        sw3=findViewById(R.id.switch5);
        sw4=findViewById(R.id.switch6);
        sw5=findViewById(R.id.switch10);
        sw6=findViewById(R.id.switch11);
        sw7=findViewById(R.id.switch12);
        //sw8=findViewById(R.id.window);
        /*
        * 对switch进行设置
        * */
        if(trip[0]>0){
            sw1.setChecked(true);
        }
        else{
            sw1.setChecked(false);
        }

        if(trip[2]>0){
            sw2.setChecked(true);
        }
        else{
            sw2.setChecked(false);
        }
        if(percel[0]>0){
            sw3.setChecked(true);
        }
        else{
            sw3.setChecked(false);
        }
        if(percel[2]>0){
            sw4.setChecked(true);
        }
        else{
            sw4.setChecked(false);
        }
        if(weather[0]>0){
            sw5.setChecked(true);
        }
        else{
            sw5.setChecked(false);
        }
        if(weather[2]>0){
            sw6.setChecked(true);
        }
        else{
            sw6.setChecked(false);
        }


        /*
        * 对Spinner进行对象获取
        * */
        sp1=findViewById(R.id.spinner4);
        sp2=findViewById(R.id.spinner5);
        sp3=findViewById(R.id.spinner9);

        /*
        * 对spinner进行设置
        *
        * */
        SpinnerAdapter apsAdapter= sp1.getAdapter();
        int k= apsAdapter.getCount();
        for(int i=0;i< k;i++){
            if(sdy[0].equals(apsAdapter.getItem(i).toString())){
                sp1.setSelection(i,true);
                break;
            }
        }

        SpinnerAdapter apsAdapter1= sp2.getAdapter();
        int k1= apsAdapter.getCount();
        for(int i=0;i< k;i++){
            if(sdy[1].equals(apsAdapter1.getItem(i).toString())){
                sp1.setSelection(i,true);
                break;
            }
        }

        SpinnerAdapter apsAdapter2= sp3.getAdapter();
        int k2= apsAdapter.getCount();
        for(int i=0;i< k;i++){
            if(sdy[2].equals(apsAdapter2.getItem(i).toString())){
                sp1.setSelection(i,true);
                break;
            }
        }


        /*
        * 对switch进行监听
        * */

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent mIntent = new Intent(live_assitance.this, HelloService.class);
                if (sw1.isChecked() == true) {
                    getPermission(Manifest.permission.READ_SMS);
                    if (ActivityCompat.checkSelfPermission(live_assitance.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("----------------------------------------------------------");
                        startService(mIntent);
                        trip[0]=1;
                        user.setTrip_on(trip[0]);
                        mgr.changeTrip_on(1, trip[0]);
                    } else {
                        System.out.println("***********************************************************");
                        sw1.setChecked(false);
                        stopService(mIntent);
                    }
                } else {
                    trip[0]=0;
                    user.setTrip_on(trip[0]);
                    mgr.changeTrip_on(1, trip[0]);
                    stopService(mIntent);
                }
            }
        });
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    trip[2]=1;
                    user.setTrip_paper(trip[2]);
                    mgr.changeTrip_paper(1, trip[2]);

                }else{
                    trip[2]=0;
                    user.setTrip_paper(trip[2]);
                    mgr.changeTrip_paper(1, trip[2]);
                }
            }
        });
        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Intent mIntent = new Intent(live_assitance.this, TicketService.class);
                        if (sw1.isChecked() == true) {
                            getPermission(Manifest.permission.READ_SMS);
                            if (ActivityCompat.checkSelfPermission(live_assitance.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                                System.out.println("----------------------------------------------------------");
                                startService(mIntent);
                                percel[0]=1;
                                user.setParcel_on(percel[0]);
                            } else {
                                System.out.println("***********************************************************");
                                sw1.setChecked(false);
                                stopService(mIntent);
                            }
                        } else {
                            percel[0]=0;
                            user.setParcel_on(percel[0]);
                            stopService(mIntent);
                        }
                        mgr.changeParcel_on(1,percel[0]);
            }
        });
        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    percel[2]=1;
                    user.setParcel_paper(percel[2]);


                }else{
                    percel[2]=0;
                    user.setParcel_paper(percel[2]);
                }
                mgr.changeParcel_paper(1, percel[2]);
            }
        });
        sw5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    weather[0]=1;
                    user.setWeather_on(weather[0]);


                }else{
                    weather[0]=0;
                    user.setWeather_on(weather[0]);
                }
                mgr.changeWeather_on(1, weather[0]);
            }
        });
        sw6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    weather[2]=1;
                    user.setWeather_paper(weather[2]);


                }else{
                    weather[2]=0;
                    user.setWeather_paper(weather[2]);
                }
                mgr.changeWallpaper_paper(1, weather[2]);
            }
        });


        /*
        * 对spinner进行监听
        * */

        sp1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            //选择item的选择点击监听事件

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中

                String temp_s=sp1.getSelectedItem().toString();
                if(temp_s.equals("无")){
                    trip[1]=0;
                }
                else if(temp_s.equals("低")){
                    trip[1]=1;
                }
                else if(temp_s.equals("中")){
                    trip[1]=2;
                }
                else {
                    trip[1]=3;
                }
                mgr.changeTrip_priority(1, trip[1]);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        user.setTrip_priority(trip[1]);
        sp2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            //选择item的选择点击监听事件

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中

                String temp_s=sp1.getSelectedItem().toString();
                if(temp_s.equals("无")){
                    percel[1]=0;
                }
                else if(temp_s.equals("低")){
                    percel[1]=1;
                }
                else if(temp_s.equals("中")){
                    percel[1]=2;
                }
                else {
                    percel[1]=3;
                }
                mgr.changeParcel_priority(1, percel[1]);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        user.setParcel_priority((percel[1]));
        sp3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            //选择item的选择点击监听事件

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中

                String temp_s=sp1.getSelectedItem().toString();
                if(temp_s.equals("无")){
                    weather[1]=0;
                }
                else if(temp_s.equals("低")){
                    weather[1]=1;
                }
                else if(temp_s.equals("中")){
                    weather[1]=2;
                }
                else {
                    weather[1]=3;
                }
                mgr.changeWall_priority(1, weather[1]);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        user.setWeather_priority(weather[1]);

        /*
        * 将权限传入数据库
        * */
//        user.setTrip_on(trip[0]);
//        user.setTrip_priority(trip[1]);
//        user.setTrip_paper(trip[2]);
//
//        user.setParcel_on(percel[0]);
//        user.setParcel_priority((percel[1]));
//        user.setParcel_paper(percel[2]);
//
//        user.setWeather_on(weather[0]);
//        user.setWeather_priority(weather[1]);
//        user.setWeather_paper(weather[2]);

        sw5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
//                    new live_assitance.MyWeather().execute(API + cityName);
                    //新增关于AlarmManager的代码
                    Intent intent = new Intent(live_assitance.this, WeatherService.class);
                    startService(intent);
                }
            }
        });
        init();
        sw7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//权限打开
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    runAppUsed();
                    runSendNotify(contentText);
                }
            }
        });
    }

    private class tomain implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            Intent intent=new Intent(live_assitance.this,can.main_delete.MainActivity.class);
            startActivity(intent);
        }
    }

    private void getPermission(String permissios) {
        if (ActivityCompat.checkSelfPermission(this,permissios) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {permissios}, 123);
        }
    }

    //天气预报功能模块
    private void init() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }
    private class MyAMapLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Log.e("位置：", aMapLocation.getCity());
                    cityName = aMapLocation.getCity();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    }
    static class MyWeather extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer stringBuffer = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = null;
                if (httpURLConnection.getResponseCode() == 200) {
                    inputStream = httpURLConnection.getInputStream();
                    //检测网络异常
                } else {
                    return "11";
                }
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                stringBuffer = new StringBuffer();
                String timp = null;
                while ((timp = bufferedReader.readLine()) != null) {
                    stringBuffer.append(timp);
                }
                inputStream.close();
                reader.close();
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("11")) {
//                Toast.makeText(live_assitance.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONArray("HeWeather6").getJSONObject(0);
                JSONObject now = object1.getJSONObject("now");
                JSONObject lifestyle = object1.getJSONArray("lifestyle").getJSONObject(1);
                String tianqi = now.getString("cond_txt");
                if (tianqi==null) tianqi =now.optString("cond_txt");
                String advice = lifestyle.getString("txt");

                System.out.println("天气："+tianqi);
                System.out.println("建议："+advice);

                //天气状况：tianqi
                //建议：advice
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String memo_dtime=format.format(date);
                memo_dtime.substring(10);
                memo_dtime=memo_dtime + " 23:59:59";
                DBManager mgr;
                mgr=new DBManager(Appcontext.getContext());
                Memo memo = new Memo(tianqi,memo_dtime, mgr.getWeather_priority(user_id),0,
                        1, 1, 1, user_id, 0, advice);
                mgr.insert_Memo(memo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //智能提醒功能模块
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void runAppUsed(){
        if (isNoOption()){//判断是否有"有权查看使用情况的应用程序"这个选项
            while(true){
                if (isNoSwitch()){//判断"有权查看使用情况的应用程序"选项的打开状态
                    contentText = "运行时间最长的3个app为：";
                    appUsed(K);//小米和魅族手机没有Settings.ACTION_USAGE_ACCESS_SETTINGS
//                    tv1.setText(contentText);
                    break;
                }else{
                    try {
                        Toast.makeText(live_assitance.this,"请打开允许查看使用情况的权限",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    } catch (Exception e) {
                        Toast.makeText(live_assitance.this,"无法开启允许查看使用情况的应用界面",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    sw7.setChecked(false);
                    break;
                }
            }

        }else{
            sw7.setChecked(false);
            Toast.makeText(live_assitance.this, "您的手机没有设置选项“有权查看使用情况的应用程序”，无法使用该功能", Toast.LENGTH_SHORT)
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
                PackageManager pm = live_assitance.this.getPackageManager();//获取应用名
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
    void runSendNotify(String contentText){
        if (isNotificationEnabled(this)){
//            sendNotify(contentText);
            Intent intent = new Intent(live_assitance.this, AppUsedService.class);
            startService(intent);
        }else{
            Toast.makeText(live_assitance.this,"请打开通知栏的权限，否则无法实现通知功能",Toast.LENGTH_LONG).show();
            goToSet();
            runSendNotify(contentText);
        }
    }

    public static String getAPI(){
        return API;
    }
    public static String getCityName(){
        return  cityName;
    }
    public static String getContentText(){return contentText;}
    public static int getK(){return K;}
}
