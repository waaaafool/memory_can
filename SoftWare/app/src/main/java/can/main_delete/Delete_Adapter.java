package can.main_delete;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;
import can.memo_add_details.DataBaseUtil;
import can.memorycan.R;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Delete_Adapter extends BaseExpandableListAdapter {

    private ArrayList<Group_new> gData;
    private ArrayList<ArrayList<Memo>> iData;
    private static HashMap<Integer,Boolean> isSelected0;
    private static HashMap<Integer,Boolean> isSelected1;
    private static HashMap<Integer,Boolean> isSelected2;
    private DBManager mgr;
    private Context mContext;

    public Delete_Adapter(ArrayList<Group_new> gData,ArrayList<ArrayList<Memo>> iData, Context mContext, DBManager m) {
        this.gData = gData;
        this.iData = iData;
        this.mContext = mContext;
        isSelected0 = new HashMap<Integer, Boolean>();
        isSelected1 = new HashMap<Integer, Boolean>();
        isSelected2 = new HashMap<Integer, Boolean>();
        mgr = m;
        initDate();
    }
    private void initDate(){
        for(int i=0;i<iData.get(0).size();i++)
        {
            getIsSelected0().put(i,false);
        }
        for(int i=0;i<iData.get(1).size();i++)
        {
            getIsSelected1().put(i,false);
        }
        for(int i=0;i<iData.get(2).size();i++)
        {
            getIsSelected2().put(i,false);
        }
    }
    public static HashMap<Integer,Boolean> getIsSelected0() {
        return isSelected0;
    }
    public static HashMap<Integer,Boolean> getIsSelected1() {
        return isSelected1;
    }
    public static HashMap<Integer,Boolean> getIsSelected2() {
        return isSelected2;
    }

    public static void setIsSelected0(HashMap<Integer,Boolean> isSelected0) {
        Delete_Adapter.isSelected0 = isSelected0;
    }
    public static void setIsSelected1(HashMap<Integer,Boolean> isSelected1) {
        Delete_Adapter.isSelected1 = isSelected1;
    }
    public static void setIsSelected2(HashMap<Integer,Boolean> isSelected2) {
        Delete_Adapter.isSelected2 = isSelected2;
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
                    R.layout.delete_memo_group, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.delete_tv_group_name_new);
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
                    R.layout.delete_memo_item, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.ckb_name = (CheckBox) convertView.findViewById(R.id.delete_ckb_name_new);
            itemHolder.tv_name = (TextView) convertView.findViewById(R.id.delete_tv_name_new);
            if(groupPosition == 2) {
                itemHolder.ckb_name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            convertView.setTag(itemHolder);
            itemHolder.ckb_name.setTag(childPosition);
//            notifyDataSetChanged();
        }else{
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
//        itemHolder.ckb_name.setOnCheckedChangeListener(null);
        if(groupPosition==0)
        {
            itemHolder.ckb_name.setChecked(getIsSelected0().get(childPosition));
        }
        else if(groupPosition==1)
        {
            itemHolder.ckb_name.setChecked(getIsSelected1().get(childPosition));
        }
        else if(groupPosition==2)
        {
            itemHolder.ckb_name.setChecked(getIsSelected2().get(childPosition));
        }
        itemHolder.ckb_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    List<Integer> tmp_list = new ArrayList<>();
                    System.out.println("组的位置："+groupPosition+" 子项中的位置："+childPosition);
                    Memo memo = iData.get(groupPosition).get(childPosition);
                    tmp_list.add(iData.get(groupPosition).get(childPosition).getMemo_id());
                    System.out.println("现在正在试图将被勾选的备忘录从数据库中删除出去"+iData.get(groupPosition).get(childPosition).getMemo_id());
                    iData.get(groupPosition).remove(childPosition);
                    mgr.deletedone(tmp_list);
//                    notifyDataSetChanged();
                }
            }
        });

        itemHolder.ckb_name.setText(iData.get(index1).get(index2).getMemo_title());
        itemHolder.tv_name.setText(String.valueOf(iData.get(index1).get(index2).getmemo_dtimestring()));
//        if(groupPosition==0)
//        {
//            itemHolder.ckb_name.setChecked(getIsSelected0().get(childPosition));
//        }
//        else if(groupPosition==1)
//        {
//            itemHolder.ckb_name.setChecked(getIsSelected1().get(childPosition));
//        }
//        else if(groupPosition==2)
//        {
//            itemHolder.ckb_name.setChecked(getIsSelected2().get(childPosition));
//        }
        return convertView;
    }
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
//        int index = (int)buttonView.getTag();
//        if(isChecked)
//            iData.get(index).get(index).setCheckStatus(true);
//        else
//            iData.get(index).get(index).setCheckStatus(false);
//    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolderGroup{
        private TextView tv_group_name;
    }

    private static class ViewHolderItem{
        private TextView tv_name;
        private CheckBox ckb_name;
    }
}