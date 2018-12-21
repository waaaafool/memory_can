package can.aboutsqlite;

import com.google.gson.Gson;

public class Memocloud {
    private int memo_id;
    private String memo_title;
    private long memo_ctime;
    private long memo_dtime;
    private int memo_priority;
    private int memo_periodicity;
    private int memo_advanced;
    private int memo_remind;
    private int memo_paper;
    private int user_id;
    private int memo_done;
    private String memo_content;


    public int getMemo_id() {
        return memo_id;
    }

    public void setMemo_id(int memo_id) {
        this.memo_id = memo_id;
    }

    public String getMemo_title() {
        return memo_title;
    }

    public void setMemo_title(String memo_title) {
        this.memo_title = memo_title;
    }

    public long getMemo_ctime() {
        return memo_ctime;
    }

    public void setMemo_ctime(long memo_ctime) {
        this.memo_ctime = memo_ctime;
    }

    public long getMemo_dtime() {
        return memo_dtime;
    }

    public void setMemo_dtime(long memo_dtime) {
        this.memo_dtime = memo_dtime;
    }

    public int getMemo_priority() {
        return memo_priority;
    }

    public void setMemo_priority(int memo_priority) {
        this.memo_priority = memo_priority;
    }

    public int getMemo_periodicity() {
        return memo_periodicity;
    }

    public void setMemo_periodicity(int memo_periodicity) {
        this.memo_periodicity = memo_periodicity;
    }

    public int getMemo_advanced() {
        return memo_advanced;
    }

    public void setMemo_advanced(int memo_advanced) {
        this.memo_advanced = memo_advanced;
    }

    public int getMemo_remind() {
        return memo_remind;
    }

    public void setMemo_remind(int memo_remind) {
        this.memo_remind = memo_remind;
    }

    public int getMemo_paper() {
        return memo_paper;
    }

    public void setMemo_paper(int memo_paper) {
        this.memo_paper = memo_paper;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMemo_done() {
        return memo_done;
    }

    public void setMemo_done(int memo_done) {
        this.memo_done = memo_done;
    }

    public String getMemo_content() {
        return memo_content;
    }

    public void setMemo_content(String memo_content) {
        this.memo_content = memo_content;
    }

    public String toString() {
        Gson gson = new Gson();
        String s = gson.toJson(this);
        return s;
    }



}
