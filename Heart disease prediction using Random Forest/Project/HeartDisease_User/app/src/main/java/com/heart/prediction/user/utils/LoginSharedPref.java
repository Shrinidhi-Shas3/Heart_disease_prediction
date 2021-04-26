package com.heart.prediction.user.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSharedPref {
    private static final String SPREF_FILE = "HeartUser";
    private static final String PID_KEY = "PID_KEY";
    //private static final String NAME_KEY = "NAME_KEY";

    //get and set id and name
    public static void setPid(Context context, String pid) {
        SharedPreferences sPref = context.getSharedPreferences(SPREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(PID_KEY, pid);
        editor.apply();
    }

   /* public static void setDname(Context context, String dname) {
        SharedPreferences sPref = context.getSharedPreferences(SPREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(NAME_KEY, dname);
        editor.apply();
    }*/

    public static String getUserId(Context context) {
        SharedPreferences sPref = context.getSharedPreferences(SPREF_FILE, Context.MODE_PRIVATE);
        return sPref.getString(PID_KEY, "");
    }

   /* public static String getName(Context context) {
        SharedPreferences sPref = context.getSharedPreferences(SPREF_FILE, Context.MODE_PRIVATE);
        return sPref.getString(NAME_KEY, "");
    }*/
}
