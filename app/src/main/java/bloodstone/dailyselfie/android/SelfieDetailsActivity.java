package bloodstone.dailyselfie.android;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SelfieDetailsActivity extends AppCompatActivity {

    static private final String EXTRA_IMAGE_ID="image_id";
    static private final String EXTRA_SELFIE_TYPE="selfie_type";

    private ImageView mSelfieImage;
    private int mImageId;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_details);
        mSelfieImage=(ImageView)findViewById(R.id.selfie_image);


        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(mBitmap!=null){
                    mSelfieImage.setImageBitmap(mBitmap);
                }
            }
        };
        final Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                if(getIntent().hasExtra(EXTRA_IMAGE_ID)){
                    mImageId=getIntent().getIntExtra(EXTRA_IMAGE_ID,-1);
                    if(mImageId!=-1){
                        Uri imageUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,mImageId);
                        try {
                            InputStream stream=getContentResolver().openInputStream(imageUri);
                            mBitmap= BitmapFactory.decodeStream(stream);
                            handler.sendEmptyMessage(0);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        mSelfieImage.post(new Runnable() {
            @Override
            public void run() {
                t.start();
                Log.e("Width", String.valueOf(mSelfieImage.getMeasuredWidth()));
                Log.e("Height", String.valueOf(mSelfieImage.getMeasuredHeight()));
            }
        });




        /*if(getIntent().hasExtra(EXTRA_IMAGE_ID)){
            mImageId=getIntent().getIntExtra(EXTRA_IMAGE_ID,-1);
            if(mImageId!=-1){
                Uri imageUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,mImageId);
                try {
                    InputStream stream=getContentResolver().openInputStream(imageUri);
                    Bitmap bmp= BitmapFactory.decodeStream(stream);
                    mSelfieImage.setImageBitmap(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }*/

    }

    @Override
    protected void onDestroy() {
        mSelfieImage.setImageBitmap(null);
        super.onDestroy();
    }

    static public Intent makeIntent(Context context,int imageId, int selfieType){
        Intent intent=new Intent(context,SelfieDetailsActivity.class);
        intent.putExtra(EXTRA_IMAGE_ID,imageId);
        intent.putExtra(EXTRA_SELFIE_TYPE,selfieType);
        return intent;
    }
}
