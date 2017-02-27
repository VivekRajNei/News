package es.esy.vivekrajendran.news.data;

import android.content.ContentResolver;
import android.net.Uri;

public class NewsContract {

    static final String DB_NAME = "News.db";
    static final int DB_VERSION = 1;

    static final String PATH_NEWS = "news";
    static final String PATH_PROVIDER = "provider";
    static final String PATH_IMAGE = "image";
    static final String PATH_VIDEO = "video";

    static final String CONTENT_AUTHORITY = "es.esy.vivekrajendran.newsapi";
    private static final String CONTENT_PROVIDER = "content://" + CONTENT_AUTHORITY;
    private static final Uri URI_BASE = Uri.parse(CONTENT_PROVIDER);

    private NewsContract() {
    }

    public static final class News {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(URI_BASE, PATH_NEWS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS + "/#";

        public static final String TABLE_NAME = "news";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL_TO_IMAGE = "urlToImage";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_PUBLISHED_AT = "publishedAt";
        public static final String COLUMN_FAV = "favourite";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_URL_TO_IMAGE + " TEXT, " +
                COLUMN_PUBLISHED_AT + " TEXT," +
                COLUMN_FAV + " INTEGER);";

        static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class Provider {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(URI_BASE, PATH_PROVIDER);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROVIDER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROVIDER + "/#";
        public static final String TABLE_NAME = "provider";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_PROVIDER_ID = "providerId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_URL_TO_IMAGE = "urlsToLogos";
        public static final String COLUMN_FAV = "favourite";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PROVIDER_ID + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_URL_TO_IMAGE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_FAV + " INTEGER);";

        static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class Images {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(URI_BASE, PATH_IMAGE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IMAGE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IMAGE + "/#";
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_VIEWS = "views";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER, " +
                COLUMN_LIKES + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_VIEWS + " TEXT); ";

        static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class Video {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(URI_BASE, PATH_VIDEO);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO + "/#";

        public static final String TABLE_NAME = "video";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_VIEWS = "views";
        public static final String COLUMN_URL = "url";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER, " +
                COLUMN_LIKES + " INTEGER, " +
                COLUMN_URL + " INTEGER, " +
                COLUMN_VIEWS + " TEXT NOT NULL );";

        static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
