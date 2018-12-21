package can.aboutsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dale on 2018/11/03.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "404note.db";
    private static final String TABLE1_NAME = "user";
    private static final String TABLE2_NAME = "memo";
    private static final String TABLE3_NAME = "picture";
    private static final String TABLE4_NAME = "audio";
    long now_time= System.currentTimeMillis();
    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*创建user表*/
        db.execSQL("create table " + "if not exists " + TABLE1_NAME + " ("
                + "user_id integer primary key AUTOINCREMENT,"
                + "user_password varchar(16) not null,"
                + "user_tel varchar(11) not null ,"
                + "trip_on integer(1) not null default 1,"
                + "trip_priority integer(1) not null default 1,"
                + "trip_paper integer(1) not null default 1,"
                + "parcel_on integer(1) not null default 1,"
                + "parcel_priority integer(1) not null default 1,"
                + "parcel_paper integer(1) not null default 1,"
                + "analysis_on integer(1) not null default 1,"
                + "analysis_priority integer(1) not null default 1,"
                + "analysis_paper integer(1) not null default 1,"
                + "weather_on integer(1) not null default 1,"
                + "weather_priority integer(1) not null default 1,"
                + "weather_paper integer(1) not null default 1,"
                + "widget integer(1) not null default 1)");
        /*创建memo表*/
        db.execSQL("create table " + "if not exists " + TABLE2_NAME + " ("
                + "memo_id integer primary key AUTOINCREMENT,"
                + "memo_title varchar(32) not null,"
                + "memo_ctime integer not null,"
                + "memo_dtime integer not null,"
                + "memo_priority integer not null default 1,"
                + "memo_periodicity integer not null default 0,"/*设置周期提醒时间默认为无周期*/
                + "memo_advanced integer(1) not null default 0,"/*设置提前提醒时间默认为无提前*/
                + "memo_remind integer(1) not null default 0,"
                + "memo_paper integer(1) not null default 1,"/*设置是否显示在壁纸默认是*/
                + "user_id integer(8) not null ,"
                + "memo_done integer(1) not null default 0,"/*默认任务未完成*/
                + "memo_content text,"
                + " FOREIGN KEY(user_id) REFERENCES user(user_id))");
        /*创建picture表*/
        db.execSQL("create table " + "if not exists " + TABLE3_NAME + " ("
                + "pic_id integer primary key AUTOINCREMENT,"
                + "pic_filename varchar(32) not null,"
                + "memo_id integer(8) not null,"
                + " FOREIGN KEY(memo_id) REFERENCES memo(memo_id))");
        /*创建audio表*/
        db.execSQL("create table " + "if not exists " + TABLE4_NAME + " ("
                + "audio_id integer primary key AUTOINCREMENT,"
                + "audio_filename varchar(32) not null,"
                + "memo_id integer(8) not null,"
                + " FOREIGN KEY(memo_id) REFERENCES memo(memo_id))");
        /*创建wallpaper表*/
        /*db.execSQL("create table " + "if not exists " + TABLE5_NAME + " ("
                + "paper_id integer primary key AUTOINCREMENT,"
                + "paper_filename varchar(32) not null)");*/
        db.execSQL("insert into "+TABLE1_NAME+"(" +
                "user_password,user_tel) " +
                "values ('404note','13215983396')");
//        db.execSQL("insert into "+TABLE2_NAME+"(" +
//                "memo_title,user_id,memo_dtime,memo_ctime) "
//                + "values ('Im a cat!',1,20181101,20181100),"
//                + "('Im a puppy!',1,20181122,20181111),"
//                + "('Im a cattle!',1,20181128,20181115),"
//                + "('Im a bird!',1,20181130,20181120),"
//                + "('Im a hourse!',1,20181230,20181200)");
    }

    @Override
    /*数据库版本控制——暂不考虑*/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        this.onCreate(db);
    }
}
