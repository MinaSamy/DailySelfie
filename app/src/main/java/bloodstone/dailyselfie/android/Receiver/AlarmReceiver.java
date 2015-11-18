package bloodstone.dailyselfie.android.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by minsamy on 11/18/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("ALARM","Received");
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
    }
}
