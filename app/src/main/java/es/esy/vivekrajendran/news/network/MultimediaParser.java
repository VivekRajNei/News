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


class MultimediaParser {
    static class ImageParser {
        private Context context;
        public static final String TAG = "TAG";


        ImageParser(Context context) {
            this.context = context;
        }

        Boolean resolveImage(String json) {
            try {
                JSONObject rootElement = new JSONObject(json);
                JSONArray sources = rootElement.getJSONArray("hits");
                JSONObject itemObject;

                Log.i(TAG, "resolveImage: " + sources.length());
                for (int i = 0; i < sources.length(); i++) {
                    itemObject = sources.getJSONObject(i);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.Images.COLUMN_ID, itemObject.getInt("id"));
                    contentValues.put(NewsContract.Images.COLUMN_LIKES, String.valueOf(itemObject.getLong("likes")));
                    contentValues.put(NewsContract.Images.COLUMN_VIEWS, String.valueOf(itemObject.getLong("views")));
                    contentValues.put(NewsContract.Images.COLUMN_URL, itemObject.getString("webformatURL"));
                    storeOnDB(NewsContract.Images.CONTENT_URI, contentValues);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "resolveImage: " + e.getMessage());
                return false;
            }
        }

        private void storeOnDB(Uri mUri, ContentValues contentValues) {
            ContentResolver mContentResolver = context.getContentResolver();
            mContentResolver.insert(mUri, contentValues);
        }
    }

    static class VideoParser {
        private Context context;
        public static final String TAG = "TAG";


        VideoParser(Context context) {
            this.context = context;
        }

        Boolean resolveVideo(String json) {
            try {
                JSONObject rootElement = new JSONObject(json);
                JSONArray sources = rootElement.getJSONArray("hits");
                JSONObject itemObject;
                JSONObject videos;
                JSONObject medium;

                Log.i(TAG, "resolveVideo: " + sources.length());
                for (int i = 0; i < sources.length(); i++) {
                    itemObject = sources.getJSONObject(i);
                    videos = itemObject.getJSONObject("videos");
                    medium = videos.getJSONObject("medium");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.Video.COLUMN_ID, itemObject.getInt("id"));
                    contentValues.put(NewsContract.Video.COLUMN_VIEWS, itemObject.getInt("views"));
                    contentValues.put(NewsContract.Video.COLUMN_LIKES, itemObject.getInt("likes"));
                    contentValues.put(NewsContract.Video.COLUMN_URL, medium.getString("url"));
                    storeOnDB(NewsContract.Video.CONTENT_URI, contentValues);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "resolveVideo: " + e.getMessage());
                return false;
            }
        }


        private void storeOnDB(Uri mUri, ContentValues contentValues) {
            ContentResolver mContentResolver = context.getContentResolver();
            mContentResolver.insert(mUri, contentValues);
        }
    }
}
