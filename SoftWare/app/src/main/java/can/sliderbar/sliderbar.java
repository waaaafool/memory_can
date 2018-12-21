package can.sliderbar;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;
import can.aboutsqlite.Memocloud;
import can.aboutsqlite.User;
import can.main_delete.MainActivity;
import can.memorycan.R;

public class sliderbar extends AppCompatActivity {
    RadioButton rb1,rb2,rb3,rb4,rb5,cloud_down,cloud_up;
    int user_id=57;
    DBManager mgr;
    int code=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mgr=new DBManager(this);
        setContentView(R.layout.activity_sliderbar);
        /*
        * 获取RadioButton的对象
        * */
        rb1=findViewById(R.id.radioButton4);
        rb2=findViewById(R.id.radioButton5);
        rb3=findViewById(R.id.radioButton7);
        rb4=findViewById(R.id.radioButton8);
        rb5=findViewById(R.id.radioButton9);
        cloud_down=findViewById(R.id.cloud_down);
        cloud_up=findViewById(R.id.cloud_up);

        /*
        * 对对象进行监听跳转
        * */

        rb1.setOnClickListener(new tomain());
        rb2.setOnClickListener(new toliveassitance());
        rb3.setOnClickListener(new towallpapergenerate());
        rb4.setOnClickListener(new tosetting());
        rb5.setOnClickListener(new tologin());
        cloud_down.setOnClickListener(new ytb());
        cloud_up.setOnClickListener(new ybf());
    }
    private class tomain implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(sliderbar.this,MainActivity.class);
            startActivity(intent);
        }
    }
    private class toliveassitance implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(sliderbar.this,can.live_assitcance.live_assitance.class);
            startActivity(intent);
        }
    }
    private class towallpapergenerate implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(sliderbar.this,can.wallpaper.wallpaper_generate.class);
            startActivity(intent);
        }
    }
    private class tosetting implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(sliderbar.this,can.memorycan.setting.setting.class);
            startActivity(intent);
        }
    }
    private class tologin implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(sliderbar.this,can.login.LoginActivity.class);
            startActivity(intent);
        }
    }
    private class ybf implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            try{
                URL url=new URL("http://139.224.232.186:8080/web/cloud/backup");
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("accept", "*/*");
                httpURLConnection.setRequestProperty("connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                User user=new User();
                user= mgr.returnUser(user_id);//输入用户ID
                Gson gson=new Gson();
                String users=user.toString();
                ArrayList memolist=mgr.returnmemo1(user_id);
                String json = gson.toJson(memolist);
                String param="type=云备份&user=" +users+ "&memo="+json;
                httpURLConnection.connect();
                PrintWriter writer=new PrintWriter(httpURLConnection.getOutputStream());
                writer.print(param);
                writer.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line=null;
                line=reader.readLine();
                int flag=0;//标示是否开始识别
                String temp_str="";
                for(int kk=0;kk<line.length()-1;kk++){
                    if(line.charAt(kk)==':') {
                        flag=1;
                    }
                    else if(flag==1&&line.charAt(kk)!=','){
                        temp_str+=line.charAt(kk);
                    }

                    if(line.charAt(kk)==',') break;

                }
                code=Integer.parseInt(temp_str);
                if(code==1)
                {
                    line=reader.readLine();
                    mgr.Deletememo_by_uid(user_id);
                    List<Memocloud> PostList = gson.fromJson(line, (new TypeToken<List<Memocloud>>() {}).getType());
                    for (int i = 0; i < PostList.size(); i++) {
                        mgr.insert_MemoCloud(PostList.get(i));
                    }
                }
                writer.close();
                reader.close();
            }catch (IOException e){
                System.out.println(e.toString());

            }
        }
    }
    private class ytb implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            try{
                URL url=new URL("http://139.224.232.186:8080/web/cloud/syn");
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("accept", "*/*");
                httpURLConnection.setRequestProperty("connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                String param="type=云同步&user_id="+user_id;
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                httpURLConnection.connect();
                PrintWriter writer=new PrintWriter(httpURLConnection.getOutputStream());
                writer.print(param);
                writer.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line=null;
                line=reader.readLine();
                Gson gson = new Gson();
                User user1=gson.fromJson(line, User.class);
                if(mgr.User_exist(user1.getUser_id()))
                    mgr.update_User(user1);
                else
                    mgr.insert_User(user1);
                mgr.Deletememo_by_uid(user1.getUser_id());
                line=reader.readLine();
                Gson gson1 = new Gson();
                List<Memocloud> PostList = gson1.fromJson(line, (new TypeToken<List<Memocloud>>() {}).getType());
                for (int i = 0; i < PostList.size(); i++) {
                    mgr.insert_MemoCloud(PostList.get(i));
                }
                writer.close();
                reader.close();
            }catch (IOException e){
                System.out.println(e.toString());

            }
        }
    }

}
