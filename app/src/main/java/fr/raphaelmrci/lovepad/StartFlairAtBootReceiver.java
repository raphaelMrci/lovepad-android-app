package fr.raphaelmrci.lovepad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartFlairAtBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, SMSFlair.class);
            context.startService(serviceIntent);
        }
    }
}
