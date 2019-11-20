package baikal.web.footballapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import baikal.web.footballapp.model.User;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static baikal.web.footballapp.PreferencesUtility.LOGGED_IN_PREF;

public class SaveSharedPreference {
    @SerializedName("person")
    @Expose
    public static User user;
    public static String id;
    public  static  final String ID = "ID";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    
    private static SharedPreferences getPreferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set the Login Status
     * @param context
     * @param loggedIn
     */
    public static void setLoggedIn(Context context, boolean loggedIn) {
//        editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }


    public static User getObject(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MyObject", "");
        return gson.fromJson(json, User.class);
    }

    public static void saveObject(User myobject){
        Gson gson = new Gson();
        String json = gson.toJson(myobject);
        editor.putString("MyObject", json);
        editor.apply();
    }
    public static void editObject(User myobject){
        Gson gson = new Gson();
        String json = gson.toJson(myobject);
        editor.putString("MyObject", json);
        editor.apply();
    }
}
