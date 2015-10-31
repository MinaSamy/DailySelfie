package bloodstone.dailyselfie.android;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import bloodstone.dailyselfie.android.fragment.LoginFragment;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private FrameLayout mFragmentContainer;
    private LoginFragment mLoginFragment;

    //fragments tags
    private final String FRAGMENT_LOGIN = "login_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFragment = (LoginFragment) getFragmentManager().findFragmentByTag(FRAGMENT_LOGIN);
        if (mLoginFragment == null) {
            mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
            mLoginFragment = LoginFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, mLoginFragment, FRAGMENT_LOGIN);
            transaction.commit();
        }
    }


}

