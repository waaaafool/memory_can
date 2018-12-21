package can.memo_add_details;
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import can.memorycan.R;

import static can.memo_add_details.ImageUtils.getimage;

public class memo_add_details extends AppCompatActivity {
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Bundle bundle;
    private Intent intent;

    //插入图片的Activity的返回的code
    static final int IMAGE_CODE = 99;
    static final int CAMERA=100;
    //数据库帮助类

    //控件申明

    EditText content;           //内容
    Uri photoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add_details);
        /*
         * 获取传递来的信息
         * */
        intent = getIntent();
        bundle = intent.getExtras();


        ImageButton imageButton_back = (ImageButton) findViewById(R.id.imageButton_back);
        imageButton_back.setOnClickListener(new MemoAdd());
        content =  findViewById(R.id.editText0);


        String temp=bundle.getString("m_content");
        content.setText(temp);

        String Input = content.getText().toString();

        initContent(Input);

        Button imageButton_preview = (Button) findViewById(R.id.imageButton_preview);
        imageButton_preview.setOnClickListener(new MemoAdd());

        ImageButton imageButton_pic = (ImageButton) findViewById(R.id.imageButton_pic);
        imageButton_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowChoise();
            }
        });

//        ImageButton imageButton_camera = (ImageButton) findViewById(R.id.imageButton_camera);
//        imageButton_camera.setOnClickListener(new TakePhoto());

    }

    private void ShowChoise()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(memo_add_details.this,android.R.style.Theme_Holo_Light_Dialog);
        //builder.setIcon(R.drawable.ic_launcher);
        // builder.setTitle("选择一个城市");
        //    指定下拉列表的显示数据
        final String[] cities = {"相册", "相机"};
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Toast.makeText(memo_add_details.this, "选择的城市为：" + cities[which], Toast.LENGTH_SHORT).show();
                if (cities[which]=="相册"){
                    callGallery();

                }
                if (cities[which]=="相机"){
                    takePhoto();
                }
            }
        });
        builder.show();
    }


    private class TakePhoto implements  View.OnClickListener{
        @Override
        public void onClick(View v){
            takePhoto();
        }
    }

    private class MemoAdd implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            bundle.putString("final_content",content.getText().toString());
            intent.putExtras(bundle);
            setResult(0x717,intent);//通过别人传过来的意图反向回去
            finish();
        }

    }
    private class MemoAdd_pic implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            callGallery();
        }

    }

    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            // intent.setType("image/*");
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            // intent.putExtra("uri" , photoUri);
            /**-----------------*/
            startActivityForResult(intent, CAMERA);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }
    private void callGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1001);
            }
        }else {
        }
        int permission = ActivityCompat.checkSelfPermission(memo_add_details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(memo_add_details.this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum, IMAGE_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bm = null;
        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        if(requestCode == IMAGE_CODE){
            try{
                // 获得图片的uri
                Uri originalUri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(resolver,originalUri);
                String[] proj = {MediaStore.Images.Media.DATA};
                // 好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(originalUri,proj,null,null,null);
                // 按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                // 最后根据索引值获取图片路径
                String path = cursor.getString(column_index);
                insertImg(path);
                //Toast.makeText(AddFlagActivity.this,path,Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(memo_add_details.this,"图片插入失败",Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == CAMERA) {
            String[] pojo = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE};
            Cursor cursor = getContentResolver().query(photoUri, pojo, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                String picpath = cursor.getString(cursor.getColumnIndexOrThrow(pojo[0]));
                if (picpath != null &&
                        (picpath.endsWith(".png") || picpath.endsWith(".PNG") || picpath.endsWith(".jpg"))) {
                    File file = new File(picpath);
                    Bitmap bt = BitmapFactory.decodeFile(picpath);
                    //content.setText(picpath);

                    insertImg(picpath);
                    bt = getimage(picpath);
                    // im.setImageBitmap(bt);

                } else {
                    Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }

        }

    }

    //region 插入图片
    private void insertImg(String path){
        String tagPath = "<img src=\""+path+"\"/>";//为图片路径加上<img>标签
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if(bitmap != null){
            SpannableString ss = getBitmapMime(path,tagPath);
            insertPhotoToEditText(ss);
            content.append("\n");
            Log.d("YYPT", content.getText().toString());
        }
    }
    //region 将图片插入到EditText中
    private void insertPhotoToEditText(SpannableString ss){
        Editable et = content.getText();
        int start = content.getSelectionStart();
        et.insert(start,ss);
        content.setText(et);
        content.setSelection(start+ss.length());
        content.setFocusableInTouchMode(true);
        content.setFocusable(true);
    }
    //endregion
    private SpannableString getBitmapMime(String path,String tagPath) {
        SpannableString ss = new SpannableString(tagPath);//这里使用加了<img>标签的图片路径

        int width = ScreenUtils.getScreenWidth(memo_add_details.this);
        int height = ScreenUtils.getScreenHeight(memo_add_details.this);




        Log.d("ScreenUtils", "高度:"+height+",宽度:"+width);

        Bitmap bitmap = getimage(path);

	        /*
	        //高:754，宽1008
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = 4;
	        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
	        */
        Log.d("YYPT_IMG_COMPRESS", "高度："+bitmap.getHeight()+",宽度:"+bitmap.getWidth());


        ImageSpan imageSpan = new ImageSpan(this, bitmap);
        ss.setSpan(imageSpan, 0, tagPath.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;


    }
    private void initContent(String Input){
        //input是获取将被解析的字符串
        //String input = flag.getContent().toString();
        String input= Input;
        //将图片那一串字符串解析出来,即<img src=="xxx" />
        Pattern p = Pattern.compile("\\<img src=\".*?\"\\/>");
        Matcher m = p.matcher(input);

        //使用SpannableString了，这个不会可以看这里哦：http://blog.sina.com.cn/s/blog_766aa3810100u8tx.html#cmt_523FF91E-7F000001-B8CB053C-7FA-8A0
        SpannableString spannable = new SpannableString(input);
        while(m.find()){
            //Log.d("YYPT_RGX", m.group());
            //这里s保存的是整个式子，即<img src="xxx"/>，start和end保存的是下标
            String s = m.group();
            int start = m.start();
            int end = m.end();
            //path是去掉<img src=""/>的中间的图片路径
            String path = s.replaceAll("\\<img src=\"|\"\\/>","").trim();
            //Log.d("YYPT_AFTER", path);

            //利用spannableString和ImageSpan来替换掉这些图片
            int width = ScreenUtils.getScreenWidth(memo_add_details.this);
            int height = ScreenUtils.getScreenHeight(memo_add_details.this);

            Bitmap bitmap = getimage(path);
            ImageSpan imageSpan = new ImageSpan(this, bitmap);
            spannable.setSpan(imageSpan,start,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        content.setText(spannable);
        content.append("\n");
        //Log.d("YYPT_RGX_SUCCESS",content.getText().toString());
    }


}