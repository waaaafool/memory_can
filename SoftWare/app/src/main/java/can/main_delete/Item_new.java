package can.main_delete;

import java.util.Date;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Item_new {

    private int memo_id;
    private String memo_title;
    //主题的长度32固定是否需要在讨论
    private String memo_dtime;
    private boolean checkStatus;

    public Item_new() {
    }

    public Item_new(int memo_id, String memo_title, String memo_dtime) {
        this.memo_id = memo_id;
        this.memo_title = memo_title;
        this.memo_dtime = memo_dtime;
        this.checkStatus = false;
    }

    public int get_memo_id() {
        return memo_id;
    }

    public String get_memo_title() {
        return memo_title;
    }

    public String get_memo_dtime(){ return memo_dtime; }

    public boolean getCheckStatus() {
        return checkStatus;
    }

    public void set_memo_id(int memo_id){ this.memo_id = memo_id; }

    public void set_memo_title(String memo_title) {
        this.memo_title = memo_title;
    }

    public void set_memo_dtime(String memo_dtime) { this.memo_dtime = memo_dtime; }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }
}
