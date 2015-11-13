package bloodstone.dailyselfie.android.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by minsamy on 11/13/2015.
 */
public class ImageLoader {
    private ExecutorService mExecutorService;

    public ImageLoader(){
        mExecutorService= Executors.newFixedThreadPool(5);
    }

    public void displayImage(final Cursor cursor, final ImageView imageView,final TextView txt){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                String title=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                txt.setText(title);
                int imageId=cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize =4;

                final Bitmap map=MediaStore.Images.Thumbnails.getThumbnail(imageView.getContext().getContentResolver(),
                        imageId, MediaStore.Images.Thumbnails.MINI_KIND, bmOptions);
                if(map!=null){
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(map);
                        }
                    });
                }
            }
        });
    }
}