package com.example.carehomemanagement.data.prefrence;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private static final String TAG = SessionManager.class.getSimpleName();

    private final SharedPreferences shpref;
    private final SharedPreferences.Editor editor;
    Context context;
    private static final String PREF_NAME = "WeApp";
    private static final String KEY_IS_LOGED_IN = "isLoggedIn";
    private static final String USERID = "userId";
    private static final String USERNAME = "userName";
    private static final String USERTYPE = "userType";
    private static final String DOCUMENT_ID = "documentId";

    public SessionManager(Context context) {
        this.context = context;
        shpref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = shpref.edit();
    }

    public void setLogin(boolean isLoggedin) {
        editor.putBoolean(KEY_IS_LOGED_IN, isLoggedin);
        editor.apply();
        Log.e(TAG, "userlogin session");
    }

    public void setUserId(String name) {
        editor.putString(USERID, name);
        editor.apply();
    }

    public void setUserName(String name) {
        editor.putString(USERNAME, name);
        editor.apply();
    }

    public void setDocumentId(String id) {
        editor.putString(DOCUMENT_ID, id);
        editor.apply();
    }

    public void setUserTypeId(String type) {
        editor.putString(USERTYPE, type);
        editor.apply();
    }


    public boolean isLoggedin() {
        return shpref.getBoolean(KEY_IS_LOGED_IN, false);
    }

    public String getUserId() {
        return shpref.getString(USERID, "");
    }

    public String getUserName() {
        return shpref.getString(USERNAME, "");
    }

    public String getDocumentId() {
        return shpref.getString(DOCUMENT_ID, "");
    }

    public String getUserType() {
        return shpref.getString(USERTYPE, "");
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}

