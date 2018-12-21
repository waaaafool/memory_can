package can.main_delete;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Group_new {
    private String group_name;
    private int memo_done;

    public Group_new() {
    }

    public Group_new(String group_name, int memo_done) {
        this.memo_done = memo_done;
        this.group_name=group_name;
    }

    public int get_memo_done() {
        return memo_done;
    }

    public String get_Group_name()
    {
        return group_name;
    }

    public void set_Group(String group_name, int memo_done) {
        this.memo_done = memo_done;
        this.group_name = group_name;
    }
}