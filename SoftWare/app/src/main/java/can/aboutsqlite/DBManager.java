package can.aboutsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

/**
 * Created by dale on 2018/11/04.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private static final String TABLE1_NAME = "user";
    private static final String TABLE2_NAME = "memo";
    private static final String TABLE3_NAME = "picture";
    private static final String TABLE4_NAME = "audio";
    // private static final String TABLE5_NAME = "wallpaper";


    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /*查询本地用户表是否存在用户*/
    public boolean  User_exist(int user_id){
        boolean isexist=false;
        String sql="select user_id from user where user_id ="+user_id;
        int user=0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            user=c.getInt(0);
            if(user==user_id)
                isexist=true;
            c.close();
        }else {
            c.close();

        }return isexist;
    }
    /*获取本地的用户详细信息*/
    public User returnUser(int user_id){
        String sql= "select * from "+TABLE1_NAME+" where user_id ="+user_id;
        Cursor c=null;
        User user=new User();
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                user.setUser_id(c.getInt((c.getColumnIndex("user_id"))));
                user.setUser_tel(c.getString((c.getColumnIndex("user_tel"))));
                user.setUser_password(c.getString((c.getColumnIndex("user_password"))));
                user.setTrip_on(c.getInt((c.getColumnIndex("trip_on"))));
                user.setTrip_paper(c.getInt((c.getColumnIndex("trip_paper"))));
                user.setTrip_priority(c.getInt((c.getColumnIndex("trip_priority"))));
                user.setParcel_on(c.getInt((c.getColumnIndex("parcel_on"))));
                user.setParcel_priority(c.getInt((c.getColumnIndex("parcel_priority"))));
                user.setParcel_paper(c.getInt((c.getColumnIndex("parcel_paper"))));
                user.setWeather_on(c.getInt((c.getColumnIndex("weather_on"))));
                user.setWeather_priority(c.getInt((c.getColumnIndex("weather_priority"))));
                user.setWeather_paper(c.getInt((c.getColumnIndex("weather_paper"))));
                user.setAnalysis_on(c.getInt((c.getColumnIndex("analysis_on"))));
                user.setAnalysis_priority(c.getInt((c.getColumnIndex("analysis_priority"))));
                user.setAnalysis_paper(c.getInt((c.getColumnIndex("analysis_paper"))));
                user.setWidget(c.getInt((c.getColumnIndex("widget"))));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }

        return user;
    }


    /*同步云端账户数据到本地：插入*/
    public void insert_User(User user){
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("user_id",user.getUser_id());
            cv.put("user_password",user.getUser_password());
            cv.put("user_tel",user.getUser_tel());
            cv.put("trip_on",user.getTrip_on());
            cv.put("trip_priority",user.getTrip_priority());
            cv.put("trip_paper",user.getTrip_paper());
            cv.put("parcel_on",user.getParcel_on());
            cv.put("parcel_priority",user.getParcel_priority());
            cv.put("parcel_paper",user.getParcel_paper());
            cv.put("analysis_on",user.getAnalysis_on());
            cv.put("analysis_priority",user.getAnalysis_priority());
            cv.put("analysis_paper",user.getAnalysis_paper());
            cv.put("weather_on",user.getWeather_on());
            cv.put("weather_priority",user.getWeather_priority());
            cv.put("weather_paper",user.getWeather_paper());
            cv.put("widget",user.getWidget());
            db.insert("user",null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }

    /*同步云端账户数据到本地:更新*/
    public void update_User(User user){
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("user_password",user.getUser_password());
            cv.put("user_tel",user.getUser_tel());
            cv.put("trip_on",user.getTrip_on());
            cv.put("trip_priority",user.getTrip_priority());
            cv.put("trip_paper",user.getTrip_paper());
            cv.put("parcel_on",user.getParcel_on());
            cv.put("parcel_priority",user.getParcel_priority());
            cv.put("parcel_paper",user.getParcel_paper());
            cv.put("analysis_on",user.getAnalysis_on());
            cv.put("analysis_priority",user.getAnalysis_priority());
            cv.put("analysis_paper",user.getAnalysis_paper());
            cv.put("weather_on",user.getWeather_on());
            cv.put("weather_priority",user.getWeather_priority());
            cv.put("weather_paper",user.getWeather_paper());
            cv.put("widget",user.getWidget());
            String[] args = {String.valueOf(user.getUser_id())};
            db.update("user", cv, "user_id=?",args);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }

    /*根据用户id删除本地的memo，以便接下来的同步*/
    public void Deletememo_by_uid(int user_id){
        db.execSQL("delete from memo where user_id="+user_id);
    }


    /*创建账户*/
    public void insert_User(int user_id,String user_password,String user_tel){
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("user_id",user_id);
            cv.put("user_password",user_password);
            cv.put("user_tel",user_tel);
            db.insert(TABLE1_NAME,null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }


    /*修改账户密码*/
    public void changeUser_password(int user_id,String user_password){
        db.execSQL("update user set user_password= ? where  user_id=?", new Object[]{ user_id,user_password });
    }

    /*用户获取密码*/
    public String getUser_password(int user_id){
        String sql="select user_password from user where user_id ="+user_id;

        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            String user_pw=c.getString(0);
            c.close();
            return user_pw;
        }else {
            c.close();
            return "none";//返回用户不存在
        }
    }

    /*获取用户的是否开启快递短信*/
    public int  getParcel_on(int user_id){
        String sql="select parcel_on from user where user_id ="+user_id;
        int parcel_on=0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            parcel_on=c.getInt(0);
            c.close();
        }else {
            c.close();

        }return parcel_on;
    }

    /*获取用户的是否开启天气功能*/
    public int  getWeather_on(int user_id){
        String sql="select weather_on from user where user_id ="+user_id;
        int weather_on=0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            weather_on=c.getInt(0);
            c.close();
        }else {
            c.close();

        }return weather_on;
    }

    /*获取用户的是否开启车票短信*/
    public int  getTrip_on(int user_id){
        String sql="select trip_on from user where user_id ="+user_id;
        int trip_on =0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            trip_on =c.getInt(0);
            c.close();

        }else {
            c.close();
        } return trip_on;
    }

    /*获取用户的是否开启用户分析功能*/
    public int  getAnalysis_on(int user_id){
        String sql="select analysis_on from user where user_id ="+user_id;
        int analysis_on=0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            analysis_on=c.getInt(0);
            c.close();
        }else {
            c.close();

        }return analysis_on;
    }

    /*获取用户的是否开启小控件功能*/
    public int  getWidget(int user_id){
        String sql="select widget from user where user_id ="+user_id;
        int widget=0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            widget=c.getInt(0);
            c.close();
        }else {
            c.close();

        }return widget;
    }

    /*获取用户的车票短信优先级*/
    public int  getTrip_priority(int user_id){
        String sql="select trip_priority from user where user_id ="+user_id;
        int trip_priority =0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            trip_priority =c.getInt(0);
            c.close();

        }else {
            c.close();
        } return trip_priority;
    }

    /*获取用户的快递短信优先级*/
    public int  getParcel_priority(int user_id){
        String sql="select parcel_priority from user where user_id ="+user_id;
        int parcel_priority =0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            parcel_priority =c.getInt(0);
            c.close();

        }else {
            c.close();
        } return parcel_priority;
    }

    //创建一个对象，赋属性值，memoid的值不需要填写，然后调用insertmemo接口把对象传进来，然后调用getmemoid函数获取memoid写入属性值
    /*创建语音memo*/
    public  void insert_VoiceMemo(String voice,int use_id) {
        Memo memo =new Memo();
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("memo_title",voice);
            cv.put("memo_ctime",System.currentTimeMillis());
            cv.put("user_id",use_id);
            cv.put("memo_done",memo.getMemo_done());
            cv.put("memo_content",memo.getMemo_content());
            db.insert(TABLE2_NAME,null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }

    /*创建车票快递备忘录*/
    public  void insert_TriporDeliveryMemo(String memo_title,String memo_dtime) {
        Memo memo=new Memo();
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("memo_title",memo.getMemo_title());
            cv.put("memo_ctime",memo.getMemo_ctime());
            cv.put("memo_dtime",memo.getMemo_dtime());
            cv.put("memo_priority",memo.getMemo_priority());
            cv.put("memo_periodicity",memo.getMemo_periodicity());
            cv.put("memo_advanced",memo.getMemo_advanced());
            cv.put("memo_remind",memo.getMemo_remind());
            cv.put("memo_paper",memo.getMemo_paper());
            cv.put("user_id",memo.getUser_id());  // 主码自增
            cv.put("memo_done",memo.getMemo_done());
            cv.put("memo_content",memo.getMemo_content());
            db.insert(TABLE2_NAME,null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }

    /*插入云端返回的备忘录*/
    public  void insert_MemoCloud(Memocloud memo) {
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("memo_title",memo.getMemo_title());
            cv.put("memo_ctime",memo.getMemo_ctime());
            cv.put("memo_dtime",memo.getMemo_dtime());
            cv.put("memo_priority",memo.getMemo_priority());
            cv.put("memo_periodicity",memo.getMemo_periodicity());
            cv.put("memo_advanced",memo.getMemo_advanced());
            cv.put("memo_remind",memo.getMemo_remind());
            cv.put("memo_paper",memo.getMemo_paper());
            cv.put("user_id",memo.getUser_id());  // 主码自增
            cv.put("memo_done",memo.getMemo_done());
            cv.put("memo_content",memo.getMemo_content());
            db.insert(TABLE2_NAME,null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }
    /*创建备忘录*/
    public  void insert_Memo(Memo memo) {
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("memo_title",memo.getMemo_title());
            cv.put("memo_ctime",memo.getMemo_ctime());
            cv.put("memo_dtime",memo.getMemo_dtime());
            cv.put("memo_priority",memo.getMemo_priority());
            cv.put("memo_periodicity",memo.getMemo_periodicity());
            cv.put("memo_advanced",memo.getMemo_advanced());
            cv.put("memo_remind",memo.getMemo_remind());
            cv.put("memo_paper",memo.getMemo_paper());
            cv.put("user_id",memo.getUser_id());  // 主码自增
            cv.put("memo_done",memo.getMemo_done());
            cv.put("memo_content",memo.getMemo_content());
            db.insert(TABLE2_NAME,null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }

    /*获取刚刚插入的memoid, 创建完一个备忘录后直接使用getmemoid获取memoid值*/
    public int getmemoid(){
        String sql= "select max(memo_id) from "+TABLE2_NAME;
        Cursor c=null;
        int memoid=0;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToFirst()) {
                memoid=c.getInt((c.getColumnIndex("memo_id")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memoid;
    }

    /*修改备忘录*/
    public  void update_Memo(Memo m) {
        db.execSQL("update memo set memo_title=? where memo_id=?", new Object[]{ m.getMemo_title(),m.getMemo_id() });
        db.execSQL("update memo set memo_dtime=? where memo_id=?", new Object[]{ m.getMemo_dtime(),m.getMemo_id() });
        db.execSQL("update memo set memo_periodicity=? where memo_id=?", new Object[]{ m.getMemo_periodicity(),m.getMemo_id() });
        db.execSQL("update memo set memo_priority=? where memo_id=?", new Object[]{ m.getMemo_priority(),m.getMemo_id() });
        db.execSQL("update memo set memo_advanced=? where memo_id=?", new Object[]{ m.getMemo_advanced(),m.getMemo_id() });
        db.execSQL("update memo set memo_remind=? where memo_id=?", new Object[]{ m.getMemo_remind(),m.getMemo_id() });
        db.execSQL("update memo set memo_paper=? where memo_id=?", new Object[]{ m.getMemo_paper(),m.getMemo_id() });
        db.execSQL("update memo set memo_content=? where memo_id=?", new Object[]{ m.getMemo_content(),m.getMemo_id() });
    }

    /*插入图片*/
    public  void insert_Picture(Picture picture) {
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("pic_filename",picture.getPic_filename());
            cv.put("memo_id",picture.getMemo_id());
            db.insert(TABLE3_NAME,null,cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }

    /*修改trip_on状态，1开0关，下同*/
    public void changeTrip_on(int user_id,int pri){
        db.execSQL("update user set trip_on=? where user_id=?", new Object[]{ pri,user_id });
    }

    /*修改trip_priority*///优先级规定：pri 0 低 1 中 2 高
    public void changeTrip_priority(int user_id,int pri){
        db.execSQL("update user set trip_priority=? where  user_id=?", new Object[]{ pri,user_id });
    }

    /*修改trip_paper状态*/
    public void changeTrip_paper(int user_id,int pri){
        db.execSQL("update user set trip_paper=? where  user_id=?", new Object[]{ pri,user_id });
    }

    /*修改weather_on状态*/
    public void changeWeather_on(int user_id,int pri){
        db.execSQL("update user set weather_on=? where  user_id=?", new Object[]{pri,user_id });
    }

    /*修改weather_priority为关*///优先级规定：pri 0 低 1 中 2 高
    public void changeWall_priority(int user_id,int pri){
        db.execSQL("update user set weather_priority=? where  user_id=?", new Object[]{pri,user_id });
    }

    /*修改weather_paper状态*/
    public void changeWallpaper_paper(int user_id,int pri){
        db.execSQL("update user set weather_paper=? where  user_id=?", new Object[]{ pri,user_id });
    }

    /*修改parcel_on状态*/
    public void changeParcel_on(int user_id,int pri){
        db.execSQL("update user set parcel_on= ? where  user_id=?", new Object[]{ pri,user_id });
    }

    /*修改parcel_priority为关*///优先级规定：pri 0 低 1 中 2 高
    public void changeParcel_priority(int user_id,int pri){
        db.execSQL("update user set parcel_priority=?  where  user_id=?", new Object[]{ pri,user_id });
    }

    /*修改parcel_paper状态*/
    public void changeParcel_paper(int user_id,int pri){
        db.execSQL("update user set parcel_paper= ? where  user_id=?", new Object[]{pri, user_id });
    }

    /*修改analysis_on状态*/
    public void changeAnalysis_on(int user_id,int pri){
        db.execSQL("update user set analysis_on= ? where  user_id=?", new Object[]{pri, user_id });
    }

    /*修改analysis_priority为关*/
    public void changeAnalysis_priority(int user_id,int pri){
        db.execSQL("update user set trip_priority=0  where  user_id=?", new Object[]{pri, user_id });
    }

    /*修改analysis_paper状态*/
    public void changeAnalysis_paper(int user_id,int pri){
        db.execSQL("update user set analysis_paper= 0 where  user_id=?", new Object[]{pri, user_id });
    }

    //改变备忘录的完成状态
    public void changestate(int memo_id){
        db.execSQL("update memo set memo_done = 1 where memo_id =?", new Object[]{ memo_id });
    }
    //改变备忘录的完成状态为未完成状态
    public void changestate1(int memo_id){
        db.execSQL("update memo set memo_done = 0 where memo_id =?", new Object[]{ memo_id });
        System.out.println("正在修改"+memo_id+"这条备忘录的完成状态");
    }
    //删除已完成备忘录列表
    public void deletedone(int user_id){
        db.execSQL("delete from memo where memo_done==1 and user_id=?", new Object[]{ user_id });
    }

    //删除多条备忘录
    public void deletedone(List<Integer> memoidlist ){
        Iterator iter = memoidlist.iterator();
        while(iter.hasNext()){
            db.execSQL("delete from memo where memo_id=?", new Object[]{ iter.next() });
        }
    }

    /*壁纸获得memo列表（未到期）*/
    public ArrayList<Memo> returnpapermemo(int user_id){
        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id+" and memo_paper=1 order by memo_priority DESC,memo_dtime ASC";
        ArrayList<Memo> memolist = new ArrayList<Memo>();
        int i=0;
        Cursor c = null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                if(c.getInt((c.getColumnIndex("memo_done")))==1) continue;

                long x=c.getLong((c.getColumnIndex("memo_dtime")));
                Date date = new Date(x);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dtime=format.format(date);

                if(judgedtime(dtime)){
                    i++;
                    if(i>10) break;
                    Memo m = new Memo();
                    m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
                    m.setMemo_dtime(x);
                    m.setMemo_paper(1);
                    m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                    memolist.add(m);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memolist;
    }

    /*返回所有过期memo列表(memo中任务id任务标题到期时间有效)*/
    public ArrayList<Memo> memotime_out(int user_id){
        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id+" order by memo_priority DESC,memo_dtime ASC";
        ArrayList<Memo> memolist = new ArrayList<Memo>();
        Cursor c = null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                long x=c.getLong((c.getColumnIndex("memo_dtime")));
                Date date = new Date(x);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dtime=format.format(date);
                if(!judgedtime(dtime)&&c.getInt((c.getColumnIndex("memo_periodicity")))!=0){
                    Memo m = new Memo();
                    m.setMemo_dtime(x);
                    m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                    m.setMemo_periodicity(c.getInt((c.getColumnIndex("memo_periodicity"))));
                    memolist.add(m);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memolist;
    }

    //    /*返回主界面的已完成列表(memo中任务id任务标题到期时间有效)*/
//    public ArrayList<Memo> returnmemo1(int user_id){
//        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id+" and memo_done= 1 order by memo_priority DESC,memo_dtime ASC";
//        ArrayList<Memo> memolist = new ArrayList<Memo>();
//        Cursor c = null;
//        try{
//            c = db.rawQuery(sql,null);
//            while (c.moveToNext()) {
//                long x=c.getLong((c.getColumnIndex("memo_dtime")));
//                Date date = new Date(x);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String dtime=format.format(date);
//                if(!judgedtime(dtime)){
//                    Memo m = new Memo();
//                    m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
//                    m.setMemo_dtime(x);
//                    m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
//                    memolist.add(m);
//                }
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            if (c!= null) {
//                c.close();
//            }
//        }
//        return memolist;
//    }
    public ArrayList<Memocloud>  returnmemocloud(int user_id){
        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id+" order by memo_priority DESC,memo_dtime ASC";
        ArrayList<Memocloud> memolist = new  ArrayList<Memocloud>();
        Cursor c = null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
            Memocloud m = new Memocloud();
            m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
            m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
            m.setMemo_ctime(c.getLong((c.getColumnIndex("memo_ctime"))));
            m.setMemo_dtime(c.getLong((c.getColumnIndex("memo_dtime"))));
            m.setMemo_priority(c.getInt((c.getColumnIndex("memo_priority"))));
            m.setMemo_periodicity(c.getInt((c.getColumnIndex("memo_periodicity"))));
            m.setMemo_advanced(c.getInt((c.getColumnIndex("memo_advanced"))));
            m.setMemo_remind(c.getInt((c.getColumnIndex("memo_remind"))));
            m.setMemo_paper(c.getInt((c.getColumnIndex("memo_paper"))));
            m.setUser_id(c.getInt((c.getColumnIndex("user_id"))));
            m.setMemo_done(c.getInt((c.getColumnIndex("memo_done"))));
            m.setMemo_content(c.getString((c.getColumnIndex("memo_content"))));
            memolist.add(m);
            }}
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memolist;
    }

    public ArrayList<Memo> returnmemo1(int user_id){
        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id+" order by memo_priority DESC,memo_dtime ASC";
        ArrayList<Memo> memolist = new ArrayList<Memo>();
        Cursor c = null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                if(c.getInt((c.getColumnIndex("memo_done")))==0) continue;

                long x=c.getLong((c.getColumnIndex("memo_dtime")));
                Date date = new Date(x);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dtime=format.format(date);

                if(judgedtime(dtime)){
                    Memo m = new Memo();
                    m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
                    m.setMemo_dtime(x);
                    m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                    memolist.add(m);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memolist;
    }

    /*判断时间是否过期*/
    public boolean judgedtime(String dtime){
        /****获取当前时间****/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
        String str =formatter.format(curDate);
        //到期时间小于当前时间，过期为false
        if(dtime.compareTo(str)<0){
            return false;
        }else {
            return true;
        }
    }



    //返回主界面的未完成列表(未到期)
    public ArrayList<Memo> returnmemo2(int user_id){
        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id+" order by memo_priority DESC,memo_dtime ASC";
        ArrayList<Memo> memolist = new ArrayList<Memo>();
        Cursor c = null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                if(c.getInt((c.getColumnIndex("memo_done")))==1) continue;

                long x=c.getLong((c.getColumnIndex("memo_dtime")));
                Date date = new Date(x);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dtime=format.format(date);

                if(judgedtime(dtime)){
                    Memo m = new Memo();
                    m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
                    m.setMemo_dtime(x);
                    m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                    memolist.add(m);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memolist;
    }

    //返回主界面的未完成列表(过期)
    public ArrayList<Memo> returnmemo3(int user_id){
        String sql= "select * from "+TABLE2_NAME+" where user_id = "+user_id+" and memo_dtime<"+System.currentTimeMillis()+" order by memo_priority DESC,memo_dtime ASC";
        ArrayList<Memo> memolist = new ArrayList<Memo>();
        Cursor c = null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                if(c.getInt((c.getColumnIndex("memo_done")))==1) continue;
                long x=c.getLong((c.getColumnIndex("memo_dtime")));
                Date date = new Date(x);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dtime=format.format(date);
                if(!judgedtime(dtime)){
                    Memo m = new Memo();
                    m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
                    m.setMemo_dtime(x);
                    m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                    memolist.add(m);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return memolist;
    }

    /*获得memo的主界面列表按照优先级和memo_dtime升序查询*/
    public Memo[] returMemo_list(int user_id) {
        int a =0;
        String sql="select memo_title,memo_dtime from memo where user_id ="+user_id+" and order by memo_priority DESC,memo_dtime ASC";
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.getCount()>10)
            a=10;
        else
            a=c.getCount();
        Memo[] memos = new Memo[a];
        if(c.moveToFirst()) {
            for (int i = 0; i < a; i++) {
                c.move(i);
                memos[i].setMemo_title(c.getString(0));
                memos[i].setMemo_dtime(c.getLong(1));

            }
        }
        return memos;

    }



    //返回一个备忘的详细内容（除图片音频）
    public Memo returnamemo(int memo_id) {
        String sql= "select * from "+TABLE2_NAME+" where memo_id = "+memo_id;
        Cursor c=null;
        Memo m=new Memo();
        try{

            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                m.setMemo_id(c.getInt((c.getColumnIndex("memo_id"))));
                m.setMemo_title(c.getString((c.getColumnIndex("memo_title"))));
                m.setMemo_advanced(c.getInt((c.getColumnIndex("memo_advanced"))));
                m.setMemo_content(c.getString((c.getColumnIndex("memo_content"))));
                m.setMemo_ctime(c.getLong((c.getColumnIndex("memo_ctime"))));
                m.setMemo_dtime(c.getLong((c.getColumnIndex("memo_dtime"))));
                m.setMemo_done(c.getInt((c.getColumnIndex("memo_done"))));
                m.setMemo_paper(c.getInt((c.getColumnIndex("memo_paper"))));
                m.setMemo_periodicity(c.getInt((c.getColumnIndex("memo_periodicity"))));
                m.setMemo_remind(c.getInt((c.getColumnIndex("memo_remind"))));
                m.setMemo_priority(c.getInt((c.getColumnIndex("memo_priority"))));
                m.setUser_id(c.getInt((c.getColumnIndex("user_id"))));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }

        return m;
    }

    //返回一张或多张图片路径
    public List<String> returnpicture(int memo_id){
        String sql= "select pic_filename from "+TABLE3_NAME+" where memo_id = "+memo_id;
        List<String> piclist = new ArrayList<String>();
        String s=null;
        Cursor c=null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                s=c.getString((c.getColumnIndex("pic_filename")));
                piclist.add(s);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return piclist;
    }

    //返回一张或多张音频路径
    public List<String> returnaudio(int memo_id){
        String sql= "select audio_filename from "+TABLE4_NAME+" where memo_id = "+memo_id;
        List<String> audiolist = new ArrayList<String>();
        String s=null;
        Cursor c=null;
        try{
            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                s=c.getString((c.getColumnIndex("audio_filename")));
                audiolist.add(s);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }
        return audiolist;
    }

    /*任务周期的实现*/
    public void Update_periodicity(int user_id){
        ArrayList<Memo> memolist =memotime_out(user_id);
        for (int i = 0; i < memolist.size(); i++) {
            Memo m=memolist.get(i);
            long newDtime=newDtime(m.getMemo_dtime(),m.getMemo_periodicity());
            db.execSQL("update memo set memo_dtime="+newDtime
                    +" where  user_id="+user_id+" and  memo_id ="+m.getMemo_id());
        }
    }

    /*过期之后，获取更新的有效时间*/
    public long newDtime(long memo_dtime,int memo_periodicity){
        long newDtime=memo_dtime;
        if(memo_periodicity==7)
            newDtime+=86400000L*7;
        if(memo_periodicity==15)
            newDtime+=86400000L*15;
        if(memo_periodicity==30){
            Date date = new Date(memo_dtime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date1 = format.format(date);
            int month=Integer.parseInt(date1.substring(5,7)),year=Integer.parseInt(date1.substring(0,4)),day=Integer.parseInt(date1.substring(8,10));
            switch (month) {
                case 1:day = 31;break;
                case 3: day = 31;break;
                case 5: day = 31; break;
                case 7:day = 31 ;break;
                case 8: day = 31;break;
                case 10: day = 31;break;
                case 12 : day = 31; break;
                case 4:day = 30;break;
                case 6:day = 30 ;break;
                case 9:day = 30 ; break;
                case 11: day = 30; break;
                case 2: day = (year%4==0&&year%100!=0||year%400==0) ? 29 : 28;break;
                default : break;
            }
            newDtime+=86400000L*day;
        }
        return newDtime;
    }


    /*返回用户的详细信息*/
    public User returnauser(int user_id) {
        String sql= "select * from "+TABLE1_NAME+" where user_id = "+user_id;
        Cursor c=null;
        User m=new User();
        try{

            c = db.rawQuery(sql,null);
            while (c.moveToNext()) {
                m.setAnalysis_on(c.getInt(c.getColumnIndex("analysis_on")));
                m.setAnalysis_paper(c.getInt(c.getColumnIndex("analysis_paper")));
                m.setAnalysis_priority(c.getInt(c.getColumnIndex("analysis_priority")));
                m.setParcel_on(c.getInt(c.getColumnIndex("parcel_on")));
                m.setParcel_paper(c.getInt(c.getColumnIndex("parcel_paper")));
                m.setParcel_priority(c.getInt(c.getColumnIndex("parcel_priority")));
                m.setTrip_on(c.getInt(c.getColumnIndex("trip_on")));
                m.setTrip_paper(c.getInt(c.getColumnIndex("trip_paper")));
                m.setTrip_priority(c.getInt(c.getColumnIndex("trip_priority")));
                m.setWeather_on(c.getInt(c.getColumnIndex("weather_on")));
                m.setWeather_paper(c.getInt(c.getColumnIndex("weather_paper")));
                m.setWeather_priority(c.getInt(c.getColumnIndex("weather_priority")));
                m.setUser_password(c.getString(c.getColumnIndex("user_password")));
                m.setUser_tel(c.getString(c.getColumnIndex("user_tel")));

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (c!= null) {
                c.close();
            }
        }

        return m;
    }
    /*获取用户的天气通知优先级*/
    public int  getWeather_priority(int user_id){
        String sql="select weather_priority from user where user_id ="+user_id;
        int weather_priority =0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            weather_priority =c.getInt(0);
            c.close();

        }else {
            c.close();
        } return weather_priority;
    }

    public int is_exis(String mobile,String pass){
        String sql="select * from user where user_tel="+mobile;
        String pass1="2222";
        int user_id1=0;
        Cursor c = null;
        c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            user_id1 =c.getInt((c.getColumnIndex("user_id")));
            pass1=c.getString((c.getColumnIndex("user_password")));
            c.close();

        }else {
            c.close();
        }

        if(pass1.equals(pass)){
            return user_id1;
        }
        else {
            return -1;
        }

    }

    /*关闭数据库*/
    public void closeDB(){
        db.close();
    }
}
