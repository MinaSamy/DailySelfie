package bloodstone.dailyselfie.android.model;

/**
 * Created by minsamy on 10/31/2015.
 */
public class LoginResponse {
    private boolean result;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    private String userId;
}
