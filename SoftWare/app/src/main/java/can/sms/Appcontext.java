package can.sms;

import android.app.Application;
import android.content.Context;

public class Appcontext extends Application {

    private static Context context;
    public static String last_to_notice = "-------";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    /**
     * 获取全局上下文*/
    public static Context getContext() {
        return context;
    }
}

