package bloodstone.dailyselfie.android.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bloodstone.dailyselfie.android.MainActivity;
import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.helper.AuthenticationHelper;
import bloodstone.dailyselfie.android.model.LoginResponse;
import bloodstone.dailyselfie.android.utils.PhotoUtils;
import bloodstone.dailyselfie.android.utils.NetUtils;
import bloodstone.dailyselfie.android.utils.ValidationUtils;

/**
 * @author minsamy
 *         The login fragment.
 *         Authenticates user credentials with aws cognito service
 *         Implements View.OnClickListener for handling the login action
 *         Implements TextView.OnEditorActionListener for handling login action after user enters the password in the password view
 */
public class LoginFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private OnLoginFragmentInteractionListener mListener;

    //authentication task
    private AuthenticationTask mAuthTask;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoginFragment.
     */

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retain fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mEmailView = (AutoCompleteTextView) v.findViewById(R.id.email);
        mPasswordView = (EditText) v.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(this);
        Button mLoginButton = (Button) v.findViewById(R.id.sign_in_button);
        mLoginButton.setOnClickListener(this);
        mProgressView = v.findViewById(R.id.login_progress);
        mLoginFormView = v.findViewById(R.id.login_form);
        Button btnSignUp=(Button)v.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        return v;
    }


    public void setOnLoginFragmentInteractionListener(OnLoginFragmentInteractionListener listener){
        this.mListener=listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.sign_in_button) {
            login();
        }else if(v.getId()==R.id.btnSignUp){
            if(mListener!=null){
                mListener.navigateToSignUp();
            }
        }*/

       /*Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                PhotoUtils cam=new PhotoUtils();
                cam.test(getActivity());
            }
        });
        t.start();*/
        Intent intent=MainActivity.makeMainActivityIntent(getActivity(),"user1");
        startActivity(intent);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.password) {
            if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
                //dismiss keyboard
                InputMethodManager imm=(InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                login();
                return true;
            }
        }
        return false;
    }


    /**
     * Authenticates user credentials with the server.
     */
    private void login() {
        if (mAuthTask != null) {
            return;
        }

        //reset error messages
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (ValidationUtils.areCredentialsValid(email, password)) {
            mAuthTask = new AuthenticationTask(email, password);
            mAuthTask.execute((Void) null);
        } else {
            //show error messages
            if (TextUtils.isEmpty(email)) {
                mEmailView.requestFocus();
                mEmailView.setError(getString(R.string.mandatory_email));
            }
            if (!ValidationUtils.isEmailValid(email)) {
                mEmailView.requestFocus();
                mEmailView.setError(getString(R.string.invalid_email));
            }

            //check password
            if (TextUtils.isEmpty(password)) {
                mPasswordView.setError(getString(R.string.mandatory_password));
            } else if (!ValidationUtils.isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.invalid_password));
            }
        }
    }

    private void showProgress(boolean show) {
        mLoginFormView.setEnabled(show);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

    }


    public interface OnLoginFragmentInteractionListener {
        void onLoginComplete(LoginResponse response);
        void onError(String message);
        void navigateToSignUp();
    }


    public class AuthenticationTask extends AsyncTask<Void, Void, LoginResponse> {

        private String mEmail;
        private String mPassword;

        @Override
        protected void onPreExecute() {
            if (!NetUtils.isNetworkAvailable(LoginFragment.this.getActivity())) {
                if(mListener!=null){
                    mListener.onError(getString(R.string.network_unavailable));
                }
            }else{
                showProgress(true);
            }

        }

        public AuthenticationTask(String email, String password) {
            this.mEmail = email;
            this.mPassword = password;
        }


        @Override
        protected LoginResponse doInBackground(Void... params) {
            LoginResponse response= AuthenticationHelper.login(mEmail,mPassword);
            return  response;
        }

        @Override
        protected void onPostExecute(LoginResponse result) {
            showProgress(false);
            if(mListener!=null){
                if(result!=null &&result.isAuthenticated()){
                    mListener.onLoginComplete(result);
                }else{
                    mListener.onError(getString(R.string.invalid_credentials));
                }
            }
            mAuthTask=null;

        }

        @Override
        protected void onCancelled() {
            mAuthTask=null;
            showProgress(false);
        }
    }

}
