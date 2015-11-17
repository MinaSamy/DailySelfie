package bloodstone.dailyselfie.android.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;


import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import bloodstone.dailyselfie.android.MainActivity;
import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.SelfieDetailsActivity;
import bloodstone.dailyselfie.android.helper.EffectsHelper;
import bloodstone.dailyselfie.android.utils.PhotoUtils;

/**
 * Created by minsamy on 11/17/2015.
 */
public class SelfieEffectsService extends IntentService {

    static private final String EXTRA_IMAGE_ID = "image_id";
    static private final String EXTRA_USER_ID = "user_id";
    static private final String EXTRA_EFFECT_TYPE = "effect_type";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public SelfieEffectsService() {
        super("SelfieEffectsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long imageId = intent.getLongExtra(EXTRA_IMAGE_ID, -1);
        String userId = intent.getStringExtra(EXTRA_USER_ID);
        int effectType = intent.getIntExtra(EXTRA_EFFECT_TYPE, -1);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        File file=null;
        if (userId != null) {
            try {
                Notification note=getUploadNotification(imageId,effectType,userId);

                notificationManager.notify((int)imageId,note);

                file = EffectsHelper.applyEffect(this, imageId, userId, effectType);
                Log.v("Done", file.getAbsolutePath());
            } catch (IOException e) {
                Log.e("ERROR", e.toString());
                notificationManager.cancel((int) imageId);
            } catch (Exception e) {
                Log.e("ERROR", e.toString());
                notificationManager.cancel((int)imageId);
            }
            finally{
                Notification n=getEffectNotification(file,effectType,userId);
                //notificationManager.cancel((int)imageId);
                notificationManager.notify((int)imageId,n);
            }
        }
    }

    public static Intent makeServiceIntent(Context context, long imageId, String userId, int effectType) {
        Intent intent = new Intent(context, SelfieEffectsService.class);
        intent.putExtra(EXTRA_IMAGE_ID, imageId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_EFFECT_TYPE, effectType);
        return intent;
    }


    private Notification getUploadNotification(long imageId,int effectType, String userId) {

        String text = this.getString(R.string.applying);
        switch (effectType) {
            case PhotoUtils.EFFECT_BLACK_WHITE:
            default:
                text+=this.getString(R.string.black_white);
                break;
            case PhotoUtils.EFFECT_COMIC:
                text+=this.getString(R.string.comic_effect);
                break;
            case PhotoUtils.EFFECT_TINT:
                text+=this.getString(R.string.tint_effect);
                break;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text);

        Intent resultIntent= SelfieDetailsActivity.makeIntent(this, imageId, effectType, userId);

        //construct the task stack to maintain the back button functionality
        //construct a new stack with the main activity at the bottom of the stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SelfieDetailsActivity.class);

        stackBuilder.addNextIntent(MainActivity.makeMainActivityIntent(this, userId));
        //stackBuilder.addParentStack(SelfieDetailsActivity.class);
        //stackBuilder.addNextIntent(resultIntent);



        //add the image details to the top of the stack

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        builder.setProgress(0, 0, true);

        Notification note=builder.build();
        note.flags=Notification.FLAG_ONGOING_EVENT;
        return note;
    }

    private Notification getEffectNotification(File file,int effectType, String userId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.applying_done))
                .setProgress(0, 0, false);


        long imageId=PhotoUtils.getMediaId(this,file.getPath());



        Intent resultIntent= SelfieDetailsActivity.makeIntent(this, imageId, effectType, userId);

        //construct the task stack to maintain the back button functionality
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SelfieDetailsActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);

        Notification note=builder.build();
        return note;
    }
}
