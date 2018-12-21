package can.appwidget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;
import can.sms.Appcontext;

public class ListViewService extends RemoteViewsService {
    public static final String INITENT_DATA = "extra_data";
    private DBManager mgr;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;

        private List<String> mList = new ArrayList<>();

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {//插入备忘录的内容
            mgr= new DBManager(ListViewService.this);
            ArrayList<Memo> list=mgr.returnmemo2(1);
            /*需要数据库返回带优先级的列表*/
            /*添加文字信息*/
//            String temp_s="   ";
//            String use_0=list.get(0).getMemo_title()+temp_s+list.get(0).getmemo_dtimestring();
//            String use_1=list.get(1).getMemo_title()+temp_s+list.get(1).getmemo_dtimestring();
//            String use_2=list.get(2).getMemo_title()+temp_s+list.get(2).getmemo_dtimestring();
//            String use_3=list.get(3).getMemo_title()+temp_s+list.get(3).getmemo_dtimestring();
//            String use_4=list.get(4).getMemo_title()+temp_s+list.get(4).getmemo_dtimestring();

            String use_0=list.get(0).getMemo_title();
            String use_1=list.get(1).getMemo_title();
            String use_2=list.get(2).getMemo_title();
            String use_3=list.get(3).getMemo_title();
            String use_4=list.get(4).getMemo_title();
            mList.add(use_0);
            mList.add(use_1);
            mList.add(use_2);
            mList.add(use_3);
            mList.add(use_4);
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            mList.clear();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
            views.setTextViewText(android.R.id.text1, mList.get(position));

            Bundle extras = new Bundle();
            extras.putInt(ListViewService.INITENT_DATA, position);
            Intent changeIntent = new Intent();
            changeIntent.setAction(can.appwidget.MulAppWidgetProvider.CHANGE_IMAGE);
            changeIntent.putExtras(extras);

            /* android.R.layout.simple_list_item_1 --- id --- text1
             * listview的item click：将 changeIntent 发送，
             * changeIntent 它默认的就有action 是provider中使用 setPendingIntentTemplate 设置的action*/
            views.setOnClickFillInIntent(android.R.id.text1, changeIntent);
            return views;
        }

        /* 在更新界面的时候如果耗时就会显示 正在加载... 的默认字样，但是你可以更改这个界面
         * 如果返回null 显示默认界面
         * 否则 加载自定义的，返回RemoteViews
         */
        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}