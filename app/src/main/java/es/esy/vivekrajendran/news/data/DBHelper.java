package es.esy.vivekrajendran.news.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, NewsContract.DB_NAME, null, NewsContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NewsContract.News.CREATE_TABLE);
        db.execSQL(NewsContract.Video.CREATE_TABLE);
        db.execSQL(NewsContract.Images.CREATE_TABLE);
        db.execSQL(NewsContract.Provider.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(NewsContract.News.DROP_TABLE);
        db.execSQL(NewsContract.Video.DROP_TABLE);
        db.execSQL(NewsContract.Images.DROP_TABLE);
        db.execSQL(NewsContract.Provider.DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
