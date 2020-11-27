package com.tailwebs.aadharindia.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tailwebs.aadharindia.models.User;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class SharedPreferenceUtils {


//+Calendar.getInstance().getTime()
//    public static final String PREFS_NAME = "aadhaar_india_shared_pref_v_1_11_26__21_10_";

    public static final String PREFS_NAME = "aadhaar_india_shared_pref_v_1_10_25_"+Calendar.getInstance().getTime();

    public SharedPreferenceUtils() {
        super();
    }

    public static void saveValue(Context context, String KEY_NAME, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(KEY_NAME, text); //3

        editor.commit(); //4
    }

    public static String getValue(Context context, String KEY_NAME) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(KEY_NAME, null);
        return text;
    }

    public static void saveDataObject(Context context, String KEY_NAME, Object value) {
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        prefsEditor.putString(Constants.KEY_USER, json);
        prefsEditor.apply();
    }

    public static User getUserData(Context context, String KEY_NAME) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_NAME, null);
        Type type = new TypeToken<User>() {
        }.getType();
        try {
            User user = gson.fromJson(json, type);
            return user;
        } catch (Exception e) {
            return new User();
        }
    }

    public static void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public static void removeValue(Context context, String KEY_NAME) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(KEY_NAME);
        editor.commit();
    }

    public static boolean isFirstLaunched(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(Constants.KEY_iS_FIRST_LAUNCHED, false);
    }

    public static void setFirstLaunched(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(Constants.KEY_iS_FIRST_LAUNCHED, loggedIn);
        editor.apply();
    }



    public static void setUserLoggedIn(Context context, boolean loggedIn, String provider) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(Constants.KEY_LOGGED_STATUS, loggedIn);
        editor.putString(Constants.KEY_PROVIDER, provider);
        editor.apply();
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(Constants.KEY_LOGGED_STATUS, false);
    }

    public static boolean isCategoryArrayAvailable(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(Constants.KEY_CATEGORY, false);
    }

    public static void clearUserLoggedIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
