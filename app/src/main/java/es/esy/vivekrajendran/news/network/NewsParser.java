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

package es.esy.vivekrajendran.news.network;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.esy.vivekrajendran.news.data.NewsContract;

class NewsParser {

    static class LatestNewsParser {
        private Context context;
        public static final String TAG = "TAG";


        LatestNewsParser(Context context) {
            this.context = context;
        }

        Boolean resolveJSON(String json) {
            try {
                JSONObject rootElement = new JSONObject(json);
                //if (!("ok".equals(rootElement.getString("status")))) {   }
                JSONArray sources = rootElement.getJSONArray("articles");
                JSONObject itemObject;

                Log.i(TAG, "resolveJSON: " + sources.length());
                for (int i = 0; i < sources.length(); i++) {
                    itemObject = sources.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.News.COLUMN_AUTHOR, itemObject.getString("author"));
                    contentValues.put(NewsContract.News.COLUMN_TITLE, itemObject.getString("title"));
                    contentValues.put(NewsContract.News.COLUMN_DESCRIPTION, itemObject.getString("description"));
                    contentValues.put(NewsContract.News.COLUMN_URL, itemObject.getString("url"));
                    contentValues.put(NewsContract.News.COLUMN_URL_TO_IMAGE, itemObject.getString("urlToImage"));
                    contentValues.put(NewsContract.News.COLUMN_PUBLISHED_AT, itemObject.getString("publishedAt"));
                    storeOnDB(NewsContract.News.CONTENT_URI, contentValues);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "resolveJSON: " + e.getMessage());
                return false;
            }
        }


        private void storeOnDB(Uri mUri, ContentValues contentValues) {
            ContentResolver mContentResolver = context.getContentResolver();
            mContentResolver.insert(mUri, contentValues);
        }
    }

    static class ProviderParser {
        private Context context;
        private static final String TAG = "TAG";


        ProviderParser(Context context) {
            this.context = context;
        }

        Boolean resolveJSON(String json) {
            try {
                JSONObject rootElement = new JSONObject(json);
                JSONArray sources = rootElement.getJSONArray("sources");
                JSONObject itemObject;

                Log.i(TAG, "resolveJSON: " + sources.length());
                for (int i = 0; i < sources.length(); i++) {
                    itemObject = sources.getJSONObject(i);
                    JSONObject urlImg = itemObject.getJSONObject("urlsToLogos");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.Provider.COLUMN_PROVIDER_ID, itemObject.getString("id"));
                    contentValues.put(NewsContract.Provider.COLUMN_NAME, itemObject.getString("name"));
                    contentValues.put(NewsContract.Provider.COLUMN_DESCRIPTION, itemObject.getString("description"));
                    contentValues.put(NewsContract.Provider.COLUMN_URL, itemObject.getString("url"));
                    contentValues.put(NewsContract.Provider.COLUMN_URL_TO_IMAGE, urlImg.getString("small"));
                    storeOnDB(NewsContract.Provider.CONTENT_URI, contentValues);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "resolveJSON: " + e.getMessage());
                return false;
            }
        }


        private void storeOnDB(Uri mUri, ContentValues contentValues) {
            ContentResolver mContentResolver = context.getContentResolver();
            mContentResolver.insert(mUri, contentValues);
        }
    }
}
