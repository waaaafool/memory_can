package can.wallpaper;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import can.memorycan.R;
import can.memorycan.setting.setting;

public class wallpaper_generate extends AppCompatActivity {
    Spinner sp1,sp2,sp3;
    ImageButton imageButton_select,igb;
    String returnId;
    Button button_preview;
    int[] temp_sp={0,0,12};
    int pictureId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_generate);
        imageButton_select=(ImageButton) findViewById(R.id.imageButton_select);
        igb=findViewById(R.id.imageButton_back);
        igb.setOnClickListener(new tomain());
        button_preview=(Button)findViewById(R.id.imageButton_preview);
        sp1=findViewById(R.id.wallpaper_generate_display);
        sp2=findViewById(R.id.wallpaper_generate_color);
        sp3=findViewById(R.id.wallpaper_generate_fontsize);
        sp1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            //选择item的选择点击监听事件

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中

                String temp_string=sp1.getSelectedItem().toString();
                if(temp_string.equals("显示全部优先级")){
                    temp_sp[0]=0;
                }
                else if(temp_string.equals("只显示高优先级")){
                    temp_sp[0]=1;
                }
                else if(temp_string.equals("不显示低优先级")){
                    temp_sp[0]=2;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        sp2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            //选择item的选择点击监听事件

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中

                String temp_string=sp2.getSelectedItem().toString();
                if(temp_string.equals("白")){
                    temp_sp[1]=0;
                }
                else if(temp_string.equals("绿")){
                    temp_sp[1]=1;
                }
                else if(temp_string.equals("蓝")){
                    temp_sp[1]=2;
                }
                else if(temp_string.equals("黑"))
                {
                    temp_sp[1]=3;
                }
                else if(temp_string.equals("橙"))
                {
                    temp_sp[1]=4;
                }
                else if(temp_string.equals("粉"))
                {
                    temp_sp[1]=5;
                }
                else if(temp_string.equals("紫"))
                {
                    temp_sp[1]=6;
                }
                else if(temp_string.equals("黄"))
                {
                    temp_sp[1]=7;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        sp3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            //选择item的选择点击监听事件

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中

                String temp_string=sp3.getSelectedItem().toString();
                temp_sp[2]=Integer.parseInt(temp_string);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        Intent intent=getIntent();
        Bundle bd=intent.getExtras();
        if(bd!=null)
            pictureId=bd.getInt("id");

        imageButton_select.setOnClickListener(new MemoDisplay());
        button_preview.setOnClickListener(new Preview_picture());
    }
    private class MemoDisplay implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(wallpaper_generate.this,select_wallpaper.class);
            startActivityForResult(intent,1);
        }
    }
    public class Preview_picture implements View.OnClickListener{
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent();
            intent.setClass(wallpaper_generate.this,preview_picture.class);
            intent.putExtra("id",returnId);
            Bundle bd=new Bundle();
            bd.putInt("id",pictureId);
            bd.putIntArray("temp_sp",temp_sp);
            intent.putExtras(bd);
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1:
                if (resultCode == RESULT_OK)
                {
                    returnId = data.getStringExtra("id");
                    // Log.d("FirstActivity", returnId);
                }
                break;
            default:
        }
    }
    private class tomain implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(wallpaper_generate.this,can.main_delete.MainActivity.class);
            startActivity(intent);
        }
    }

}