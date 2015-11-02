package bloodstone.dailyselfie.android.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.helper.AuthenticationHelper;
import bloodstone.dailyselfie.android.utils.ValidationUtils;

/**
 * Created by minsamy on 11/1/2015.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener,EditText.OnEditorActionListener {

    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mDisplayNameText;
    private ProgressBar mProgressBar;
    private View mRegisterForm;


    private onRegistrationFragmentInteractionListener mListener;
    private RegisterTask mRegisterTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_register,container,false);
        mEmailText=(EditText)v.findViewById(R.id.txtEmail);
        mPasswordText=(EditText)v.findViewById(R.id.txtPassword);
        mDisplayNameText=(EditText)v.findViewById(R.id.txtDisplayName);
        mDisplayNameText.setOnEditorActionListener(this);
        Button btnRegister=(Button)v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        mRegisterForm=v.findViewById(R.id.register_form);
        mProgressBar=(ProgressBar)v.findViewById(R.id.register_progress);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnRegister){
            register();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(v.getId()==R.id.txtDisplayName ||actionId==R.id.register){
            //hide keyboard
            InputMethodManager mngr=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mngr.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            register();
            return true;
        }
        return false;
    }


    private void register(){


        if(mRegisterTask!=null){
            return;
        }

        String email=mEmailText.getText().toString();
        String password=mPasswordText.getText().toString();
        String displayName=mDisplayNameText.getText().toString();

        //remove error messages
        mEmailText.setError(null);
        mPasswordText.setError(null);
        mDisplayNameText.setError(null);

        if(ValidationUtils.areCredentialsValid(email,password) && !TextUtils.isEmpty(displayName)){
            mRegisterTask=new RegisterTask(email,password,displayName);
            mRegisterTask.execute((Void)null);
        }else{
            if(TextUtils.isEmpty(email)){
                mEmailText.requestFocus();
                mEmailText.setError(getString(R.string.mandatory_email));
            }else if(!ValidationUtils.isEmailValid(email)){
                mEmailText.requestFocus();
                mEmailText.setError(getString(R.string.invalid_email));
            }

            if(TextUtils.isEmpty(password)){
                mPasswordText.requestFocus();
                mPasswordText.setError(getString(R.string.mandatory_password));
            }else if(!ValidationUtils.isPasswordValid(password)){
                mPasswordText.requestFocus();
                mPasswordText.setError(getString(R.string.mandatory_password));
            }

            if(TextUtils.isEmpty(displayName)){
                mDisplayNameText.setError(getString(R.string.mandatory_display_name));
            }
        }
    }


    private void showProgress(boolean show){
        mRegisterForm.setEnabled(show);
        mProgressBar.setVisibility(show?View.VISIBLE:View.GONE);
    }

    private class RegisterTask extends AsyncTask<Void,Void,Boolean>{

        private String mEmail;
        private String mPassword;
        private String mDisplayName;

        private RegisterTask(String email, String password, String displayName) {
            this.mEmail = email;
            this.mPassword = password;
            this.mDisplayName = displayName;
        }


        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result= AuthenticationHelper.register(mEmail,mPassword,mDisplayName);
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showProgress(false);
            if(mListener!=null){
                mListener.onRegistrationComplete(result);
            }
            mRegisterTask=null;
        }
    }

    public interface onRegistrationFragmentInteractionListener{
        void onRegistrationComplete(boolean result);
        void onRegistrationError(String error);
    }

    public void setonRegistrationFragmentInteractionListener(onRegistrationFragmentInteractionListener listener){
        this.mListener=listener;
    }
}
