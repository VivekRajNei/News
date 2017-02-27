package es.esy.vivekrajendran.news.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import es.esy.vivekrajendran.news.R;


public class VideoCursorAdapter extends CursorAdapter {

    private Activity activity;

    public VideoCursorAdapter(Activity activity, Cursor c) {
        super(activity.getApplicationContext(), c, 0);
        this.activity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

}
