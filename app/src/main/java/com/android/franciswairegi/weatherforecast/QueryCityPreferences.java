package com.android.franciswairegi.weatherforecast;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryCityPreferences {

    private static final String PREF_SEARCH_QUERY = "searchCityQuery";
    private static final String PREF_CITY_ID = "city_id";

    public static String getStoredQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, "");
    }

    public static void setStoredQuery(Context context, String query){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }

    public static String getStoredCityId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_CITY_ID, "");
    }

    public static void setStoredCityId(Context context, String cityId){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CITY_ID, cityId)
                .apply();
    }
}
