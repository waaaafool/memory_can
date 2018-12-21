package can.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
import java.util.List;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memocloud;
import can.aboutsqlite.User;
import can.login.SignupActivity;
import can.main_delete.MainActivity;
import can.memorycan.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

     EditText _mobileText;
     EditText _passwordText;
     Button _loginButton;
     TextView _signupLink;
     int code=0;
     int user_id=0;
     String mobile;
     String password;
     String temp_str="";
    DBManager mgr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mgr=new DBManager(this);
        setContentView(R.layout.activity_login);
        SharedPreferences sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        mobile=sp.getString("mobile",null);
        password=sp.getString("password",null);

        _loginButton=findViewById(R.id.btn_login);
        _signupLink=findViewById(R.id.link_signup);
        _mobileText=findViewById(R.id.input_mobile1);
        _mobileText.setText(mobile);
        _passwordText=findViewById(R.id.input_password);
        _passwordText.setText(password);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("登陆中...");
        progressDialog.show();

        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        SharedPreferences sp=getSharedPreferences("sp_demo",Context.MODE_PRIVATE);
        int is_close=sp.getInt("is_close",0);
        String mobile_1=sp.getString("mobile",null);
        String password_1=sp.getString("password",null);
        int key=0;
        if(is_close!=0){
            if(mgr.is_exis(mobile,password)==-1){
                code=1;
                key=1;
            }
            else{
                user_id=mgr.is_exis(mobile,password);
                code=2;
            }
        }
        if(is_close==0||key==1){
            try{
                URL url=new URL("http://139.224.232.186:8080/web/user/login");

                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("accept", "*/*");
                httpURLConnection.setRequestProperty("connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                String param="user_tel="+mobile+"&user_password="+password;
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                httpURLConnection.connect();
                PrintWriter writer=new PrintWriter(httpURLConnection.getOutputStream());
                writer.print(param);
                writer.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line=null;
                line=reader.readLine();
                line=line.substring(0,(line.length()-1));
                int flag=0;//标示是否开始识别
                temp_str="";
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

                if(code==2){

                    line=reader.readLine();
                    flag=0;//标示是否开始识别
                    temp_str="";
                    for(int kk=0;kk<line.length()-1;kk++){
                        if(line.charAt(kk)==':') {
                            flag=1;
                        }
                        else if(flag==1&&line.charAt(kk)!=','){
                            temp_str+=line.charAt(kk);
                        }

                        if(line.charAt(kk)==',') break;

                    }
                    user_id=Integer.parseInt(temp_str);
                    temp_str=line;
                    Gson gson1=new Gson();
                    // line=line.substring(0,line.length());
                    User user1=gson1.fromJson(line,User.class);
                    if(mgr.User_exist(user1.getUser_id()))
                        mgr.update_User(user1);
                    else
                        mgr.insert_User(user1);
                    mgr.Deletememo_by_uid(user1.getUser_id());
                    line=null;
                    line=reader.readLine();
                    mgr.Deletememo_by_uid(user1.getUser_id());
                    Gson gson=new Gson();
                    List<Memocloud> PostList = gson.fromJson(line, (new TypeToken<List<Memocloud>>() {}).getType());
                    for (int i = 0; i < PostList.size(); i++) {
                        mgr.insert_MemoCloud(PostList.get(i));
                    }

                }

                writer.close();
                reader.close();
            }catch (IOException e){
                System.out.println(e.getMessage());

            }
        }


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(code==2){
                            onLoginSuccess();
                        }
                        else{
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        SharedPreferences sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("mobile", mobile);
        editor.putString("password",password);
        editor.putInt("user_id", user_id);
        editor.putInt("is_close", 1);
        editor.commit();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);
        finish();
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(),"登录失败" , Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();

//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }

        if (mobile.isEmpty() || mobile.length()!=11) {
            _mobileText.setError("请输入有效的电话号码");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("4到10个字母或者数字");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
