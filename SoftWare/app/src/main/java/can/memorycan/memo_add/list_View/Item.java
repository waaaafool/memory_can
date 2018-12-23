package can.memorycan.memo_add.list_View;

import android.widget.ArrayAdapter;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Item {

    private int iId;
    private ArrayAdapter<String> _Adapter;
    private String iName;

    public Item() {
    }

    public Item(int iId,String iName,ArrayAdapter<String> _Adapter) {
        this.iId = iId;
        this._Adapter=_Adapter;
        this.iName = iName;
    }

    public int getiId() {
        return iId;
    }

    public String getiName() {
        return iName;
    }

    public ArrayAdapter<String> geti_Adapter(){return _Adapter;}

    public void setiId(int iId) {
        this.iId = iId;
    }

    public void seti_Adapter(ArrayAdapter<String> _Adapter){this._Adapter=_Adapter;}

    public void setiName(String iName) {
        this.iName = iName;
    }
}
