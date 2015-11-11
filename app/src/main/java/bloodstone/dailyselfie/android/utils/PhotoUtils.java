package bloodstone.dailyselfie.android.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by minsamy on 11/5/2015.
 */
public class PhotoUtils {

    static public final int PHOTO_TYPE_NORMAL_SELFIE = 0;
    static public final int PHOTO_TYPE_EFFECTS_SELFIE = 1;
    static private String DIR_SELFIES = "Daily Selfies";
    static private String DIR_NORMAL_SELFIES = "Selfies";
    static private String DIR_EFFECTS_SELFIES = "Selfies Effects";


    static public final String ANON_USER="anon_user";

    /**
     * @param userId
     * @param selfieType
     * @return
     * @throws IOException
     */
    static private File createImageFile(Context context,String userId, int selfieType) throws IOException {
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
                imageFile.setWritable(true,false);
                //imageFile.setExecutable(true,false);
                imageFile.setReadable(true,false);

                MediaScannerConnection.scanFile(context, new String[]{imageFile.getPath()}, null, null);
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
     * @param context
     * @param requestCode
     * @param userId
     * @param selfieType
     * @return
     */
    static public Intent makeCameraCaptureIntent(Context context, int requestCode, String userId, int selfieType) throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = createImageFile(context,userId, selfieType);
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra("file",photoFile.getAbsolutePath());
                return intent;
            }
        }
        return null;
    }





    static public CursorLoader getImageFileCursorLoader(Context context,String userId,int selfieType){
        String selection = MediaStore.Images.Media.DATA + " LIKE ? AND "+MediaStore.Images.Media.SIZE+">0";
        String args=null;
        if(selfieType==PHOTO_TYPE_NORMAL_SELFIE){
            args="%"+DIR_SELFIES+File.separator+userId+File.separator+DIR_NORMAL_SELFIES+File.separator+"%";
        }else{
            args="%"+DIR_SELFIES+File.separator+userId+File.separator+DIR_EFFECTS_SELFIES+File.separator+"%";
        }
        String[] selectionArgs = new String[]{args};
        CursorLoader loader=new CursorLoader(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,
                selection,selectionArgs,null);
        return loader;
    }



    public void test(Context context) {

        ///storage/emulated/0/Pictures/Daily Selfies/user1/Selfies/selfie_1446683353142-821907906.jpg

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES
                + File.separator + DIR_SELFIES + File.separator + "user1" + File.separator + DIR_NORMAL_SELFIES);
        Uri u = Uri.fromFile(storageDir);


        //MatrixCursor matrixCursor

        String selection = MediaStore.Images.Media.DATA + " LIKE ?";
        //String selection=MediaStore.Images.Thumbnails.DATA+" LIKE ?";
        String[] selectionArgs = new String[]{"%Daily Selfies/user1/Selfies/selfie%"};
        Cursor c = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, null);
        //Cursor c=context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,null,null,null,null);

        String[] cols = c.getColumnNames();

        /*for(String column:cols){
            Log.e("Column",column);
        }*/

        //TODO check Matrix cursor
        Log.e("Count",String.valueOf(c.getCount()));
        while (c.moveToNext()) {
            int x = c.getInt(c.getColumnIndex(MediaStore.Images.Media._ID));
            String s = c.getString(c.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            String data = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.e("Data", s + "**" + data);
        }

    }


    public void test2(Context context) {

        ///storage/emulated/0/Pictures/Daily Selfies/user1/Selfies/selfie_1446683353142-821907906.jpg

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES
                + File.separator + DIR_SELFIES + File.separator + "user1" + File.separator + DIR_NORMAL_SELFIES);
        Uri u = Uri.fromFile(storageDir);


        //MatrixCursor matrixCursor

        String selection = MediaStore.Images.Thumbnails.IMAGE_ID + "=?";
        //String selection=MediaStore.Images.Thumbnails.DATA+" LIKE ?";
        String[] selectionArgs = new String[]{"108776"};
        Cursor c = context.getContentResolver().query(MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI, null, null, null, null);
        //Cursor c=context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,null,null,null,null);

        String[] cols = c.getColumnNames();

        /*for(String column:cols){
            Log.e("Column",column);
        }*/

        //TODO check Matrix cursor
        while (c.moveToNext()) {
            int x = c.getInt(c.getColumnIndex(MediaStore.Images.Thumbnails._ID));
            int s = c.getInt(c.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
            String data = c.getString(c.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            Log.e("Data", s + "**" + data);
        }

    }

}
