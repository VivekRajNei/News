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
