package com.example.fyp.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {
    public static final String EMERGENCY_NAME = "name";
    public static final String EMERGENCY_NUMBER = "number";
    public static final String PREF_NAME = "LocationPreferenceFile";


    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public PrefsHelper(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public String getEmergencyContactName(){
        return pref.getString(EMERGENCY_NAME,null);
    }

    public void setEmergencyContactName(String name){
        editor.putString(EMERGENCY_NAME,name);
        editor.apply();
    }

    public String getEmergencyContactNumber(){
        return pref.getString(EMERGENCY_NUMBER,null);
    }

    public void setEmergencyContactNumber(String number){
        editor.putString(EMERGENCY_NUMBER,number);
        editor.apply();
    }




}
