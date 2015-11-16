package bloodstone.dailyselfie.android;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


import java.lang.ref.WeakReference;

import bloodstone.dailyselfie.android.utils.PhotoUtils;

public class SelfieDetailsActivity extends AppCompatActivity {

    static private final String EXTRA_IMAGE_ID = "image_id";
    static private final String EXTRA_SELFIE_TYPE = "selfie_type";
    static private final String EXTRA_USER_ID="user_id";

    private ImageView mSelfieImage;
    private int mImageId;
    private WeakReference<Bitmap> mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_details);
        mSelfieImage = (ImageView) findViewById(R.id.selfie_image);


        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mBitmap != null &&mBitmap.get()!=null) {
                    mSelfieImage.setImageBitmap(mBitmap.get());
                }
            }
        };
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (getIntent().hasExtra(EXTRA_IMAGE_ID)) {
                    mImageId = getIntent().getIntExtra(EXTRA_IMAGE_ID, -1);
                    if (mImageId != -1) {
                        Bitmap bmp = PhotoUtils.getSelfieDetails(SelfieDetailsActivity.this, mImageId, mSelfieImage.getMeasuredWidth(),
                                mSelfieImage.getMeasuredHeight());
                        if(bmp!=null){
                            mBitmap=new WeakReference<Bitmap>(bmp);
                        }

                        handler.sendEmptyMessage(0);
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

    static public Intent makeIntent(Context context, int imageId, int selfieType,String userId) {
        Intent intent = new Intent(context, SelfieDetailsActivity.class);
        intent.putExtra(EXTRA_IMAGE_ID, imageId);
        intent.putExtra(EXTRA_SELFIE_TYPE, selfieType);
        intent.putExtra(EXTRA_USER_ID,userId);
        return intent;
    }
}
