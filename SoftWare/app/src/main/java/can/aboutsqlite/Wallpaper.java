package can.aboutsqlite;

public class Wallpaper {


    private int paper_id;
    private  String paper_filename;
    private int paper_priority;

    public int getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(int paper_id) {
        this.paper_id = paper_id;
    }
    public String getPaper_filename() {
        return paper_filename;
    }

    public void setPaper_filename(String paper_filename) {
        this.paper_filename = paper_filename;
    }
    public int getPaper_priority() {
        return paper_priority;
    }

    public void setPaper_priority(int paper_priority) {
        this.paper_priority = paper_priority;
    }
}
