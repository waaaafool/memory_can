package can.live_assitcance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver0 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("-------------");
        System.out.println("Receiver运行了！");
        System.out.println("-------------");
        Intent i = new Intent(context, AppUsedService.class);
        context.startService(i);
    }

}