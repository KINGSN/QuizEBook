package com.example.androidebookapp.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.androidebookapp.item.QuizCategoryList;
import com.example.androidebookapp.util.GlobalVariables;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SharedPrefrence {
    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor prefsEditor;

    public static SharedPrefrence myObj;
    public static final String DATE_STRING = "dateString";

    public static final String SETTING_Quiz_PREF = "setting_quiz_pref";
    private static final String SOUND_ONOFF = "sound_enable_disable";
    private static final String SHOW_MUSIC_ONOFF = "showmusic_enable_disable";
    private static final String LANG = "language_enable_disable";
    private static final String VIBRATION = "vibrate_status";
    public static final String TOTAL_SCORE = "total_score";
    public static final String POINT = "no_of_point";
    public static final String LEVEL_COMPLETED = "level_completed";
    public static final String IS_LAST_LEVEL_COMPLETED = "is_last_level_completed";
    public static final String LAST_LEVEL_SCORE = "last_level_score";
    public static final String COUNT_QUESTION_COMPLETED = "count_question_completed";
    public static final String COUNT_RIGHT_ANSWARE_QUESTIONS = "count_right_answare_questions";
    public static final String LEVEL_COMPLETED_ACHIVEMENT = "level_completed_achivement";

    private static Vibrator sVibrator;
    public static int TotalQuestion =1;
    public static int CoreectQuetion=1;
    public static int WrongQuation=1;
    public static int level_coin=1;
    public static final long VIBRATION_DURATION = 100;
    public static final String PLAYSTORE_URL = "http://play.google.com/store/apps/details?id=";
    public static int RequestlevelNo = 1;
    public static final boolean DEFAULT_SOUND_SETTING = true;
    public static final boolean DEFAULT_VIBRATION_SETTING = true;
    public static final boolean DEFAULT_MUSIC_SETTING = false;
    public static final boolean DEFAULT_LAN_SETTING = true;

    private SharedPrefrence() {

    }

    @SuppressLint("ApplySharedPref")
    public void clearAllPreferences() {
        prefsEditor = myPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }


    public static SharedPrefrence getInstance(Context ctx) {
        if (myObj == null) {
            myObj = new SharedPrefrence();
            myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            prefsEditor = myPrefs.edit();
        }
        return myObj;
    }

    public void clearPreferences(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }


    public void setIntValue(String Tag, int value) {
        prefsEditor.putInt(Tag, value);
        prefsEditor.apply();
    }

    public int getIntValue(String Tag) {
        return myPrefs.getInt(Tag, 0);
    }

    public void setLongValue(String Tag, long value) {
        prefsEditor.putLong(Tag, value);
        prefsEditor.apply();
    }

    public long getLongValue(String Tag) {
        return myPrefs.getLong(Tag, 0);
    }


    public void setValue(String Tag, String token) {
        prefsEditor.putString(Tag, token);
        prefsEditor.commit();
    }



    public String getValue(String Tag) {
        if (Tag.equalsIgnoreCase(GlobalVariables.LATITUDE))
            return myPrefs.getString(Tag, "22.7497853");
        else if (Tag.equalsIgnoreCase(GlobalVariables.LONGITUDE))
            return myPrefs.getString(Tag, "75.8989044");
        return myPrefs.getString(Tag, "");
    }


    public boolean getBooleanValue(String Tag) {
        return myPrefs.getBoolean(Tag, false);

    }

    public static void setBooleanValue(String Tag, boolean token) {
        prefsEditor.putBoolean(Tag, token);
        prefsEditor.commit();
    }

    public static void setVibration(Context context, Boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(	SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(VIBRATION, result);
        prefEditor.commit();
    }
    public static boolean getVibration(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(SETTING_Quiz_PREF, Context.MODE_PRIVATE);
            return prefs.getBoolean(VIBRATION, DEFAULT_VIBRATION_SETTING);
        }catch (Exception e){
            e.printStackTrace();
        }
        return DEFAULT_VIBRATION_SETTING;
    }
    public static void setSoundEnableDisable(Context context, Boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(SOUND_ONOFF, result);
        prefEditor.commit();
    }
    public static boolean getSoundEnableDisable(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(SETTING_Quiz_PREF, Context.MODE_PRIVATE);
            return prefs.getBoolean(SOUND_ONOFF, DEFAULT_SOUND_SETTING);
        }catch (Exception e){
            e.printStackTrace();
        }
        return DEFAULT_SOUND_SETTING;
    }
    public static void setMusicEnableDisable(Context context, Boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(SHOW_MUSIC_ONOFF, result);
        prefEditor.commit();
    }

    public static boolean getMusicEnableDisable(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        return prefs.getBoolean(SHOW_MUSIC_ONOFF,
                DEFAULT_MUSIC_SETTING);
    }

    public static void setLan(Context context, Boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(LANG, result);
        prefEditor.commit();
    }


    public static boolean getLan(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        return prefs.getBoolean(LANG,
                DEFAULT_LAN_SETTING);
    }

    public static void setScore(Context context, int totalScore) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(TOTAL_SCORE, totalScore);
        prefEditor.commit();
    }


    public static int getScore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        return prefs.getInt(TOTAL_SCORE, 0);
    }
    public static void setPoint(Context context, int point) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(POINT, point);
        prefEditor.commit();
    }


    public static int getPoint(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        return prefs.getInt(POINT, 6);

    }

    public static void setNoCompletedLevel(Context context, int completedLevel) {
        SharedPreferences prefs = context.getSharedPreferences(SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        //prefEditor.putInt(LEVEL_COMPLETED, completedLevel);

        //
        int level =  prefs.getInt(LEVEL_COMPLETED_ACHIVEMENT,1);
        if(level < completedLevel)
            prefEditor.putInt(LEVEL_COMPLETED_ACHIVEMENT,completedLevel);
        else
            prefEditor.putInt(LEVEL_COMPLETED_ACHIVEMENT,level);

        //

        prefEditor.commit();
    }


    public static int getNoCompletedLevel(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        return prefs.getInt(GlobalVariables.SelectedSubCategoryID, 1);

    }

    public static void setRightAns(Context context, int NoofrightAns) {
        SharedPreferences prefs = context.getSharedPreferences(SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_RIGHT_ANSWARE_QUESTIONS, NoofrightAns);
        prefEditor.commit();
    }

    public static int getRightAns(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        return prefs.getInt(COUNT_RIGHT_ANSWARE_QUESTIONS, 1);
    }

    public static void setCountQuestionCompleted(Context context, int countquetioncompleted) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_QUESTION_COMPLETED, countquetioncompleted);
        prefEditor.commit();
    }

    public static int getCountQuestionCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                SETTING_Quiz_PREF, Context.MODE_PRIVATE);
        return prefs.getInt(COUNT_QUESTION_COMPLETED, 1);
    }

    public static boolean getBoolean(String key, Context context) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, true);
    }
    // /getPWDFromSP()

    public static void setBoolean(String key, boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void saveArrayList(List<QuizCategoryList> list, String key, Context context){
        Gson gson = new Gson();
        String hashMapString = gson.toJson(list);
        Log.d("KINGSN", "setParentUser2: "+hashMapString);
        prefsEditor.putString(key, hashMapString);
        prefsEditor.apply();


    }


   /* public void setParentUser2(UserDTO userDTO, String tag) {

        Gson gson = new Gson();
        String hashMapString = gson.toJson(userDTO);
        Log.d("KINGSN", "setParentUser2: "+hashMapString);
        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public UserDTO getParentUser2(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new UserDTO();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<UserDTO>() {
            }.getType();
            return gson.fromJson(storedHashMapString, type);
        }
    }

    public void setSettings(Settings settings, String tag) {

        Gson gson = new Gson();
        String hashMapString = gson.toJson(settings);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public Settings getSettings(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new Settings();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<Settings>() {
            }.getType();
            return gson.fromJson(storedHashMapString, type);
        }
    }*/

   /* public UserDTO getParentUser2(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        assert obj != null;
        if (obj.equals("defValue")) {
            return new UserDTO();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<UserDTO>() {
            }.getType();
            return gson.fromJson(storedHashMapString, type);
        }
    }
*/



}
