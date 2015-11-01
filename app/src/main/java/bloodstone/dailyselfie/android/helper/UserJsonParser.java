package bloodstone.dailyselfie.android.helper;

import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import bloodstone.dailyselfie.android.model.LoginResponse;

/**
 * Created by minsamy on 10/31/2015.
 */
public class UserJsonParser {

    static private final String TOKEN_RESULT="result";
    static private final String TOKEN_USER_ID="userId";
    static private final String TOKEN_USER_NAME="userName";
    static private final String TOKEN_PASSWORD="passowrd";
    static private final String TOKEN_DISPLAY_NAME="displayName";

    static public LoginResponse getLoginResponse(String json) throws JSONException {
        LoginResponse response=new LoginResponse();
        JSONObject object=new JSONObject(json);
        response.setAuthenticated(object.getBoolean(TOKEN_RESULT));
        response.setUserId(object.getString(TOKEN_USER_ID));
        return response;
    }

    static public String getRegistrationRequestBody(String email,String password,String displayName) throws JSONException {
        JSONObject object=new JSONObject();
        object.put(TOKEN_USER_NAME,email);
        object.put(TOKEN_PASSWORD,password);
        object.put(TOKEN_DISPLAY_NAME,displayName);
        return object.toString();
    }
}
