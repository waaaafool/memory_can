package can.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;


import can.aboutsqlite.DBManager;
import can.memorycan.R;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

     EditText _nameText;
     EditText _addressText;
     EditText _emailText;
     EditText _mobileText;
     EditText _passwordText;
     EditText _reEnterPasswordText;
     Button _signupButton;
     TextView _loginLink;
     int code;
     int user_id;
     String mobile;
     String password;
     String test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//        _nameText=findViewById(R.id.input_name);
//        _addressText=findViewById(R.id.input_address);
//        _emailText=findViewById(R.id.input_email1);
        _mobileText=findViewById(R.id.input_mobile);
        _passwordText=findViewById(R.id.input_password1);
        _reEnterPasswordText=findViewById(R.id.input_reEnterPassword);
        _signupButton=findViewById(R.id.btn_signup);
        _loginLink=findViewById(R.id.link_login);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在注册");
        progressDialog.show();

//        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
//        String email = _emailText.getText().toString();

        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();
        int code1=0;
        int user_id1=0;
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        try{
            URL url=new URL("http://139.224.232.186:8080/web/user/register");
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
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
//            while(()!=null){
//
//                }

            /*获取字符串里面的code的值*/
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
            code1=Integer.parseInt(temp_str);




            if(code1!=0){
                line=reader.readLine();
                line=line.substring(0,(line.length()-1));
                test=line;
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

//                int use_to_id=Integer.parseInt(temp_str);
//                user_id1=use_to_id;
//                JSONObject jsonobj1 = new JSONObject(line);
//                user_id1=jsonobj1.getInt("user_id");
            }

            writer.close();
            reader.close();
        }catch (IOException e){
            System.out.println(e.getMessage());

        }
//        catch (JSONException e) {
//
//            e.printStackTrace();
//        }
//        finally {
//            code1=1;
//        }
        // TODO: Implement your own signup logic here.

        code=code1;
        user_id=user_id1;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        if( code ==0){
                            onSignupFailed();
                        }
                        else{
                            onSignupSuccess();
                        }
                        progressDialog.dismiss();
                    }
                }, 1000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();
        SharedPreferences sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("mobile", mobile);
        editor.putString("password",password);
        editor.putInt("user_id", user_id);
        editor.commit();

        Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {

        Toast.makeText(getBaseContext(),"手机号已被注册" , Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

//        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
//        String email = _emailText.getText().toString();

        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

//        if (name.isEmpty() || name.length() < 3) {
//            _nameText.setError("at least 3 characters");
//            valid = false;
//        } else {
//            _nameText.setError(null);
//        }
//
//        if (address.isEmpty()) {
//            _addressText.setError("Enter Valid Address");
//            valid = false;
//        } else {
//            _addressText.setError(null);
//        }
//
//
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

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("密码不匹配");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }


}