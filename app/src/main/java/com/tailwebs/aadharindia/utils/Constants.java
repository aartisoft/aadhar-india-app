package com.tailwebs.aadharindia.utils;

import android.os.Environment;

public class Constants {

//    public static final String BASE_URL = "http://staging.tailwebs.com:3001/api/v1/";
//    public static final String BASE_URL = "http://192.168.0.107:3000/api/v1/";
//    public static final String BASE_URL = "http://192.168.1.10:3001/api/v1/"; //satheesh
//    public static final String BASE_URL = "http://192.168.1.27:3000/api/v1/"; //kundan
    public static final String BASE_URL = "http://app.aadharindiafin.in:3000/api/v1/";//server    versionName "1.5.20"
//    public static final String BASE_URL = "http://app.aadharindiafin.in:3001/api/v1/";//server anish test    versionName "1.0.21"
//    public static final String BASE_URL = "http://13.126.157.149:3000/api/v1/";


    /**
     * Shared Preference Variables
     */
    public static final String KEY_SECRET = "secret";
    public static final String KEY_SECRET_ID = "secret-id";
    public static String KEY_LOGGED_STATUS = "logged_status";
    public static String KEY_PROVIDER = "provider";
    public static String KEY_USER_TYPE = "user_type";
    public static String KEY_CATEGORY = "category";
    public static String KEY_CITY = "city";
    public static String KEY_CITY_ID = "city_id";
    public static String KEY_AADHAAR_USER_ID = "aadhar_user_id";
    public static String KEY_iS_FIRST_LAUNCHED = "is_first_launched";
    public static String KEY_USER_IMAGE = "user_image";


    public static String KEY_USER = "user";
    public static String KEY_USER_ID = "user_id";
    public static String KEY_USER_NAME = "user_name";

    public static final String EMAIL = "e_mail";
    public static final String MOBILE = "mobile";

    public static final String PUSH_TOKEN = "push_token";



    public static final String FEEDBACK_IMAGES_FOLDER_PATH = Environment.getExternalStorageDirectory().toString() + "/aadhaar_india/feedback";


    /**
     *
     */
    public static final String HTTP_LOCALE = "HTTP_LOCALE";
    public static final String HTTP_TIMEZONE = "HTTP_TIMEZONE";


}
