package bloodstone.dailyselfie.android;

import android.app.FragmentManager;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import bloodstone.dailyselfie.android.fragment.LoginFragment;
import bloodstone.dailyselfie.android.fragment.RegisterFragment;
import bloodstone.dailyselfie.android.model.LoginResponse;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener, RegisterFragment.onRegistrationFragmentInteractionListener {

    private FrameLayout mFragmentContainer;
    private LoginFragment mLoginFragment;

    //fragments tags
    private final String FRAGMENT_LOGIN = "login_fragment";
    private final String FRAGMENT_REGISTER = "register_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_LOGIN);
        if (mLoginFragment == null) {
            mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
            mLoginFragment = LoginFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, mLoginFragment, FRAGMENT_LOGIN);
            transaction.commit();
        }

        mLoginFragment.setOnLoginFragmentInteractionListener(this);


    }


    @Override
    public void onLoginComplete(LoginResponse response) {
        if(response.isAuthenticated()){
            //TODO navigate to next view
        }
    }

    @Override
    public void onError(String message) {
        Snackbar.make(mFragmentContainer, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void navigateToSignUp() {
        RegisterFragment registerFragment=null;
        registerFragment=(RegisterFragment)getSupportFragmentManager().findFragmentByTag(FRAGMENT_REGISTER);
        if(registerFragment==null){
            registerFragment=new RegisterFragment();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, registerFragment, FRAGMENT_REGISTER);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        }
        registerFragment.setonRegistrationFragmentInteractionListener(this);

    }

    @Override
    public void onRegistrationComplete(boolean result) {
        if(result){
            //Navigate to main activity
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Snackbar.make(mFragmentContainer,R.string.try_again,Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRegistrationError(String error) {
        Snackbar.make(mFragmentContainer,error,Snackbar.LENGTH_LONG).show();
    }
}

