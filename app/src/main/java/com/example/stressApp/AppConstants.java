package com.example.stressApp;

import java.util.HashMap;
import java.util.Map;

public class AppConstants {
    public static final String DATABASE_TEST    = "Test-Database";
    public static final String PREF_CREDENTIALS         = "User-Credentials";
    public static final String KEY_LOGIN_FLAG           = "Login-Flag"      ;

    public static final String KEY_USER_ID              = "User-Id"         ;
    public static final String LOG_LOGIN                = "App-Login-Logs"  ;
    public static final String LOG_REGISTER             = "App-Register-Logs"  ;
    public static final String DATA_USER_INFO           = "User-Information";


    public static final String DATA_CREDENTIALS         = "Credentials"     ;
    public static final String FIREBASE_URL             =  "https://stress-management-app-7a8f9-default-rtdb.firebaseio.com/";

    public static final String KEY_MOBILE_NUMBER        = "Mobile-Number"   ;
    public static final String KEY_GMAIL                = "Gmail"           ;
    public static final String KEY_PASSWORD             = "Password"        ;
    public static final String KEY_USER_NAME            = "User-Name"       ;
    public static final String ROOT_FRAGMENT_TAG = "Root-Fragment";

    public static final String FRAGMENT_HOME = "HomeFragment";
    public static final String FRAGMENT_OTHERS = "OthersFragment";
    public static final String FRAGMENT_YOGA = "YogaFragment";
    public static final String FRAGMENT_SETTING = "SettingFragment";

    public static final Map<String, Integer> fragmentMap = new HashMap<>();
    static {
        fragmentMap.put(FRAGMENT_HOME, R.id.home_icon);
        fragmentMap.put(FRAGMENT_OTHERS, R.id.others_icon);
        fragmentMap.put(FRAGMENT_YOGA, R.id.yoga_icon);
        fragmentMap.put(FRAGMENT_SETTING, R.id.setting_icon);
    }

}
