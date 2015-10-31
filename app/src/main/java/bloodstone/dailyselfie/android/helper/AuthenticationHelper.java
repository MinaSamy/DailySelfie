package bloodstone.dailyselfie.android.helper;


import bloodstone.dailyselfie.android.R;
import bloodstone.dailyselfie.android.model.LoginResponse;
import bloodstone.dailyselfie.android.utils.LogUtil;
import bloodstone.dailyselfie.android.utils.NetUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;

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

    private static final String BASE_URL = "http://dailyselfiecloud-env.elasticbeanstalk.com/";
    private static final String URL_LOGIN = BASE_URL + "login";
    private static final String URL_REGISTER=BASE_URL+"register";

    private static final String HEADER_USER_NAME="username";
    private static final String HEADER_PASSWORD="password";

    public static LoginResponse login(String userName, String password, Context context, Handler errorHandler) {

        LoginResponse loginResponse=null;
        if (!NetUtils.isNetworkAvailable(context)) {
            Message msg = errorHandler.obtainMessage();
            msg.obj = context.getString(R.string.network_unavailable);
            msg.sendToTarget();
            errorHandler.sendMessage(msg);
        }

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
}
