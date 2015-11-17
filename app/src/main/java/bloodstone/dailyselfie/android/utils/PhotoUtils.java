package bloodstone.dailyselfie.android.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by minsamy on 11/5/2015.
 */
public class PhotoUtils {

    static private final String TAG=PhotoUtils.class.getName();

    static public final int PHOTO_TYPE_NORMAL_SELFIE = 0;
    static public final int PHOTO_TYPE_EFFECTS_SELFIE = 1;
    static private final String DIR_SELFIES = "Daily Selfies";
    static private final String DIR_NORMAL_SELFIES = "Selfies";
    static private final String DIR_EFFECTS_SELFIES = "Selfies Effects";

    //effects
    static public final int EFFECT_BLACK_WHITE=3;
    static public final int EFFECT_COMIC=2;
    static public final int EFFECT_TINT=1;


    static public final String ANON_USER = "anon_user";

    /**
     * @param userId
     * @param selfieType
     * @return
     * @throws IOException
     */
    static public File createImageFile(Context context, String userId, int selfieType) throws IOException {
        File imageFile = null;
        //check the external storage state
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            String fileName = "selfie" + "_" + Calendar.getInstance().getTimeInMillis();
            File storageDir;
            if (selfieType == PHOTO_TYPE_NORMAL_SELFIE) {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES
                        + File.separator + DIR_SELFIES + File.separator + userId + File.separator + DIR_NORMAL_SELFIES);
            } else {
                storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES
                        + File.separator + DIR_SELFIES + File.separator + userId + File.separator + DIR_EFFECTS_SELFIES);
            }

            boolean directoryExists = true;
            if (!storageDir.exists()) {
                directoryExists = storageDir.mkdirs();
            }

            if (directoryExists) {
                imageFile = File.createTempFile(fileName, ".jpg", storageDir);
                imageFile.setWritable(true, false);
                //imageFile.setExecutable(true,false);
                imageFile.setReadable(true, false);

                //MediaScannerConnection.scanFile(context, new String[]{imageFile.getPath()}, null, null);
                Uri contentUri = Uri.fromFile(imageFile);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
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
            File photoFile = createImageFile(context, userId, selfieType);
            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra("file", photoFile.getAbsolutePath());
                return intent;
            }
        }
        return null;
    }

    static public long getMediaId(Context context, String filePath){
        long id=-1;
        String []projection=new String[]{MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DATA+"=?";
        String [] selectionArgs=new String[]{filePath};



        Cursor cursor=context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ,projection,selection,selectionArgs,null);
        if(cursor.moveToFirst()){
            id=cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        }
        cursor.close();
        return id;
    }


    static public CursorLoader getImageFileCursorLoader(Context context, String userId, int selfieType) {
        String selection = MediaStore.Images.Media.DATA + " LIKE ? AND " + MediaStore.Images.Media.SIZE + ">0";
        String args = null;
        if (selfieType == PHOTO_TYPE_NORMAL_SELFIE) {
            args = "%" + DIR_SELFIES + File.separator + userId + File.separator + DIR_NORMAL_SELFIES + File.separator + "%";
        } else {
            args = "%" + DIR_SELFIES + File.separator + userId + File.separator + DIR_EFFECTS_SELFIES + File.separator + "%";
        }
        String[] selectionArgs = new String[]{args};
        CursorLoader loader = new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                selection, selectionArgs, null);
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
        Log.e("Count", String.valueOf(c.getCount()));
        while (c.moveToNext()) {
            int x = c.getInt(c.getColumnIndex(MediaStore.Images.Media._ID));
            String s = c.getString(c.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            String data = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.e("Data", s + "**" + data);
        }

    }

    static public Bitmap getSelfieDetails(Context context, long imageId, int targetWidth,int targetHeight){
        Bitmap bmp=null;
        InputStream stream=null;
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Uri imageUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);
        try {
            stream=context.getContentResolver().openInputStream(imageUri);
            BitmapFactory.decodeStream(stream,null,options);
            options.inSampleSize=calculateInSampleSize(options,targetWidth,targetHeight);

            options.inJustDecodeBounds=false;
            stream=context.getContentResolver().openInputStream(imageUri);
            bmp= BitmapFactory.decodeStream(stream, null, options);



        } catch (FileNotFoundException e) {
            LogUtil.logError(TAG,e.toString());
        }
        finally {
            if(stream!=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    LogUtil.logError(TAG, e.toString());
                }
            }
            return bmp;
        }
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
