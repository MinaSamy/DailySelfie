package bloodstone.dailyselfie.android.helper;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import bloodstone.dailyselfie.android.utils.NetUtils;
import bloodstone.dailyselfie.android.utils.PhotoUtils;

/**
 * Created by minsamy on 11/17/2015.
 */
public class EffectsHelper {
    private static final String URL_APPLY_EFFECT=AuthenticationHelper.BASE_URL+"applyeffect";

    private static final String PARAM_USER_ID="userid";
    private static final String PARAM_EFFECT_TYPE="type";

    static public File applyEffect(Context context, long imageId,String userId,int effectType) throws IOException {
        HashMap<String,String> params=new HashMap<>();
        params.put(PARAM_USER_ID,userId);
        params.put(PARAM_EFFECT_TYPE,String.valueOf(effectType));

        Uri imageUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);
        InputStream fileStream= context.getContentResolver().openInputStream(imageUri);


        InputStream stream= NetUtils.postMultiPart(URL_APPLY_EFFECT, params, fileStream);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int next =-1;
        byte[] buffer = new byte[1024];
        while ((next=stream.read()) != -1) {
            bos.write(next);
        }
        //bos.flush();
        //bos.close();
        byte[] result = bos.toByteArray();


        Bitmap bmp=BitmapFactory.decodeByteArray(result,0,result.length);
        MediaStore.Images.Media.insertImage(context.getContentResolver(),bmp,"TEST.jpg","desc");

        File effectsFile= PhotoUtils.createImageFile(context, userId, PhotoUtils.PHOTO_TYPE_EFFECTS_SELFIE);
        FileOutputStream fos=new FileOutputStream(effectsFile);
        fos.write(result);
        //fos.flush();
        fos.close();
        return effectsFile;

    }
}
