package can.main_delete;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;
import can.memorycan.R;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class MyBaseExpandableListAdapter_new extends BaseExpandableListAdapter {

    private ArrayList<Group_new> gData;
    private ArrayList<ArrayList<Memo>> iData;
    private DBManager mgr;
    private Context mContext;

    public MyBaseExpandableListAdapter_new(ArrayList<Group_new> gData,ArrayList<ArrayList<Memo>> iData, Context mContext, DBManager m) {
        this.gData = gData;
        this.iData = iData;
        mgr = m;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return iData.get(groupPosition).size();
    }

    @Override
    public Group_new getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Memo getChild(int groupPosition, int childPosition) {
        return iData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public void change_gData(ArrayList<Group_new> group)
    {
        gData = group;
        notifyDataSetChanged();
    }

    public void change_iData(ArrayList<ArrayList<Memo>> item)
    {
        iData = item;
        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_exlist_group_new, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name_new);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.tv_group_name.setText(gData.get(groupPosition).get_Group_name());
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final int index1 = groupPosition;
        final int index2 = childPosition;
        ViewHolderItem itemHolder;
        convertView = null;
        if(convertView == null){
            Log.e("groupposition",String.valueOf(convertView));
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_exlist_item_new, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.ckb_name = (CheckBox) convertView.findViewById(R.id.ckb_name_new);
            itemHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_new);
            if(groupPosition == 2) {
                itemHolder.ckb_name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            convertView.setTag(itemHolder);
            itemHolder.ckb_name.setTag(childPosition);
//            notifyDataSetChanged();
        }else{
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        //对每个折叠列表中的子项进行监听是否被选中！
        itemHolder.ckb_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(groupPosition!=2) {
                        iData.get(2).add(iData.get(groupPosition).get(childPosition));
//                    System.out.println("组的位置："+groupPosition+" 子项中的位置："+childPosition);
//                    Log.e("这是组的位置",String.valueOf(groupPosition));
//                    Log.e("这是子项的位置",String.valueOf(childPosition));
//                    Log.e("这是当前组的大小",String.valueOf(iData.get(groupPosition).size()));
                        Memo memo = iData.get(groupPosition).get(childPosition);
                        System.out.println("现在正在试图将被勾选的备忘录从数据库中改变状态" + iData.get(groupPosition).get(childPosition).getMemo_id());
                        mgr.changestate(iData.get(groupPosition).get(childPosition).getMemo_id());
                        iData.get(groupPosition).remove(iData.get(groupPosition).get(childPosition));
                    }
                    else
                    {
                        iData.get(0).add(iData.get(groupPosition).get(childPosition));
                        Memo memo = iData.get(groupPosition).get(childPosition);
                        System.out.println("现在正在试图将被勾选的备忘录从数据库中改变状态" + iData.get(groupPosition).get(childPosition).getMemo_id());
                        mgr.changestate1(iData.get(groupPosition).get(childPosition).getMemo_id());
                        iData.get(groupPosition).remove(iData.get(groupPosition).get(childPosition));
                    }
                }
            }
        });

        itemHolder.ckb_name.setText(iData.get(index1).get(index2).getMemo_title());
        itemHolder.tv_name.setText(String.valueOf(iData.get(index1).get(index2).getmemo_dtimestring()));
        return convertView;
    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


//    public void add(Item item)
//    {
//        if(iData == null)
//        {
//            iData=new ArrayList<>();
//        }
//        iData.add(item);
//    }

    private static class ViewHolderGroup{
        private TextView tv_group_name;
    }

    private static class ViewHolderItem{
        private TextView tv_name;
        private CheckBox ckb_name;
    }
}