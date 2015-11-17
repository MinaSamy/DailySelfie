package bloodstone.dailyselfie.android;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import java.lang.ref.WeakReference;

import bloodstone.dailyselfie.android.service.SelfieEffectsService;
import bloodstone.dailyselfie.android.utils.PhotoUtils;

public class SelfieDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    static private final String EXTRA_IMAGE_ID = "image_id";
    static private final String EXTRA_SELFIE_TYPE = "selfie_type";
    static private final String EXTRA_USER_ID="user_id";

    private ImageView mSelfieImage;
    private long mImageId;
    private String mUserId;
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
                    mImageId = getIntent().getLongExtra(EXTRA_IMAGE_ID, -1);
                    mUserId=getIntent().getStringExtra(EXTRA_USER_ID);
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

        View btnBlackWhite=findViewById(R.id.black_white_button);
        btnBlackWhite.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        mSelfieImage.setImageBitmap(null);
        super.onDestroy();
    }

    static public Intent makeIntent(Context context, long imageId, int selfieType,String userId) {
        Intent intent = new Intent(context, SelfieDetailsActivity.class);
        intent.putExtra(EXTRA_IMAGE_ID, imageId);
        intent.putExtra(EXTRA_SELFIE_TYPE, selfieType);
        intent.putExtra(EXTRA_USER_ID,userId);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.black_white_button:
                Intent serviceIntent= SelfieEffectsService.makeServiceIntent(this,mImageId,mUserId,PhotoUtils.EFFECT_BLACK_WHITE);
                startService(serviceIntent);
                break;
        }
    }
}
