package bloodstone.dailyselfie.android.utils;

import android.text.TextUtils;
import android.util.Patterns;

import org.w3c.dom.Text;

/**
 * Created by minsamy on 10/24/2015.
 */
public class ValidationUtils {

    /**
     * Validates email and password
     *
     * @param email
     * @param password
     * @return
     */
    static public boolean areCredentialsValid(String email, String password) {
        boolean isEmailValid = !TextUtils.isEmpty(email)
                && isEmailValid(email);
        boolean isPasswordValid = isPasswordValid(password);
        return isEmailValid && isPasswordValid;
    }


    static public boolean isPasswordValid(String password){
        return password.length() >= 6;
    }


    static public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
