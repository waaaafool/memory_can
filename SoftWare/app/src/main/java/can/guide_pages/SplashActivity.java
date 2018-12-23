package can.guide_pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import can.guide_pages.global.AppConstants;
import can.guide_pages.utils.SpUtils;

import can.guide_pages.global.AppConstants;
import can.guide_pages.utils.SpUtils;
import can.main_delete.MainActivity;
import can.memorycan.R;

/**
 * @desc 启动屏
 * Created by devilwwj on 16/1/23.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this,can.guide_pages.WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);
        SharedPreferences sp=getSharedPreferences("sp_demo",Context.MODE_PRIVATE);
        int is_close=sp.getInt("is_close",0);
        /*
        * isclose:
        * 0->第一次登陆,1->登陆后没有退出登陆,2->登陆后退出登陆
        * */
        if(is_close==0||is_close==2)
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 2000);
        else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    enterMainActivity();
                }
            }, 2000);
        }
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, can.login.LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private  void enterMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
