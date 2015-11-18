package bloodstone.dailyselfie.android.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import bloodstone.dailyselfie.android.MainActivity;
import bloodstone.dailyselfie.android.R;

/**
 * Created by minsamy on 11/18/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    static private final String EXTRA_USER_ID = "user_id";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("ALARM", "Received");
        String userId=intent.getStringExtra(EXTRA_USER_ID);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(context.getString(R.string.reminder_note));

        Intent mainActivityIntent= MainActivity.makeMainActivityIntent(context,userId);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,mainActivityIntent,0);

        builder.setContentIntent(pendingIntent);
        Notification note= builder.build();
        //show a notification

        NotificationManager mngr=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mngr.notify(1,note);
    }

    static public Intent makeIntent(Context context, String userId) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }
}
