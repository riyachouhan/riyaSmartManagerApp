package com.ddkcommunity.utilies;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ritesh on 18/7/17.
 */

public class Validation {
    public static boolean checkMobile(String mobile) {
        return mobile.length() > 0 || mobile.length() <= 10;
    }

    public static boolean checkEmail(String email) {
        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkNull(String value) {
        try {
            return value.length() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkPassword(String passwordString) {
        return passwordString != null && passwordString.length() >= 3;
    }
}
