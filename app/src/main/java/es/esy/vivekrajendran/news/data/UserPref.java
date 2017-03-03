/*
* Copyright (c) <2017> <Vivek Rajendran>
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

package es.esy.vivekrajendran.news.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class UserPref {

    private SharedPreferences jsonSharedPreferences;
    private SharedPreferences dbSharedPrefernces;
    private int timeLapse = 43200;

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
        return jsonSharedPreferences.getString(PrefContract.Json.JSTRING, null) != null;
    }

    public boolean isImageFetchable() {
        return ((getCurrentTime() - getImageTime()) < timeLapse);
    }

    public boolean isVideoFetchable() {
        return ((getCurrentTime() - getVideoTime()) < timeLapse);
    }

    public boolean isNewsFetchable() {
        return ((getCurrentTime() - getNewsTime()) < timeLapse);
    }

    public boolean isProvidersFetchable() {
        return ((getCurrentTime() - getProvidersTime()) < timeLapse);
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