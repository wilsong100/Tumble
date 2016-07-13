package com.wilsong.tumble;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gerard on 11/07/2016.
 * This class contains methods which contain validation rules for user input on the Registration and
 * Login screens.
 */
public class Validation {
    //regular expression for email accepting A-Z, a-z, or 0-9 on the left of the @ symbol
    //must contain an '@' symbol
    //after @ symbol, must contain lower case letters a-z
    //must contain a '.' (a dot)
    //private static final String EMAIL_REGEX = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]";
   // private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9]++@[a-z]++[.]++[A-Za-z]");
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    //Error messages
    private static final String REQUIRED_MSG = "Required Field";
    private static final String EMAIL_MSG = "Invalid Email";
    private static final String PASSWORD_LENGTH_MSG = "Password must be a minimum of 6 characters";
    private static final String PASSWORD_MATCH_MSG = "Passwords do not match";


    /**
     * method to check email is valid
     */
    public static boolean isEmailValid(EditText editText,boolean required){
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    /**
     * method returns true if input field is valid based on the parameter passed
     */
    public static boolean isValid(EditText editText, Pattern regex, String errorMsg, boolean required ){
        String text = editText.getText().toString().trim();
        editText.setError(null);

        //text required and editText is blank, so return false
        if (required && !hasText(editText))
            return false;

        // pattern doesn't match regex so return false
        if (required && !checkEmail(editText)){
            editText.setError(errorMsg);
            return false;
        }
        return true;
    }

    /**
     *  method checks that the field has been filled in, returns false if no text or
     * returns true if there is text present
     */
    public static boolean hasText(EditText editText){
        String text = editText.getText().toString().trim();
        editText.setError(null);

        // if length is 0 then it means there is no text present
        if(text.length() == 0){
            editText.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }

    public static boolean checkEmail(EditText editText){
        String email = editText.getText().toString();
        Matcher matcher = EMAIL_REGEX.matcher((CharSequence) email);
        if(!matcher.find()){
            return false;
        }
        return true;
    }


    public static boolean passwordLengthValid(EditText password){

        if (password.length()<6){
            //set error message content
            password.setError(PASSWORD_LENGTH_MSG);
            return false;
        }
        return true;
    }
    public static boolean passwordsMatch(EditText password, EditText confirmPassword){
        String p = password.getText().toString();
        String cp = confirmPassword.getText().toString();
        if (!cp.matches(p)){
            //set error message content
            confirmPassword.setError(PASSWORD_MATCH_MSG);
            return false;
        }
        return true;
    }
}
