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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class NewsProvider extends ContentProvider {

    public static final String TAG = "TAG";
    private static final int NEWS = 100;
    private static final int NEWS_ID = 101;
    private static final int NEWS_ID_FAV = 102;
    private static final int NEWS_ID_SEARCH = 103;
    private static final int PROVIDER = 200;
    private static final int PROVIDER_ID = 201;
    private static final int IMAGE = 300;
    private static final int IMAGE_ID = 301;
    private static final int VIDEO = 400;
    private static final int VIDEO_ID = 401;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS, NEWS);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS + "/#", NEWS_ID);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS + "/search", NEWS_ID_SEARCH);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_PROVIDER, PROVIDER);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_PROVIDER + "/#", PROVIDER_ID);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_IMAGE, IMAGE);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_IMAGE + "/#", IMAGE_ID);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_VIDEO, VIDEO);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_VIDEO + "/#", VIDEO_ID);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS +
                "/" + NewsContract.News.COLUMN_FAV + "/#", NEWS_ID_FAV);
    }

    private DBHelper mDBHelper;

    public NewsProvider() {
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase mReadableDB = mDBHelper.getReadableDatabase();
        Cursor cursor;
        Log.i(TAG, "query: " + uri.toString());
        int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                cursor = mReadableDB.rawQuery("SELECT * FROM " + NewsContract.News.TABLE_NAME
                        + " ORDER BY  rowid DESC", null);
                Log.i(TAG, "query: " + cursor.getCount());
                break;
            case NEWS_ID:
                selection = "rowid" + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = mReadableDB.query(NewsContract.News.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case NEWS_ID_FAV:
                selection = NewsContract.News.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                String[] projectionFav = new String[]{NewsContract.News.COLUMN_FAV};
                cursor = mReadableDB.query(NewsContract.News.TABLE_NAME, projectionFav, selection,
                        selectionArgs, null, null, null);
                break;
            case NEWS_ID_SEARCH:
                Log.i(TAG, "query: NEWS_ID_SEARCH");
                String query = "SELECT * FROM " + NewsContract.News.TABLE_NAME + " WHERE  "
                        + NewsContract.News.COLUMN_DESCRIPTION + ", "
                        + NewsContract.News.COLUMN_TITLE + " LIKE '%" + selection + "%'";
                cursor = mReadableDB.rawQuery(query, null);
                break;
            case PROVIDER:
                cursor = mReadableDB.query(NewsContract.Provider.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PROVIDER_ID:
                selection = NewsContract.Provider.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = mReadableDB.query(NewsContract.Provider.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case IMAGE:
                cursor = mReadableDB.query(NewsContract.Images.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case IMAGE_ID:
                selection = NewsContract.Images.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = mReadableDB.query(NewsContract.Images.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case VIDEO:
                cursor = mReadableDB.query(NewsContract.Video.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case VIDEO_ID:
                selection = NewsContract.Video.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = mReadableDB.query(NewsContract.Video.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase mWritableDB = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        long id;
        switch (match) {
            case NEWS:
                id = mWritableDB.insert(
                        NewsContract.News.TABLE_NAME,
                        null,
                        values);
                break;
            case PROVIDER:
                id = mWritableDB.insert(
                        NewsContract.Provider.TABLE_NAME,
                        null,
                        values);
                break;
            case IMAGE:
                id = mWritableDB.insert(
                        NewsContract.Images.TABLE_NAME,
                        null,
                        values);
                break;
            case VIDEO:
                id = mWritableDB.insert(
                        NewsContract.Video.TABLE_NAME,
                        null,
                        values);
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        if (id == -1) {
            Log.e("TAG", "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                rowsDeleted = database.delete(NewsContract.News.TABLE_NAME, selection, selectionArgs);
                break;
            case NEWS_ID:
                selection = NewsContract.News.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(NewsContract.News.TABLE_NAME, selection, selectionArgs);
                break;
            case PROVIDER:
                rowsDeleted = database.delete(NewsContract.Provider.TABLE_NAME, selection, selectionArgs);
                break;
            case PROVIDER_ID:
                selection = NewsContract.Provider.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(NewsContract.Provider.TABLE_NAME, selection, selectionArgs);
                break;
            case IMAGE:
                rowsDeleted = database.delete(NewsContract.Images.TABLE_NAME, selection, selectionArgs);
                break;
            case IMAGE_ID:
                selection = NewsContract.Images.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(NewsContract.Images.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEO:
                rowsDeleted = database.delete(NewsContract.Video.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEO_ID:
                selection = NewsContract.Video.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(NewsContract.Video.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        Log.i(TAG, "update: " + uri.toString() + " Match " + match);
        switch (match) {
            case NEWS:
                return updateData(uri, values, selection, selectionArgs);
            case NEWS_ID:
                selection = NewsContract.News.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateData(uri, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateData(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        int rowsUpdated = database.update(NewsContract.News.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
