package can.memorycan.memo_add;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import can.aboutsqlite.*;
import can.main_delete.MainActivity;
import can.memo_add_details.memo_add_details;
import can.memorycan.memo_add.clock.widget.CustomDatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import can.memorycan.R;
import java.util.ArrayList;
import can.memorycan.memo_add.list_View.Group;
import can.memorycan.memo_add.list_View.Item;
import can.memorycan.memo_add.list_View.MyBaseExpandableListAdapter;
public class memo_add extends AppCompatActivity{
    /*定义控件变量*/
    private RelativeLayout selectDate, selectTime;
    private ImageView memo_add_back;
    private EditText editMemoTitle;
    private Spinner spinMoreSetting,listItemSpinner;
    private TextView currentDate, currentTime;
    private CustomDatePicker customDatePicker1, customDatePicker2;
    private ArrayList<Group> gData = null;
    private ArrayList<ArrayList<Item>> iData = null;
    private ArrayList<Item> lData = null;
    private Context mContext;
    private Button memo_add_save,memo_add_to_details;
    private ExpandableListView exlist_lol;
    private MyBaseExpandableListAdapter myAdapter = null;
    private Memo temp_memo;
    private DBManager mgr;
    private Bundle bundleFrom5;
    public int x=-1,y=-1;
    private TextView test;


    /*创建Memo所需的变量*/
    int memo_id;
    String memo_title;
    String s[];
    String memo_ctime="now";
    String memo_dtime="";
    int memo_priority;
    int memo_periodicity;
    int memo_advanced;
    int memo_remind;
    int memo_paper;
    int user_id;
    int memo_done;
    String memo_content;
    String first[]={"无","每周","每15天","每个月"};
    String second[]={"不提醒","提前十分钟","提前30分钟","提前1小时"};
    String thrid[]={"不提醒","闹钟","震动","闹钟加震动"};
    String fourth[]={"否","是"};

    String temp_str[]={"无","不提醒","不提醒","否"};
    String temp_str1[]=temp_str;

    int n;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add);
       SharedPreferences sp= getSharedPreferences("sp_demo",Context.MODE_PRIVATE);
        user_id=sp.getInt("user_id",1);
        mContext = memo_add.this;
        /*创建DATABASE*/
        mgr = new DBManager(this);
        temp_memo=new Memo();

        /*下面是获取交互的内容，从主页面获得n，也就是memo_id，n==-1是新建memo
        *memo_content是获得
        *
        * */
        Intent it=getIntent();
        Bundle bd=it.getExtras();
        n=bd.getInt("memo_id");

        System.out.println(n);

        memo_done=0;
        //memo_content="a我是中国人5";

        editMemoTitle=findViewById(R.id.memo_add_title);
        //   editMemoTitle.setOnClickListener(new class_addTitle());
        if(n!=-1){
            temp_memo=mgr.returnamemo(n);
            editMemoTitle.setText(temp_memo.getMemo_title());
            System.out.println(temp_memo.getMemo_title());
            memo_content=temp_memo.getMemo_content();
        }

        currentDate = (TextView) findViewById(R.id.currentDate);
        currentTime = (TextView) findViewById(R.id.currentTime);


        exlist_lol =  findViewById(R.id.exlist_lol);
        /*建立监听事件*/
        selectTime = (RelativeLayout) findViewById(R.id.selectTime);
        selectTime.setOnClickListener(new class_selectTime());

        /*返回按钮*/
        memo_add_back=(ImageButton)findViewById(R.id.memo_add_back);
        memo_add_back.setOnClickListener(new back_to_main());

        /*保存按钮*/
        memo_add_save=(Button)findViewById(R.id.memo_add_save);
        memo_add_save.setOnClickListener(new to_main());



        spinMoreSetting=findViewById(R.id.memo_add_priority);

        /*n==-1,就是创建新的备忘录*/

        /*从数据库获得优先级，并写进去*/
        String temp_priority="";
        if(temp_memo.getMemo_priority()==0) temp_priority="无";
        else if(temp_memo.getMemo_priority()==3) temp_priority="高";
        else if(temp_memo.getMemo_priority()==2) temp_priority="中";
        else if(temp_memo.getMemo_priority()==1) temp_priority="低";
        if(n!=-1) {
            SpinnerAdapter apsAdapter= spinMoreSetting.getAdapter();
            int k= apsAdapter.getCount();
            for(int i=0;i< k;i++){
                if(temp_priority.equals(apsAdapter.getItem(i).toString())){
                    spinMoreSetting.setSelection(i,true);
                    break;
                }
            }

        }
        spinMoreSetting.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                if(arg2==0){memo_priority=0;}
                else if(arg2==1){memo_priority=3;}
                else if(arg2==2){memo_priority=2;}
                else if(arg2==3){memo_priority=1;}
               // memo_priority=arg2;Toast.makeText(mContext, "你点击了："+arg2 , Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        if(n!=-1){
            String sdy=temp_memo.getmemo_dtimestring_memo_add();
            System.out.println(sdy);
            if(sdy!=null&&sdy.length()>3)
            sdy=sdy.substring(0,sdy.length()-3);
            else if(sdy.equals("")){
                sdy="9999-12-01 12:12";
            }
            currentTime.setText(sdy);
        }
        else {
            String SSSS="9999-12-01 12:12";
            currentTime.setText(SSSS);
        }

        initDatePicker();

        /*为了可折叠下拉列表准备spinner类型*/
        String[] time = getResources().getStringArray(R.array.time);
        ArrayAdapter<String> _Adapter0=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, time);
        String[] advancedTime = getResources().getStringArray(R.array.advanced_time);
        ArrayAdapter<String> _Adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, advancedTime);
        String[] remindType = getResources().getStringArray(R.array.remind_type);
        ArrayAdapter<String> _Adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, remindType);
        String[] switch1 = getResources().getStringArray(R.array.switch1);
        ArrayAdapter<String> _Adapter3=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, switch1);


        //数据准备
        gData = new ArrayList<Group>();
        iData = new ArrayList<ArrayList<Item>>();
        gData.add(new Group("更多设置"));
        lData = new ArrayList<Item>();

        //更多设置组
        lData.add(new Item(R.drawable.icon_generate,"任务周期性",_Adapter0));
        lData.add(new Item(R.drawable.icon_bell,"提前定时提醒",_Adapter1));
        lData.add(new Item(R.drawable.icon_tel,"到期提醒方式",_Adapter2));
        lData.add(new Item(R.drawable.icon_small_picture,"是否显示在锁屏",_Adapter3));
        iData.add(lData);

        if(n!=-1) {
            if (temp_memo.getMemo_periodicity() == 0) temp_str1[0] = "无";
            else if (temp_memo.getMemo_periodicity() == 1) temp_str1[0] = "每周";
            else if (temp_memo.getMemo_periodicity() == 2) temp_str1[0] = "每15天";
            else if (temp_memo.getMemo_periodicity() == 3) temp_str1[0] = "每个月";

            if (temp_memo.getMemo_advanced() == 0) temp_str1[1] = "不提醒";
            else if (temp_memo.getMemo_advanced() == 10) temp_str1[1] = "提前十分钟";
            else if (temp_memo.getMemo_advanced() == 30) temp_str1[1] = "提前30分钟";
            else if (temp_memo.getMemo_advanced() == 60) temp_str1[1] = "提前1小时";

            if (temp_memo.getMemo_remind() == 0) temp_str1[2] = "不提醒";
            else if (temp_memo.getMemo_remind() == 1) temp_str1[2] = "闹铃";
            else if (temp_memo.getMemo_remind() == 2) temp_str1[2] = "震动";
            else if (temp_memo.getMemo_remind() == 3) temp_str1[2] = "闹铃加震动";

            if(temp_memo.getMemo_paper()==0) temp_str1[3]="否";
            else temp_str1[3]="是";

        }

        //将Adapter加入方法创建myAdapter类
        myAdapter = new MyBaseExpandableListAdapter(gData,iData,mContext);
        exlist_lol.setAdapter(myAdapter);

        //默认的spinner的值
        myAdapter.setSet_spinner(temp_str1);

        s=myAdapter.sp_display();


        memo_add_to_details=findViewById(R.id.memo_add_to_details);
        memo_add_to_details.setOnClickListener(new to_details());

        test=findViewById(R.id.memo_add_test);

        test.setText(memo_content);



    }

    /*为每个监听创建类*/
    private class class_addTitle implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, s[0].equals(first[1])+memo_content+"  "+memo_dtime+"  "+memo_title+"  "+memo_priority+"  "+memo_remind+"  "+memo_periodicity+"  "+memo_paper+"  "+memo_advanced+"  "+s[0]+s[1]+s[2]+s[3], Toast.LENGTH_SHORT).show();
            memo_title=editMemoTitle.getText().toString();
        }
    }

    /*返回的监听类*/
    private class back_to_main implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(memo_add.this,MainActivity.class);
            startActivity(intent);
        }
    }
    private class to_details implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(memo_add.this,memo_add_details.class);
            Bundle bundle2=new Bundle();
            bundle2.putInt("id",n);
            /*下拉列表里面的值*/
            bundle2.putStringArray("s",s);
            bundle2.putString("memo_title",editMemoTitle.getText().toString());
            bundle2.putString("memo_dtime",currentTime.getText().toString());
            bundle2.putInt("memo_priority",memo_priority);
            if(n==-1){
                String str="  ";
                bundle2.putString("m_content",str);
            }
            else {
                bundle2.putString("m_content",memo_content);
            }

            intent.putExtras(bundle2);//这里测试一下用之前的可不可以
            startActivityForResult(intent,0x717);
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==0x717){
            bundleFrom5=data.getExtras();
            editMemoTitle.setText(bundleFrom5.getString("memo_title"));

            String temp_priority="";
            if(bundleFrom5.getInt("memo_priority")==0) temp_priority="无";
            else if(bundleFrom5.getInt("memo_priority")==3) temp_priority="高";
            else if(bundleFrom5.getInt("memo_priority")==2) temp_priority="中";
            else if(bundleFrom5.getInt("memo_priority")==1) temp_priority="低";
                SpinnerAdapter apsAdapter= spinMoreSetting.getAdapter();
                int k= apsAdapter.getCount();
                for(int i=0;i< k;i++){
                    if(temp_priority.equals(apsAdapter.getItem(i).toString())){
                        spinMoreSetting.setSelection(i,true);
                        break;
                    }
                }

            myAdapter.setSet_spinner(bundleFrom5.getStringArray("s"));

            memo_content=bundleFrom5.getString("final_content");
         //   Toast.makeText(memo_add.this,bundleFrom5.getString("final_content") , Toast.LENGTH_SHORT).show();
            test.setText(memo_content);

        }
    }
    /*保存的监听相应类*/
    private class to_main implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            Intent intent=new Intent(memo_add.this,MainActivity.class);
            startActivity(intent);
            memo_title=editMemoTitle.getText().toString();
            memo_dtime=currentTime.getText().toString()+":00";

            if(s[0].equals(first[0])) memo_periodicity=0;
            else if(s[0].equals(first[1])) memo_periodicity=1;
            else if(s[0].equals(first[2])) memo_periodicity=2;
            else if(s[0].equals(first[3]))  memo_periodicity=3;

            if(s[1].equals(second[0])) memo_advanced=0;
            else if(s[1].equals(second[1])) memo_advanced=10;
            else if(s[1].equals(second[2])) memo_advanced=30;
            else if(s[1].equals(second[3]))  memo_advanced=60;

            if(s[2].equals(thrid[0])) memo_remind=0;
            else if(s[2].equals(thrid[1])) memo_remind=1;
            else if(s[2].equals(thrid[2])) memo_remind=2;
            else if(s[2].equals(thrid[3]))  memo_remind=3;

            if(s[3].equals(fourth[0])) memo_paper=0;
            else if(s[3].equals(fourth[1])) memo_paper=1;
            if(n!=-1){
                temp_memo.setMemo_content(memo_content);
                temp_memo.setMemo_advanced(memo_advanced);
                temp_memo.setMemo_paper(memo_paper);
                temp_memo.setMemo_dtimestring(memo_dtime);
                temp_memo.setMemo_periodicity(memo_periodicity);
                temp_memo.setMemo_priority(memo_priority);
                temp_memo.setMemo_title(memo_title);
                mgr.update_Memo(temp_memo);
            }
            else{
                Memo final_memo=new  Memo(memo_title,memo_dtime,memo_priority,memo_periodicity, memo_advanced,memo_remind,memo_paper,user_id,memo_done,memo_content);
                mgr.insert_Memo(final_memo);
            }

        }
    }
    private  class class_selectTime implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            customDatePicker2.show(currentTime.getText().toString());
            //Toast.makeText(mContext, "你点击了：" +currentTime.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
    /*进行时间选择的初始化*/
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        currentDate.setText(now.split(" ")[0]);
        //currentTime.setText(now);

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentDate.setText(time.split(" ")[0]);
            }
        }, "2018-01-01 00:00", "2035-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentTime.setText(time);
            }
        }, "2018-01-01 00:00","2035-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }
    /*销毁活动时关闭DB*/
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

}
