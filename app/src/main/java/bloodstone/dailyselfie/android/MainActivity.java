package bloodstone.dailyselfie.android;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.io.IOException;

import bloodstone.dailyselfie.android.adapter.MainPagerAdapter;
import bloodstone.dailyselfie.android.utils.CameraUtils;

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

    private final static int CAMERA_CAPTURE_REQUEST_CODE=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_translate);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),this);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(final View v) {
        final Handler handler=new Handler(){
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
        };

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=handler.obtainMessage();
                try {
                    //TODO retrieve correct user id from login/registration API
                    Intent intent= CameraUtils.makeCameraCaptureIntent(MainActivity.this,CAMERA_CAPTURE_REQUEST_CODE,
                            "user1",CameraUtils.PHOTO_TYPE_NORMAL_SELFIE);
                    if(intent!=null){
                        message=handler.obtainMessage();
                        message.obj=intent;
                    }
                    else{
                        message.arg1=-1;
                    }
                } catch (IOException e) {
                    message.arg1=-2;
                }finally {
                    message.sendToTarget();
                }
            }
        });
        t.start();
    }
}
