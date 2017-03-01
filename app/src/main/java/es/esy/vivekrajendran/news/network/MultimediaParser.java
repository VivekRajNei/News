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
