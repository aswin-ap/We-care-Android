package com.example.carehomemanagement.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private Pattern pattern;
    private Matcher matcher;

    private static final String DATE_PATTERN =
            "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";

    public static boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isValidPassword(String email) {
        return email.length() >= 6;
    }

    public static boolean isPasswordConfirm(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public static boolean isValidField(String field) {
        return (!TextUtils.isEmpty(field));
    }

}