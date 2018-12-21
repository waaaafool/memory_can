package can.wallpaper;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;
import can.memo_add_details.memo_add_details;
import can.memorycan.R;

import java.io.IOException;
import java.util.ArrayList;

public class preview_picture extends AppCompatActivity {
    private DBManager mgr;
    private Button buttom;
    private ImageView imageView;
    private ArrayList<Memo> lData = null;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private TextView textView9;
    private TextView textView10;
    private Button preview_picture_generate;
    public int judge_flag;
    public View view;
    public int width;
    public int  height;
    public Bitmap bitmap;
    public int size;
    int pictureId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_picture);
        buttom=findViewById(R.id.button1);
        imageView=findViewById(R.id.imageView);
        buttom.setOnClickListener(new Wall_generate());
        preview_picture_generate=findViewById(R.id.preview_picture_generate);
        preview_picture_generate.setOnClickListener(new generate());
        textView1=findViewById(R.id.textView1);
        textView2=findViewById(R.id.textView2);
        textView3=findViewById(R.id.textView3);
        textView4=findViewById(R.id.textView4);
        textView5=findViewById(R.id.textView5);
        textView6=findViewById(R.id.textView6);
        textView7=findViewById(R.id.textView7);
        textView8=findViewById(R.id.textView8);
        textView9=findViewById(R.id.textView9);
        textView10=findViewById(R.id.textView10);
        mgr=new DBManager(this);
        ArrayList<Memo> list=mgr.returnmemo2(1);
        size=list.size();
        /*需要数据库返回带优先级的列表*/
        /*添加文字信息*/
        if(size>=5) {
            String temp_s = "     ";
            String use_0 = list.get(0).getMemo_title() + temp_s ;
            String use_1 = list.get(1).getMemo_title() + temp_s ;
            String use_2 = list.get(2).getMemo_title() + temp_s ;
            String use_3 = list.get(3).getMemo_title() + temp_s ;
            String use_4 = list.get(4).getMemo_title() + temp_s ;
            String use_5 =  list.get(0).getmemo_dtimestring();
            String use_6 =  list.get(1).getmemo_dtimestring();
            String use_7 =  list.get(2).getmemo_dtimestring();
            String use_8 =  list.get(3).getmemo_dtimestring();
            String use_9 =  list.get(4).getmemo_dtimestring();
            textView1.setText(use_0);
            textView2.setText(use_5);
            textView3.setText(use_1);
            textView4.setText(use_6);
            textView5.setText(use_2);
            textView6.setText(use_7);
            textView7.setText(use_3);
            textView8.setText(use_8);
            textView9.setText(use_4);
            textView10.setText(use_9);
        }
        else if(size==1)
        {
            String use_0 = list.get(0).getMemo_title();
            String use_1 = list.get(0).getmemo_dtimestring();
            textView1.setText(use_0);
            textView2.setText(use_1);
        }
        else if(size==2)
        {
            String use_0 = list.get(0).getMemo_title();
            String use_1 = list.get(0).getmemo_dtimestring();
            textView1.setText(use_0);
            textView2.setText(use_1);

            String use_2 = list.get(1).getMemo_title();
            String use_3 = list.get(1).getmemo_dtimestring();

            textView3.setText(use_2);
            textView4.setText(use_3);
        }
        else if(size==3)
        {
            String use_0 = list.get(0).getMemo_title();
            String use_1 = list.get(0).getmemo_dtimestring();

            textView1.setText(use_0);
            textView2.setText(use_1);

            String use_2 = list.get(1).getMemo_title();
            String use_3 = list.get(1).getmemo_dtimestring();

            textView3.setText(use_2);
            textView4.setText(use_3);

            String use_5 = list.get(2).getMemo_title();
            String use_6 = list.get(2).getmemo_dtimestring();

            textView3.setText(use_5);
            textView4.setText(use_6);
        }
        else if(size==4)
        {
            String use_0 = list.get(0).getMemo_title();
            String use_1 = list.get(0).getmemo_dtimestring();

            textView1.setText(use_0);
            textView2.setText(use_1);

            String use_2 = list.get(1).getMemo_title();
            String use_3 = list.get(1).getmemo_dtimestring();

            textView3.setText(use_2);
            textView4.setText(use_3);

            String use_5 = list.get(2).getMemo_title();
            String use_6 = list.get(2).getmemo_dtimestring();

            textView3.setText(use_5);
            textView4.setText(use_6);

            String use_7 = list.get(3).getMemo_title();
            String use_8 = list.get(4).getmemo_dtimestring();

            textView3.setText(use_7);
            textView4.setText(use_8);
        }

        Intent intent = getIntent();
        Bundle bd=intent.getExtras();
        int[] temp_sp=bd.getIntArray("temp_sp");

        assert temp_sp != null;
        /*缺优先级的展示*/

        /*判断颜色*/
        if(temp_sp[1]==0) {
            textView1.setTextColor(Color.WHITE);
            textView2.setTextColor(Color.WHITE);
            textView3.setTextColor(Color.WHITE);
            textView4.setTextColor(Color.WHITE);
            textView5.setTextColor(Color.WHITE);
            textView6.setTextColor(Color.WHITE);
            textView7.setTextColor(Color.WHITE);
            textView8.setTextColor(Color.WHITE);
            textView9.setTextColor(Color.WHITE);
            textView10.setTextColor(Color.WHITE);
        }
        else if(temp_sp[1]==1){
            textView1.setTextColor(this.getResources().getColor(R.color.color9));
            textView2.setTextColor(this.getResources().getColor(R.color.color9));
            textView3.setTextColor(this.getResources().getColor(R.color.color9));
            textView4.setTextColor(this.getResources().getColor(R.color.color9));
            textView5.setTextColor(this.getResources().getColor(R.color.color9));
            textView6.setTextColor(this.getResources().getColor(R.color.color9));
            textView7.setTextColor(this.getResources().getColor(R.color.color9));
            textView8.setTextColor(this.getResources().getColor(R.color.color9));
            textView9.setTextColor(this.getResources().getColor(R.color.color9));
            textView10.setTextColor(this.getResources().getColor(R.color.color9));
        }
        else if(temp_sp[1]==2){
            textView1.setTextColor(this.getResources().getColor(R.color.color5));
            textView2.setTextColor(this.getResources().getColor(R.color.color5));
            textView3.setTextColor(this.getResources().getColor(R.color.color5));
            textView4.setTextColor(this.getResources().getColor(R.color.color5));
            textView5.setTextColor(this.getResources().getColor(R.color.color5));
            textView6.setTextColor(this.getResources().getColor(R.color.color5));
            textView7.setTextColor(this.getResources().getColor(R.color.color5));
            textView8.setTextColor(this.getResources().getColor(R.color.color5));
            textView9.setTextColor(this.getResources().getColor(R.color.color5));
            textView10.setTextColor(this.getResources().getColor(R.color.color5));
        }
        else if(temp_sp[1]==3){
            textView1.setTextColor(Color.BLACK);
            textView2.setTextColor(Color.BLACK);
            textView3.setTextColor(Color.BLACK);
            textView4.setTextColor(Color.BLACK);
            textView5.setTextColor(Color.BLACK);
            textView6.setTextColor(Color.BLACK);
            textView7.setTextColor(Color.BLACK);
            textView8.setTextColor(Color.BLACK);
            textView9.setTextColor(Color.BLACK);
            textView10.setTextColor(Color.BLACK);
        }
        else if(temp_sp[1]==4){
            textView1.setTextColor(this.getResources().getColor(R.color.color8));
            textView2.setTextColor(this.getResources().getColor(R.color.color8));
            textView3.setTextColor(this.getResources().getColor(R.color.color8));
            textView4.setTextColor(this.getResources().getColor(R.color.color8));
            textView5.setTextColor(this.getResources().getColor(R.color.color8));
            textView6.setTextColor(this.getResources().getColor(R.color.color8));
            textView7.setTextColor(this.getResources().getColor(R.color.color8));
            textView8.setTextColor(this.getResources().getColor(R.color.color8));
            textView9.setTextColor(this.getResources().getColor(R.color.color8));
            textView10.setTextColor(this.getResources().getColor(R.color.color8));
        }
        else if(temp_sp[1]==5){
            textView1.setTextColor(this.getResources().getColor(R.color.color6));
            textView2.setTextColor(this.getResources().getColor(R.color.color6));
            textView3.setTextColor(this.getResources().getColor(R.color.color6));
            textView4.setTextColor(this.getResources().getColor(R.color.color6));
            textView5.setTextColor(this.getResources().getColor(R.color.color6));
            textView6.setTextColor(this.getResources().getColor(R.color.color6));
            textView7.setTextColor(this.getResources().getColor(R.color.color6));
            textView8.setTextColor(this.getResources().getColor(R.color.color6));
            textView9.setTextColor(this.getResources().getColor(R.color.color6));
            textView10.setTextColor(this.getResources().getColor(R.color.color6));
        }
        else if(temp_sp[1]==6){
            textView1.setTextColor(this.getResources().getColor(R.color.color7));
            textView2.setTextColor(this.getResources().getColor(R.color.color7));
            textView3.setTextColor(this.getResources().getColor(R.color.color7));
            textView4.setTextColor(this.getResources().getColor(R.color.color7));
            textView5.setTextColor(this.getResources().getColor(R.color.color7));
            textView6.setTextColor(this.getResources().getColor(R.color.color7));
            textView7.setTextColor(this.getResources().getColor(R.color.color7));
            textView8.setTextColor(this.getResources().getColor(R.color.color7));
            textView9.setTextColor(this.getResources().getColor(R.color.color7));
            textView10.setTextColor(this.getResources().getColor(R.color.color7));
        }
        else if(temp_sp[1]==7){
            textView1.setTextColor(this.getResources().getColor(R.color.color10));
            textView2.setTextColor(this.getResources().getColor(R.color.color10));
            textView3.setTextColor(this.getResources().getColor(R.color.color10));
            textView4.setTextColor(this.getResources().getColor(R.color.color10));
            textView5.setTextColor(this.getResources().getColor(R.color.color10));
            textView6.setTextColor(this.getResources().getColor(R.color.color10));
            textView7.setTextColor(this.getResources().getColor(R.color.color10));
            textView8.setTextColor(this.getResources().getColor(R.color.color10));
            textView9.setTextColor(this.getResources().getColor(R.color.color10));
            textView10.setTextColor(this.getResources().getColor(R.color.color10));
        }
        /*设置字体*/
        textView1.setTextSize(temp_sp[2]);
        textView2.setTextSize(temp_sp[2]);
        textView3.setTextSize(temp_sp[2]);
        textView4.setTextSize(temp_sp[2]);
        textView5.setTextSize(temp_sp[2]);
        textView6.setTextSize(temp_sp[2]);
        textView7.setTextSize(temp_sp[2]);
        textView8.setTextSize(temp_sp[2]);
        textView9.setTextSize(temp_sp[2]);
        textView10.setTextSize(temp_sp[2]);


        pictureId = bd.getInt("id");
        if(pictureId==1){
            imageView.setBackgroundResource(R.drawable.wallpaper1);
        }
        else if(pictureId==2){
            imageView.setBackgroundResource(R.drawable.wallpaper2);
        }
        else if(pictureId==3){
            imageView.setBackgroundResource(R.drawable.wallpaper3);
        }
        else if(pictureId==4){
            imageView.setBackgroundResource(R.drawable.wallpaper4);
        }
        else if(pictureId==5){
            imageView.setBackgroundResource(R.drawable.wallpaper15);
        }
        else if(pictureId==6){
            imageView.setBackgroundResource(R.drawable.wallpaper16);
        }
        else if(pictureId==7){
            imageView.setBackgroundResource(R.drawable.wallpaper13);
        }
        else if(pictureId==8){
            imageView.setBackgroundResource(R.drawable.wallpaper14);
        }
        else if(pictureId==9){
            imageView.setBackgroundResource(R.drawable.wallpaper9);
        }
        else if(pictureId==10){
            imageView.setBackgroundResource(R.drawable.wallpaper10);
        }
        else if(pictureId==11){
            imageView.setBackgroundResource(R.drawable.wallpaper11);
        }
        else if(pictureId==12){
            imageView.setBackgroundResource(R.drawable.wallpaper12);
        }
    }
    public class Wall_generate implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(preview_picture.this, wallpaper_generate.class);
            startActivity(intent);
        }

    }
    public  Bitmap imageCrop(Bitmap b) {
        // 得到图片的宽，高
        int w = b.getWidth();
        int h = b.getHeight();
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        b = Bitmap.createBitmap(b, 0, 2*statusBarHeight, width, height - 2*statusBarHeight);
        return b;
    }
    public class generate implements View.OnClickListener{
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            //获取当前屏幕的大小
            width = getWindow().getDecorView().getRootView().getWidth();
            height = getWindow().getDecorView().getRootView().getHeight();
            //生成相同大小的图片
            bitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
            //找到当前页面的跟布局
            view = getWindow().getDecorView().getRootView();
            //设置缓存
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            //从缓存中获取当前屏幕的图片
            bitmap = view.getDrawingCache();
            //imageView.setImageBitmap(bitmap);
            bitmap=imageCrop(bitmap);
            SetLockWallPaper();       //设置锁屏壁纸
        }
    }

    public void SetWallPaper() {
        WallpaperManager mWallManager = WallpaperManager.getInstance(this);
        try {
            Resources res = preview_picture.this.getResources();
            Bitmap bitmap;
            if(judge_flag==0){
                Bitmap bitmap1= BitmapFactory.decodeResource(res, R.drawable.wallpaper1);
                bitmap=bitmap1;
                Toast.makeText(preview_picture.this, "壁纸设置成功0", Toast.LENGTH_SHORT)
                        .show();
            }
            else{
                Bitmap bitmap1= BitmapFactory.decodeResource(res, R.drawable.wallpaper2);
                bitmap=bitmap1;
                Toast.makeText(preview_picture.this, "壁纸设置成功1", Toast.LENGTH_SHORT)
                        .show();
            }
            mWallManager.setBitmap(bitmap);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SetLockWallPaper() {
        // TODO Auto-generated method stub
        WallpaperManager mWallManager = WallpaperManager.getInstance(this);

        try {

            mWallManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK);
            Toast.makeText(preview_picture.this,"壁纸生成成功",Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }



}