package can.main_delete;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;
import can.memorycan.R;
import can.memorycan.memo_add.memo_add;


public class Delete extends AppCompatActivity {

    private ArrayList<Group_new> gData = null;
    private ArrayList<ArrayList<Memo>> iData = null;
    private ArrayList<Memo> lData = null;
    private Context mContext;
    private ExpandableListView list_memo;
    private ImageButton imagebotton_slide;
    private ImageButton imagebotton_delete;
    private ImageButton imagebotton_speak;
    private ImageButton imagebotton_add;
    private Button delete_botton_back;
    private CheckBox delete_select_all;
    private Button delete_delete;
    private Delete_Adapter myAdapter = null;
    private Handler handle = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            this.update();
            handle.postDelayed(this,1000*2);
        }
        void update()
        {
            myAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_memo);
        final DBManager mgr = new DBManager(this);
        mContext = Delete.this;
        list_memo = (ExpandableListView) findViewById(R.id.delete_list_memo);
        delete_select_all = (CheckBox) findViewById(R.id.delete_select_all);
        delete_delete = (Button) findViewById(R.id.delete_delete);
        delete_botton_back = (Button)findViewById(R.id.delete_button_back);

        gData = new ArrayList<Group_new>();
        iData = new ArrayList<ArrayList<Memo>>();
        gData.add(new Group_new("近期待完成",1));
        gData.add(new Group_new("超时未完成",-1));
        gData.add(new Group_new("已完成任务",0));

        lData = new ArrayList<Memo>();
        lData = mgr.returnmemo2(1);
        iData.add(lData);

        lData = new ArrayList<Memo>();
        lData = mgr.returnmemo3(1);
        iData.add(lData);

        lData = new ArrayList<Memo>();
        lData = mgr.returnmemo1(1);
        iData.add(lData);

        myAdapter = new Delete_Adapter(gData,iData,mContext, mgr);
        list_memo.setAdapter(myAdapter);
        if(list_memo!=null)
        {
            list_memo.expandGroup(2);
        }
        final Intent detail = new Intent(Delete.this,memo_add.class);
        final Intent main = new Intent(Delete.this,MainActivity.class);
        list_memo.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                List<Integer> list = new ArrayList();
                for(int i=0;i<iData.get(2).size();i++)
                {
                    if(myAdapter.getIsSelected2().get(i))
                    {
                        long tmp1 = iData.get(groupPosition).get(childPosition).getMemo_id();
                        int tmp2 = new Long(tmp1).intValue();
                        list.add(tmp2);
                    }
                }
                mgr.deletedone(list);
//                Intent self = new Intent(Delete.this,Delete.class);
//                startActivity(self);
                Log.e("kkkkLLLLL","kkkkkLLLLL");
//                myAdapter.notifyDataSetChanged();
                return true;
            }
        });
        delete_select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    for(int i=0;i<iData.get(2).size();i++)
                    {
                        myAdapter.getIsSelected2().put(i,true);
                        Log.e("Is true!",String.valueOf(isChecked));
                        myAdapter.notifyDataSetChanged();
//                        myAdapter.notifyDataSetChanged();
                    }
                    mgr.deletedone(1);
                    iData.get(2).clear();
                }
                else
                {
                    for(int i=0;i<iData.get(2).size();i++)
                    {
                        myAdapter.getIsSelected2().put(i,false);
                        Log.e("Is true!",String.valueOf(isChecked));
                        myAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        delete_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> list = new ArrayList();
                for(int i=0;i<iData.get(2).size();i++)
                {
                    if(myAdapter.getIsSelected2().get(i))
                    {
                        long tmp1 = myAdapter.getChildId(2,i);
                        Log.e("KKKKKLLLLL","LLLLLLKKKKKK");
                        Log.e("NumNumNum",String.valueOf(tmp1));
                        int tmp2 = new Long(tmp1).intValue();
                        list.add(tmp2);
                    }
                }
                mgr.deletedone(list);
//                Intent self = new Intent(Delete.this,Delete.class);
//                startActivity(self);
//                Log.e("kkkkkkkkkk","kkkkkkkkkk");
                myAdapter.notifyDataSetChanged();
            }
        });
        delete_botton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(main);
            }
        });
        handle.postDelayed(runnable,1000*2);
    }
}