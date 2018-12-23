package can.sms;

/**
* 判断车票
*/


import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;


import java.util.ArrayList;

import can.aboutsqlite.DBManager;
import can.aboutsqlite.Memo;
import can.sms.smsMatch.SMSMatch;

/**
 * 数据库观察者
 */
public class TicketSmsDatabaseChaneObserver extends ContentObserver {
    // 只检查收件箱
    public static final Uri MMSSMS_ALL_MESSAGE_URI = Uri.parse("content://sms/inbox");
    public static final String SORT_FIELD_STRING = "_id asc";  // 排序
    public static final String DB_FIELD_ID = "_id";
    public static final String DB_FIELD_ADDRESS = "address";
    public static final String DB_FIELD_PERSON = "person";
    public static final String DB_FIELD_BODY = "body";
    public static final String DB_FIELD_DATE = "date";
    public static final String DB_FIELD_TYPE = "type";
    public static final String DB_FIELD_THREAD_ID = "thread_id";
    public static final String[] ALL_DB_FIELD_NAME = {
            DB_FIELD_ID, DB_FIELD_ADDRESS, DB_FIELD_PERSON, DB_FIELD_BODY,
            DB_FIELD_DATE, DB_FIELD_TYPE, DB_FIELD_THREAD_ID };
    public static int mMessageCount = -1;
    private static final long DELTA_TIME = 60 * 1000;

    private ArrayList<String> arrayList;
    private ContentResolver mResolver;
    private Handler mHandler;
    private int needTicketSMS;

    public TicketSmsDatabaseChaneObserver(ContentResolver resolver, Handler mHandler) {
        super(mHandler);
        mResolver = resolver;
        this.mHandler = mHandler;
        needTicketSMS = 0;
    }

    @Override
    public void onChange(boolean selfChange) {
        onReceiveSms();
    }

    private void onReceiveSms() {
        Cursor cursor = null;
        // 添加异常捕捉
        try {
            cursor = mResolver.query(MMSSMS_ALL_MESSAGE_URI, ALL_DB_FIELD_NAME,
                    null, null, SORT_FIELD_STRING);
            final int count = cursor.getCount();
            if (count <= mMessageCount) {
                mMessageCount = count;
                return;
            }
            // 发现收件箱的短信总数目比之前大就认为是刚接收到新短信---如果出现意外，请神保佑
            // 同时认为id最大的那条记录为刚刚新加入的短信的id---这个大多数是这样的，发现不一样的情况的时候可能也要求神保佑了
            mMessageCount = count;
            if (cursor != null) {
                cursor.moveToLast();
                final long smsdate = Long.parseLong(cursor.getString(cursor.getColumnIndex(DB_FIELD_DATE)));
                final long nowdate = System.currentTimeMillis();
                // 如果当前时间和短信时间间隔超过60秒,认为这条短信无效
                if (nowdate - smsdate > DELTA_TIME) {
                    return;
                }
                final String strAddress = cursor.getString(cursor.getColumnIndex(DB_FIELD_ADDRESS));    // 短信号码
                final String strbody = cursor.getString(cursor.getColumnIndex(DB_FIELD_BODY));          // 在这里获取短信信息
                final int smsid = cursor.getInt(cursor.getColumnIndex(DB_FIELD_ID));
                if (TextUtils.isEmpty(strAddress) || TextUtils.isEmpty(strbody)) {
                    return;
                }
                // 得到短信号码和内容之后进行相关处理
                int update = setArrayList(strbody);
                //System.out.println(update + "-----------------------------------------------------------------" + strbody);
                mHandler.obtainMessage(update, arrayList).sendToTarget();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {  // 有可能cursor都没有创建成功
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //@return update 是否需要生成便签
    private int setArrayList(String strbody) {

        SMSMatch mSMSMatch = new SMSMatch(strbody);

        int update = 0;

        int isTicketSMS = mSMSMatch.getIsTicketSMS();

        setParam();

        if (needTicketSMS != 0) {
            if (needTicketSMS != 0 && isTicketSMS != 0) {
                arrayList = mSMSMatch.getArrayList();
                System.out.println("按要求输出车票短信关键字：");
                System.out.println("标题：" + arrayList.get(0));
                System.out.println("截止时间：" + arrayList.get(1));
                System.out.println("----------------------");
                System.out.println(arrayList.get(2));
                if (arrayList.get(0) != null)
                    update = 1;
            }
//            test.getTitle();
//            test.getTicketEndTime();
        } else {
            System.out.println("未开启短信分析功能");
        }
        return update;
    }

    private void setParam() {
        DBManager mgr;
        mgr=new DBManager(Appcontext.getContext());
        needTicketSMS=mgr.getTrip_on(1);
    }

}
