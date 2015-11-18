package bloodstone.dailyselfie.android.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by minsamy on 11/13/2015.
 */
public class ImageLoader {
    private ExecutorService mExecutorService;
    private SimpleDateFormat mSdf;
    private Calendar mCalendar;

    public ImageLoader(){
        mExecutorService= Executors.newFixedThreadPool(3);
        mSdf=new SimpleDateFormat("EEE, MMM d, yyyy h:mm a");
        mCalendar=Calendar.getInstance();
    }

    public void displayImage(final Cursor cursor, final ImageView imageView,final TextView txt){
        final WeakReference<ImageView>imageViewReference=new WeakReference<ImageView>(imageView);
        final WeakReference<TextView>textViewReference=new WeakReference<TextView>(txt);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                long captureDate=cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                mCalendar.setTimeInMillis(captureDate);
                String title=mSdf.format(mCalendar.getTime());
                if(textViewReference.get()!=null){
                    textViewReference.get().setText(title);
                }
                //txt.setText(title);
                int imageId=cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize =8;



                final Bitmap map=MediaStore.Images.Thumbnails.getThumbnail(imageView.getContext().getContentResolver(),
                        imageId, MediaStore.Images.Thumbnails.MINI_KIND, bmOptions);
                if(map!=null){
                    final ImageView img=imageViewReference.get();
                    if(img!=null){
                        img.post(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageBitmap(map);
                            }
                        });
                    }

                }
            }
        });
    }
}
