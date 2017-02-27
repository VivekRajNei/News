package es.esy.vivekrajendran.news.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

public class UserPref {

    private SharedPreferences jsonSharedPreferences;
    private SharedPreferences dbSharedPrefernces;

    private UserPref(Context context) {
        dbSharedPrefernces = context.getSharedPreferences(PrefContract.DbTime.PREF_NAME, Context.MODE_PRIVATE);
        jsonSharedPreferences = context.getSharedPreferences(PrefContract.Json.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dbSharedPrefernces.edit();
        editor.putLong(PrefContract.DbTime.NEWS, 0);
        editor.putLong(PrefContract.DbTime.PROVIDERS, 0);
        editor.putLong(PrefContract.DbTime.IMAGE, 0);
        editor.putLong(PrefContract.DbTime.VIDEO, 0);
        editor.apply();
    }

    public static UserPref getInstance(@NonNull Context context) {
        return new UserPref(context);
    }

    public boolean isJStringAvailable() {
        boolean availbilty = jsonSharedPreferences.getString(PrefContract.Json.JSTRING, null) != null;
        Log.i("TAG", "isJStringAvailable: " + availbilty);
        return availbilty;
    }

    public boolean isImageFetchable() {
        Log.i("TAG", "isProvidersFetchable: " + getCurrentTime() + " " +
                getImageTime() + ((getCurrentTime() - getImageTime()) > 43200));
        return ((getCurrentTime() - getImageTime()) < 43200);
    }

    public boolean isVideoFetchable() {
        return ((getCurrentTime() - getVideoTime()) < 43200);
    }

    public boolean isNewsFetchable() {
        return ((getCurrentTime() - getNewsTime()) < 43200);
    }

    public boolean isProvidersFetchable() {
        Log.i("TAG", "isProvidersFetchable: " + getCurrentTime() + " " +
                getProvidersTime() + ((getCurrentTime() - getProvidersTime()) > 43200));
        return ((getCurrentTime() - getProvidersTime()) < 43200);
    }

    private long getImageTime() {
        return jsonSharedPreferences.getLong(PrefContract.DbTime.IMAGE, getCurrentTime());
    }

    private long getVideoTime() {
        return jsonSharedPreferences.getLong(PrefContract.DbTime.VIDEO, getCurrentTime());
    }

    private long getNewsTime() {
        return jsonSharedPreferences.getLong(PrefContract.DbTime.NEWS, getCurrentTime());
    }

    private long getProvidersTime() {
        return jsonSharedPreferences.getLong(PrefContract.DbTime.PROVIDERS, getCurrentTime());
    }

    public void setImageTime() {
        SharedPreferences.Editor editor = dbSharedPrefernces.edit();
        editor.putLong(PrefContract.DbTime.IMAGE, getCurrentTime());
        editor.apply();
    }

    public void setNewsTime() {
        SharedPreferences.Editor editor = dbSharedPrefernces.edit();
        editor.putLong(PrefContract.DbTime.NEWS, getCurrentTime());
        editor.apply();
    }

    public void setProviderTime() {
        SharedPreferences.Editor editor = dbSharedPrefernces.edit();
        editor.putLong(PrefContract.DbTime.PROVIDERS, getCurrentTime());
        editor.apply();
    }

    public void setVideoTime() {
        SharedPreferences.Editor editor = dbSharedPrefernces.edit();
        editor.putLong(PrefContract.DbTime.VIDEO, getCurrentTime());
        editor.apply();
    }

    private long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }
}