package can.aboutsqlite;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.text.ParseException;


public class Memo {


    private int memo_id;
    private String memo_title;
    private String memo_ctime;
    private String memo_dtime;
    private int memo_priority;
    private int memo_periodicity;
    private int memo_advanced;
    private int memo_remind;
    private int memo_paper;
    private int user_id;
    private int memo_done;
    private String memo_content;

    public Memo() {
    }
    /*全部参数的memo构造*/
    public Memo(String memo_title, String memo_dtime,int memo_priority,int memo_periodicity,
                int memo_advanced,int memo_remind,int memo_paper,int user_id,int memo_done,String memo_content) {
        //this.memo_id = memo_id;
        this.memo_title= memo_title;
        //this.memo_ctime = memo_ctime;
        this.memo_dtime = memo_dtime;
        this.memo_priority = memo_priority;
        this.memo_periodicity = memo_periodicity;
        this.memo_advanced = memo_advanced;
        this.memo_remind = memo_remind;
        this.memo_paper = memo_paper;
        this.user_id = user_id;
        this.memo_done = memo_done;
        this.memo_content = memo_content;
    }
    /*memo_id*/
    public int getMemo_id() {
        return memo_id;
    }

    public void setMemo_id(int memo_id) {
        this.memo_id = memo_id;
    }
    /*memo_title*/
    public String getMemo_title() {
        return memo_title;
    }
    public void setMemo_title(String memo_title) {
        this.memo_title = memo_title;
    }

    /*memo_ctime*/
    public long getMemo_ctime() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
        String str =formatter.format(curDate);
        Date date = null;
        try{
            date = formatter.parse(str);// String类型转成date类型}
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime();// date类型转成long类型
            return currentTime;
        }
    }
    public void setMemo_ctime(long memo_ctime) {
        Date date = new Date(memo_ctime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println("对应日期时间字符串：" + format.format(date));
        this.memo_ctime=format.format(date);
    }


    /*memo_dtime*/
    public long getMemo_dtime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try{
            date = formatter.parse(memo_dtime);// String类型转成date类型}
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime();// date类型转成long类型
            return currentTime;
        }
        /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));// 中国北京时间，东八区
        Date dateTime = null;
        try {
            dateTime = (Date) format.parse(memo_dtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;*/
    }

    public String getmemo_ctimestring(){
        return memo_ctime;
    }

    public String getmemo_dtimestring(){
        if(memo_dtime == "9999-12-01 12:12:12")
        {
            return "";
        }
        else
            return memo_dtime;
    }
    public String getmemo_dtimestring_memo_add(){

        if( memo_dtime!=null){
            return memo_dtime;
        }
        else  return "9999-12-01 12:12:12";
    }

    public void setMemo_dtimestring(String memo_dtime){
        this.memo_dtime=memo_dtime;
    }

    public void setMemo_dtime(long memo_dtime) {
        Date date = new Date(memo_dtime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if("9999-12-01 12:12:12".equals(format.format(date)))
            this.memo_dtime="";
        else
            this.memo_dtime=format.format(date);
    }



    /*memo_priority*/
    public int getMemo_priority() {
        return memo_priority;
    }
    public void setMemo_priority(int memo_priority) {
        this.memo_priority = memo_priority;
    }
    /*memo_periodicity*/
    public int getMemo_periodicity() {
        return memo_periodicity;
    }
    public void setMemo_periodicity(int memo_periodicity) {
        this.memo_periodicity = memo_periodicity;
    }
    /*memo_advanced*/
    public int getMemo_advanced() {
        return memo_advanced;
    }
    public void setMemo_advanced(int memo_advanced) {
        this.memo_advanced = memo_advanced;
    }
    /*memo_remind*/
    public int getMemo_remind() {
        return memo_remind;
    }
    public void setMemo_remind(int memo_remind) {
        this.memo_remind = memo_remind;
    }
    /*memo_paper*/
    public int getMemo_paper() {
        return memo_paper;
    }
    public void setMemo_paper(int memo_paper) {
        this.memo_paper = memo_paper;
    }
    /*user_id*/
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    /*memo_done*/
    public int getMemo_done() {
        return memo_done;
    }
    public void setMemo_done(int memo_done) {
        this.memo_done = memo_done;
    }
    /*memo_content*/
    public String getMemo_content() {
        return memo_content;
    }
    public void setMemo_content(String memo_content) {
        this.memo_content = memo_content;
    }


}
