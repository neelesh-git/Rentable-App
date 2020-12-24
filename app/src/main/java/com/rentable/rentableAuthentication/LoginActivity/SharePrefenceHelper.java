package com.rentable.rentableAuthentication.LoginActivity;

import android.content.Context;
import android.content.SharedPreferences;

 /*
 * SingleTon class Implementation to avoid multiple object creation.
 */
public class SharePrefenceHelper {
    private final String PASSWORD = "PHONENUMBER";
    private final String EMAIL = "EMAIL";
    private final String TYPE = "LOGINDETAILS";
    private static SharePrefenceHelper mSharePrefenceHelper = null;
    private Context mContext;

    public static SharePrefenceHelper getInstance(Context context)
    {
        if (mSharePrefenceHelper == null)
        {
            mSharePrefenceHelper = new SharePrefenceHelper(context);
        }
        return mSharePrefenceHelper;
    }


    private SharePrefenceHelper(Context mContext) {
        this.mContext = mContext;
    }


    public void saveLoginDeatils(String email, String password)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(TYPE, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, password);
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getEmail()
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(TYPE, mContext.MODE_PRIVATE);
        return  sharedPreferences.getString(EMAIL, "");
    }

    public String getPassword()
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(TYPE, mContext.MODE_PRIVATE);
        return  sharedPreferences.getString(PASSWORD, "");
    }


    public void clearData()
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(TYPE, mContext.MODE_PRIVATE);
    }

    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(TYPE, Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString(EMAIL, "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString(PASSWORD, "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }


    public void clear()
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(TYPE, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
