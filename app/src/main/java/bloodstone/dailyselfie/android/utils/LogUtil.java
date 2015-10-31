package bloodstone.dailyselfie.android.utils;

import android.util.Log;

import bloodstone.dailyselfie.android.BuildConfig;

/**
 * Created by minsamy on 10/31/2015.
 */
public class LogUtil {
    static public void logError(String tag,String message){
        if(BuildConfig.DEBUG){
            Log.e(tag,message);
        }
    }
}
