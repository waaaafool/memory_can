package can.main_delete;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baiduspeechdialog.dialog.SpeechBottomSheetDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;
import can.aboutsqlite.Memocloud;
import can.aboutsqlite.User;
import can.live_assitcance.AppUsedService;
import can.live_assitcance.WeatherService;
import can.live_assitcance.live_assitance;
import can.memorycan.R;
import can.memorycan.memo_add.memo_add;
import io.reactivex.functions.Action;
import can.memorycan.speech;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = speech.class.getSimpleName();
    private Button mOpenSpeechDialogBtn;
    private TextView mResultTv;
    private ArrayList<Group_new> gData = null;
    private ArrayList<ArrayList<Memo>> iData = null;
    private ArrayList<Memo> lData = null;
    private Context mContext;
    private ExpandableListView list_memo;
    private ImageButton imagebotton_slide;
    private ImageButton imagebotton_delete;
    private ImageButton imagebotton_speak;
    private ImageButton imagebotton_add,igb_to_slider;
    private MyBaseExpandableListAdapter_new myAdapter = null;
    private Handler handle = new Handler();
    int user_id;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            this.update();
            handle.postDelayed(this,1000*1);
        }
        void update()
        {
            Notice_clock();
            set_iData();
            myAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DBManager mgr = new DBManager(this);
        SharedPreferences sp = getSharedPreferences("sp_demo", Context.MODE_PRIVATE);
        user_id=sp.getInt("user_id",1);

        mContext = MainActivity.this;
       int weather= mgr.getWeather_on(user_id);


        igb_to_slider=findViewById(R.id.imageButton_slide);
        igb_to_slider.setOnClickListener(new tosliderbar());

        imagebotton_add=findViewById(R.id.imageButton_add);
        imagebotton_add.setOnClickListener(new tomemoadd());

        list_memo = (ExpandableListView) findViewById(R.id.list_memo);
        imagebotton_slide = (ImageButton) findViewById(R.id.imageButton_slide);
        imagebotton_delete = (ImageButton) findViewById(R.id.imageButton_delete);
        imagebotton_add = (ImageButton) findViewById(R.id.imageButton_add);
        gData = new ArrayList<Group_new>();
        iData = new ArrayList<ArrayList<Memo>>();
        gData.add(new Group_new("近期待完成",1));
        gData.add(new Group_new("超时未完成",-1));
        gData.add(new Group_new("已完成任务",0));

        Notice_clock();
        set_iData();

        myAdapter = new MyBaseExpandableListAdapter_new(gData,iData,mContext, mgr);
        list_memo.setAdapter(myAdapter);
        if(list_memo!=null)
        {
            list_memo.expandGroup(0);
            list_memo.expandGroup(1);
        }
        final Intent detail = new Intent(MainActivity.this,memo_add.class);
        final Intent delete = new Intent(MainActivity.this,Delete.class);
        //对每一条备忘录点击的时候进行监听并跳转到详情界面
        list_memo.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                System.out.print("------");
                detail.putExtra("memo_id",iData.get(groupPosition).get(childPosition).getMemo_id());
                Bundle bundle = new Bundle();
                bundle.putString("test","false");
                detail.putExtras(bundle);
                startActivity(detail);
                return true;
            }
        });

        imagebotton_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                startActivity(delete);
            }
        });
        onePermission();
        initViews();
//        initEvents();
        mOpenSpeechDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开百度语音对话框
                SpeechBottomSheetDialog speechBottomSheetDialog = SpeechBottomSheetDialog.getInstance(MainActivity.this);
                speechBottomSheetDialog.seOnResultListItemClickListener(new SpeechBottomSheetDialog.OnResultListItemClickListener() {
                    @Override
                    public void onItemClick(String title) {
                        //填充到输入框中
//                        mResultTv.setText(title);
                        Memo memo = new Memo(title,
                                "9999-12-01 12:12:12",1,0,0,
                                0, 1,user_id,0,"：）");
                        Log.e("insert","insert");
                        mgr.insert_Memo(memo);
                        //iData.get(0).add(memo);
                        //myAdapter.notifyDataSetChanged();
//                        Intent self = new Intent(MainActivity.this,MainActivity.class);
//                        startActivity(self);
                    }
                });
                speechBottomSheetDialog.show(getSupportFragmentManager(), TAG);
            }
        });
        handle.postDelayed(runnable,1000*1);
    }

    public void set_iData()
    {
        iData.clear();
        final DBManager mgr = new DBManager(this);
        lData = new ArrayList<Memo>();
        lData = mgr.returnmemo2(user_id);
        iData.add(lData);

        lData = new ArrayList<Memo>();
        lData = mgr.returnmemo3(user_id);
        iData.add(lData);

        lData = new ArrayList<Memo>();
        lData = mgr.returnmemo1(user_id);
        iData.add(lData);
    }


    public void Notice_clock()
    {
        final DBManager mgr = new DBManager(this);
        ArrayList<Memocloud> Notice = new ArrayList();
        String all_to_notice = "";
        Notice = mgr.returnmemocloud(user_id);
        for(int i=0;i<Notice.size();i++)
        {
            if(System.currentTimeMillis() >= Notice.get(i).getMemo_dtime() - 720000 && System.currentTimeMillis() < Notice.get(i).getMemo_dtime())
            {
                if(all_to_notice=="") {
                    all_to_notice += Notice.get(i).getMemo_title();
                }
                else
                {
                    all_to_notice += "、 ";
                    all_to_notice += Notice.get(i).getMemo_title();
                }
            }
        }
        System.out.println("-----------------");
        System.out.println("这是判断一下通知什么妖怪东西" + all_to_notice);
        if(all_to_notice==""){}
        else
            sendNotify(all_to_notice);
    }

    protected void onDestroy(){
        handle.removeCallbacks(runnable);
        super.onDestroy();
    }

    public void initViews() {
        mOpenSpeechDialogBtn = findViewById(R.id.btn_openSpeechDialog);
        //mOpenSpeechLongDialogBtn = findViewById(R.id.btn_openSpeechLongDialog);
        mResultTv = findViewById(R.id.tv_result);
    }

    public void initEvents() {
        mOpenSpeechDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开百度语音对话框
                SpeechBottomSheetDialog speechBottomSheetDialog = SpeechBottomSheetDialog.getInstance(MainActivity.this);
                speechBottomSheetDialog.seOnResultListItemClickListener(new SpeechBottomSheetDialog.OnResultListItemClickListener() {
                    @Override
                    public void onItemClick(String title) {
                        //填充到输入框中
//                        mResultTv.setText(title);
                        Memo memo = new Memo(title,
                                "2018-11-14 23:00:00",2,0,0,
                                0, 1,1,0,"：）");
                    }
                });
                speechBottomSheetDialog.show(getSupportFragmentManager(), TAG);
            }
        });
    }

    /**只有一个运行时权限申请的情况*/
    private void onePermission(){
        RxPermissions rxPermissions = new RxPermissions(MainActivity.this); // where this is an Activity instance
        rxPermissions.request(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) //权限名称，多个权限之间逗号分隔开
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        Log.e(TAG, "{accept}granted=" + granted);//执行顺序——1【多个权限的情况，只有所有的权限均允许的情况下granted==true】
                        if (granted) { // 在android 6.0之前会默认返回true
                            // 已经获取权限
                        } else {
                            // 未获取权限
                            Toast.makeText(MainActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG,"{accept}");//可能是授权异常的情况下的处理
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG,"{run}");//执行顺序——2
                    }
                });
    }

    private class tosliderbar implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,can.sliderbar.sliderbar.class);
            startActivity(intent);
        }
    }

    private class tomemoadd implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,can.memorycan.memo_add.memo_add.class);
            Bundle bd=new Bundle();
            bd.putInt("memo_id",-1);
            intent.putExtras(bd);
            startActivity(intent);
        }
    }

    //消息发送到通知栏
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void sendNotify(String contentText){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知渠道
            CharSequence name = "MenoAlarm";
            String description = "MenoAlarm";
            String channelId="channelId2";//渠道id
            int importance = NotificationManager.IMPORTANCE_DEFAULT;//重要性级别
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
            mChannel.setDescription(description);//渠道描述
            mChannel.enableLights(true);//是否显示通知指示灯
            mChannel.enableVibration(true);//是否振动
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);//创建通知渠道
            //第二个参数与channelId对应
            Notification.Builder builder = new Notification.Builder(MainActivity.this,channelId);
            //icon title text必须包含，不然影响桌面图标小红点的展示
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("即将过期的备忘录")
                    .setContentText(contentText)
                    .setNumber(3); //久按桌面图标时允许的此条通知的数量

            //            Intent intent= new Intent(this,NotificationActivity.class);
            //            PendingIntent ClickPending = PendingIntent.getActivity(this, 0, intent, 0);
            //            builder.setContentIntent(ClickPending);
            notificationManager.notify(1,builder.build());
            //            Notification notify = builder.build();
        }else{
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(MainActivity.this);
            //icon title text必须包含，不然影响桌面图标小红点的展示
            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle("即将过期的备忘录")
                    .setContentText(contentText)
                    .setNumber(3); //久按桌面图标时允许的此条通知的数量
            notificationManager.notify(2,builder.build());
        }
    }


}