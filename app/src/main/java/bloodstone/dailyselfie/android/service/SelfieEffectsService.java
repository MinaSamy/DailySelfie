package bloodstone.dailyselfie.android.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import bloodstone.dailyselfie.android.helper.EffectsHelper;

/**
 * Created by minsamy on 11/17/2015.
 */
public class SelfieEffectsService extends IntentService {

    static private final String EXTRA_IMAGE_ID = "image_id";
    static private final String EXTRA_USER_ID="user_id";
    static private final String EXTRA_EFFECT_TYPE="effect_type";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SelfieEffectsService() {
        super("SelfieEffectsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long imageId=intent.getLongExtra(EXTRA_IMAGE_ID,-1);
        String userId=intent.getStringExtra(EXTRA_USER_ID);
        int effectType=intent.getIntExtra(EXTRA_EFFECT_TYPE,-1);
        if(userId!=null){
            try {
                File f=EffectsHelper.applyEffect(this,imageId,userId,effectType);
                Log.v("Done",f.getAbsolutePath());
            } catch (IOException e) {
                Log.e("ERROR",e.toString());
            }
            catch (Exception e) {
                Log.e("ERROR",e.toString());
            }
        }
    }

    public static Intent makeServiceIntent(Context context,long imageId,String userId,int effectType){
        Intent intent=new Intent(context,SelfieEffectsService.class);
        intent.putExtra(EXTRA_IMAGE_ID,imageId);
        intent.putExtra(EXTRA_USER_ID,userId);
        intent.putExtra(EXTRA_EFFECT_TYPE,effectType);
        return intent;
    }
}
