package bloodstone.dailyselfie.android.helper;

import org.json.JSONException;
import org.json.JSONObject;

import bloodstone.dailyselfie.android.model.LoginResponse;

/**
 * Created by minsamy on 10/31/2015.
 */
public class UserJsonParser {

    static private final String TOKEN_RESULT="result";
    static private final String TOKEN_USER_ID="userId";

    static public LoginResponse getLoginResponse(String json) throws JSONException {
        LoginResponse response=new LoginResponse();
        JSONObject object=new JSONObject(json);
        response.setResult(object.getBoolean(TOKEN_RESULT));
        response.setUserId(object.getString(TOKEN_USER_ID));
        return response;
    }
}
