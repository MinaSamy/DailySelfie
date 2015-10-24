package bloodstone.dailyselfie.android.fragments;

import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.animation.ValueAnimatorCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import bloodstone.dailyselfie.android.R;
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
    private Button mLoginButton;
    private View mProgressView;
    private View mLoginFormView;

    private OnFragmentInteractionListener mListener;

    //authentication task
    private AuthenticationTask mAuthTask;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoginFragment.
     */

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
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
        mLoginButton = (Button) v.findViewById(R.id.sign_in_button);
        mLoginButton.setOnClickListener(this);
        mProgressView = v.findViewById(R.id.login_progress);
        mLoginFormView = v.findViewById(R.id.login_form);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            login();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.password) {
            if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
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
                mEmailView.setError(getString(R.string.mandatory_email));
            } else if (!ValidationUtils.isEmailValid(email)) {
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public class AuthenticationTask extends AsyncTask<Void, Void, Boolean> {

        private String mEmail;
        private String mPassword;

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        public AuthenticationTask(String email, String password) {
            this.mEmail = email;
            this.mPassword = password;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask=null;
            showProgress(false);
        }
    }

}
