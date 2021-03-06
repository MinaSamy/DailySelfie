package bloodstone.dailyselfie.android.helper;


import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.model.LoginResponse;
import bloodstone.dailyselfie.android.utils.LogUtil;
import bloodstone.dailyselfie.android.utils.NetUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.Hashtable;
import java.util.Map;

import javax.security.auth.PrivateCredentialPermission;

/**
 * Created by minsamy on 10/31/2015.
 */
public class AuthenticationHelper {

    private static final String TAG=AuthenticationHelper.class.getName();

    public static final String BASE_URL = "http://dailyselfiecloud-env.elasticbeanstalk.com/";
    private static final String URL_LOGIN = BASE_URL + "login";
    private static final String URL_REGISTER=BASE_URL+"register";


    private static final String HEADER_USER_NAME="username";
    private static final String HEADER_PASSWORD="password";

    public static LoginResponse login(String userName, String password) {

        LoginResponse loginResponse=null;
        Map<String,String>headers= new Hashtable<String,String>();
        headers.put(HEADER_USER_NAME,userName);
        headers.put(HEADER_PASSWORD,password);
        String response=NetUtils.post(URL_LOGIN,headers,null);
        if(response!=null){
            try {
                loginResponse=UserJsonParser.getLoginResponse(response);
            } catch (JSONException e) {
                LogUtil.logError(TAG,e.toString());
            }
        }

        return loginResponse;
    }

    public static boolean register(String email,String password,String displayName){
        boolean result=false;
        try {
            String requestBody=UserJsonParser.getRegistrationRequestBody(email,password,displayName);
            result=Boolean.parseBoolean(NetUtils.post(URL_REGISTER,null,requestBody));
        } catch (JSONException e) {
            Log.e(TAG,e.toString());
        }
        return result;
    }
}
