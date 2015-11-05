package bloodstone.dailyselfie.android.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by minsamy on 11/5/2015.
 */
public class CameraUtils {

    static private String DIR_SELFIES = "Daily Selfies";
    static private String DIR_NORMAL_SELFIES = "Selfies";
    static private String DIR_EFFECTS_SELFIES = "Selfies Effects";


    static public final int PHOTO_TYPE_NORMAL_SELFIE = 0;
    static public final int PHOTO_TYPE_EFFECTS_SELFIE = 1;

    /**
     *
     * @param userId
     * @param selfieType
     * @return
     * @throws IOException
     */
    static private File createImageFile(String userId, int selfieType) throws IOException {
        File imageFile = null;
        //check the external storage state
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            String fileName = "selfie" + "_" + Calendar.getInstance().getTimeInMillis();
            File storageDir;
            if (selfieType == PHOTO_TYPE_NORMAL_SELFIE) {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES
                        + File.separator+DIR_SELFIES+File.separator + userId+File.separator+DIR_NORMAL_SELFIES);
            } else {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES
                        + File.separator+DIR_SELFIES+File.separator + userId+File.separator+DIR_EFFECTS_SELFIES);
            }

            boolean directoryExists = true;
            if (!storageDir.exists()) {
                directoryExists = storageDir.mkdirs();
            }

            if (directoryExists) {
                imageFile=File.createTempFile(fileName,".jpg",storageDir);
            } else {
                throw new IOException("Directory creation failed");
            }
        } else {
            //storage not mounted
            throw new IOException("External Storage not mounted");
        }
        return imageFile;

    }

    /**
     *
     * @param context
     * @param requestCode
     * @param userId
     * @param selfieType
     * @return
     */
    static public Intent makeCameraCaptureIntent(Context context,int requestCode,String userId,int selfieType) throws IOException {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(context.getPackageManager())!=null){
            File photoFile=createImageFile(userId,selfieType);
            if(photoFile!=null){
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                return intent;
            }
        }
        return null;
    }
}
