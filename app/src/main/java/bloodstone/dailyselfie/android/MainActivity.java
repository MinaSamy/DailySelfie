package bloodstone.dailyselfie.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.Message;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.IOException;

import bloodstone.dailyselfie.android.adapter.MainPagerAdapter;
import bloodstone.dailyselfie.android.fragment.PhotosFragment;
import bloodstone.dailyselfie.android.utils.LogUtil;
import bloodstone.dailyselfie.android.utils.PhotoUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainPagerAdapter mPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String mUserId=PhotoUtils.ANON_USER;

    private final static int CAMERA_CAPTURE_REQUEST_CODE=10;
    private final static String EXTRA_USER_ID="user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_translate);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        if(getIntent().hasExtra(EXTRA_USER_ID)){
            mUserId=getIntent().getStringExtra(EXTRA_USER_ID);
        }
        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),this,mUserId);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=SettingsActivity.makeIntent(this,mUserId);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    static public Intent makeMainActivityIntent(Context context, String userId){
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra(EXTRA_USER_ID,userId);
        return intent;
    }

    @Override
    public void onClick(final View v) {
        /*final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==-1){
                    //capture intent is null
                }else if(msg.arg1==-2){
                    //IO exception
                }else{
                    Intent intent=(Intent)msg.obj;
                    startActivityForResult(intent,CAMERA_CAPTURE_REQUEST_CODE);
                }

            }
        };*/

        //Message message=handler.obtainMessage();
        try {
            //TODO retrieve correct user id from login/registration API
            Intent intent= PhotoUtils.makeCameraCaptureIntent(this,CAMERA_CAPTURE_REQUEST_CODE,
                    mUserId, PhotoUtils.PHOTO_TYPE_NORMAL_SELFIE);
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent,CAMERA_CAPTURE_REQUEST_CODE);
            }


        } catch (IOException e) {
            LogUtil.logError("MainActivity",e.toString());
        }catch (Exception ex){
            LogUtil.logError("MainActivity",ex.toString());
        }
        finally {
            //message.sendToTarget();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            if(data!=null &&data.getData()!=null){
                Log.e("DATA",data.getData().toString());
            }
        }
    }
}
