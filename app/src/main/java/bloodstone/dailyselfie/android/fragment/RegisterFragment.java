package bloodstone.dailyselfie.android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bloodstone.dailyselfie.android.R;

/**
 * Created by minsamy on 11/1/2015.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener,EditText.OnEditorActionListener {

    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mDisplayNameText;

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
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnRegister){

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
