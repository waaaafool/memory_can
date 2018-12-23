package can.aboutsqlite;

public class Picture {
    private int pic_id;
    private  String pic_filename;
    private int memo_id;

    public int getPic_id() {
        return pic_id;
    }

    public void setPic_id(int pic_id) {
        this.pic_id = pic_id;
    }

    public String getPic_filename() {
        return pic_filename;
    }

    public void setPic_filename(String pic_filename) {
        this.pic_filename = pic_filename;
    }

    public int getMemo_id() {
        return memo_id;
    }

    public void setMemo_id(int memo_id) {
        this.memo_id = memo_id;
    }


}
