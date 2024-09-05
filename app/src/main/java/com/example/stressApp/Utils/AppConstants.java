package com.example.stressApp.Utils;

import com.example.stressApp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppConstants {
    public static final String DATABASE_TEST            = "Test-Database"       ;
    public static final String PREF_CREDENTIALS         = "User-Credentials"    ;
    public static final String KEY_LOGIN_FLAG           = "Login-Flag"          ;

    public static final String KEY_USER_ID              = "User-Id"             ;
    public static final String LOG_LOGIN                = "App-Login-Logs"      ;
    public static final String LOG_REGISTER             = "App-Register-Logs"   ;
    public static final String LOG_YOGA                 = "App-Yoga-Logs"       ;
    public static final String LOG_ACTIVITY             = "App-Activity-Logs"   ;
    public static final String LOG_SETTING              = "App-Setting-Logs"    ;
    public static final String DATA_USER_INFO           = "User-Information"    ;
    public static final String DATA_YOGA_INFO           = "Yoga-Information"    ;
    public static final String KEY_NAME                 = "name"                ;
    public static final String KEY_INFO                 = "info"                ;
    public static final String KEY_HELP                 = "help"                ;
    public static final String KEY_HOW_TO_DO            = "how-to-do"           ;
    public static final String KEY_STRETCHED_PART       = "stretched-part"      ;
    public static final String KEY_TIPS                 = "tips"                ;
    public static final String KEY_PATH                 = "path"                ;


    public static final String DATA_CREDENTIALS         = "Credentials"         ;
    public static final String FIREBASE_URL             =  "https://stress-management-app-7a8f9-default-rtdb.firebaseio.com/";

    public static final String KEY_MOBILE_NUMBER        = "Mobile-Number"       ;
    public static final String KEY_GMAIL                = "Gmail"               ;
    public static final String KEY_PASSWORD             = "Password"            ;
    public static final String KEY_USER_NAME            = "User-Name"           ;
    public static final String ROOT_FRAGMENT_TAG        = "Root-Fragment"       ;

    public static final String FRAGMENT_HOME            = "HomeFragment"        ;
    public static final String FRAGMENT_OTHERS          = "OthersFragment"      ;
    public static final String FRAGMENT_YOGA            = "YogaFragment"        ;
    public static final String FRAGMENT_SETTING         = "SettingFragment"     ;

    public static final Map<String, Integer> fragmentMap = new HashMap<>();
    static {
        fragmentMap.put(FRAGMENT_HOME, R.id.homeFragment);
        fragmentMap.put(FRAGMENT_OTHERS, R.id.otherFragment);
        fragmentMap.put(FRAGMENT_YOGA, R.id.yogaFragment);
        fragmentMap.put(FRAGMENT_SETTING, R.id.settingFragment);
    }

    public static final Map<String, Integer> drawableMap = new HashMap<>();
    static {
        drawableMap.put("bridge", R.drawable.bridge);
        drawableMap.put("cat", R.drawable.cat);
        drawableMap.put("child", R.drawable.child);
        drawableMap.put("corpse", R.drawable.corpse);
        drawableMap.put("lotus", R.drawable.lotus);
        drawableMap.put("revolver", R.drawable.revolver);
        drawableMap.put("seat", R.drawable.seat);
        drawableMap.put("standing", R.drawable.standing);
        drawableMap.put("eagle", R.drawable.eagle);
        drawableMap.put("music", R.drawable.music);
        drawableMap.put("high", R.drawable.high);
        drawableMap.put("fidget", R.drawable.fidget);
        drawableMap.put("stress", R.drawable.stress_test);
    }

    public static final ArrayList<String> PREF_LIST = new ArrayList<>();
    static {
        PREF_LIST.add(PREF_CREDENTIALS);
    }

}
