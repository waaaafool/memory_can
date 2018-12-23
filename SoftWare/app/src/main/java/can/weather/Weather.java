package can.weather;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import android.os.AsyncTask;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;
import can.memorycan.R;
import can.sms.Appcontext;

public class Weather extends AppCompatActivity {
    private Switch switch1;
    private TextView tv_now;
    private TextView tv_lifestyle;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    public AMapLocationClientOption mLocationOption = null;

    String cityName;
    //weather部分
    private String API = "https://free-api.heweather.com/s6/weather?key=3888986748b04aa591001efe452dcd30&location=";
//    private String API1 = "https://free-api.heweather.com/s6/weather/now?key=3888986748b04aa591001efe452dcd30&location=";
//    private String API2 = "https://free-api.heweather.com/s6/weather/lifestyle?key=3888986748b04aa591001efe452dcd30&location=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_assitance);

        binID();
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
//                    new MyWeather1().execute(API1 + cityName);
//                    new MyWeather2().execute(API2 + cityName);
                    new MyWeather().execute(API + cityName);
                }else{
//                    tv_now.setText("Switch按钮未开");
                }
            }
        });
        init();
    }

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

    private void binID() {
        switch1 = findViewById(R.id.switch10);
//        tv_now = findViewById(R.id.now);
//        tv_lifestyle = findViewById(R.id.lifestyle);
    }
//    class MyWeather1 extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            StringBuffer stringBuffer = null;
//
//            try {
//                URL url = new URL(strings[0]);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = null;
//                if (httpURLConnection.getResponseCode() == 200) {
//                    inputStream = httpURLConnection.getInputStream();
//                    //检测网络异常
//                } else {
//                    return "11";
//                }
//                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(reader);
//                stringBuffer = new StringBuffer();
//                String timp = null;
//                while ((timp = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(timp);
//                }
//                inputStream.close();
//                reader.close();
//                bufferedReader.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return stringBuffer.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if (s.equals("11")) {
//               Toast.makeText(Weather.this, "网络异常", Toast.LENGTH_SHORT).show();
//            }
//            try {
//                JSONObject object = new JSONObject(s);
//                JSONObject object1 = object.getJSONArray("HeWeather6").getJSONObject(0);
//               JSONObject now = object1.getJSONObject("now");
////                JSONObject lifestyle = object1.getJSONObject("lifestyle");
//                String tianqi = now.getString("cond_txt");
////                String advice = lifestyle.getString("txt");
//                tv_now.setText("当前天气状况：" + "“" + tianqi + "”" );
////                tv_lifestyle.setText("Tips："+ advice);
//            } catch (JSONException e) {
//                tv_now.setText("ERROR！");
//                e.printStackTrace();
//            }
//        }
//    }
//    class MyWeather2 extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            StringBuffer stringBuffer = null;
//
//            try {
//                URL url = new URL(strings[0]);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = null;
//                if (httpURLConnection.getResponseCode() == 200) {
//                    inputStream = httpURLConnection.getInputStream();
//                    //检测网络异常
//                } else {
//                    return "11";
//                }
//                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(reader);
//                stringBuffer = new StringBuffer();
//                String timp = null;
//                while ((timp = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(timp);
//                }
//                inputStream.close();
//                reader.close();
//                bufferedReader.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return stringBuffer.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if (s.equals("11")) {
//                Toast.makeText(Weather.this, "网络异常", Toast.LENGTH_SHORT).show();
//            }
//            try {
//                JSONObject object = new JSONObject(s);
//                JSONObject object1 = object.getJSONArray("HeWeather6").getJSONObject(0);
////                JSONArray object2 = object1.getJSONArray("lifestyle");
////                JSONObject lifestyle = object2.getJSONObject(1);
//                JSONObject lifestyle = object1.getJSONArray("lifestyle").getJSONObject(1);
//                String advice = lifestyle.getString("txt");
//                tv_lifestyle.setText("Tips：" + "“" + advice + "”");
//            } catch (JSONException e) {
//                tv_now.setText("ERROR！");
//                e.printStackTrace();
//            }
//        }
//    }
    class MyWeather extends AsyncTask<String, String, String> {
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
                Toast.makeText(Weather.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONArray("HeWeather6").getJSONObject(0);
                JSONObject now = object1.getJSONObject("now");
                JSONObject lifestyle = object1.getJSONArray("lifestyle").getJSONObject(1);
                String tianqi = now.getString("cond_txt");
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
                SharedPreferences sp=getSharedPreferences("sp_demo",MODE_PRIVATE);
                int user_id=sp.getInt("user_id",1);
                System.out.println("user_id : " + user_id);
                Memo memo = new Memo(tianqi,memo_dtime, mgr.getWeather_priority(user_id),0,
                        1, 1, 1, user_id, 0, advice);
                mgr.insert_Memo(memo);


//                tv_now.setText("当前天气状况：" + "“" + tianqi + "”" );
//                tv_lifestyle.setText("Tips："+ advice);
            } catch (JSONException e) {
//                tv_now.setText("ERROR！");
                e.printStackTrace();
            }
        }
    }

}

