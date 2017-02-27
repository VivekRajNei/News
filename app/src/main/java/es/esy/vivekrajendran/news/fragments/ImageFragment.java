package es.esy.vivekrajendran.news.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import es.esy.vivekrajendran.news.R;
import es.esy.vivekrajendran.news.data.NewsContract;
import es.esy.vivekrajendran.news.data.UserPref;
import es.esy.vivekrajendran.news.network.NewsAsync;
import es.esy.vivekrajendran.news.util.NetworkChecker;

public class ImageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int IMAGE_LOADER = 10;
    private Adapter imageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_images, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gridView = (GridView) view.findViewById(R.id.lv_frag_images);
        imageAdapter = new Adapter(getActivity(), null);
        gridView.setNumColumns(2);
        gridView.setAdapter(imageAdapter);
        String url = "https://pixabay.com/api/?key=4654053-f29f39f63a9a301a1ec7dae0d&q=nature";
        getData(url);
        getLoaderManager().initLoader(IMAGE_LOADER, null, this);
    }


    public void getData(String url) {
        if (NetworkChecker.getInstance(getContext()).isNetworkAvailable()
                && UserPref.getInstance(getContext()).isImageFetchable()) {
            new NewsAsync(getContext())
                    .execute(url, NewsAsync.IMAGES);
        } else {
            Log.i("TAG", "getData: Network unavailable: "
                    + "Fetchable " + UserPref.getInstance(getContext()).isImageFetchable()
                    + ": NetworkStatus " + NetworkChecker.getInstance(getContext()).isNetworkAvailable());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                NewsContract.Images.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imageAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imageAdapter.swapCursor(null);
    }

    private static class Adapter extends CursorAdapter {

        Adapter(Activity activity, Cursor c) {
            super(activity.getApplicationContext(), c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_item_image);

            int columnURL = cursor.getColumnIndexOrThrow(NewsContract.Images.COLUMN_URL);
            int columnLikes = cursor.getColumnIndexOrThrow(NewsContract.Images.COLUMN_LIKES);
            int columnViews = cursor.getColumnIndexOrThrow(NewsContract.Images.COLUMN_VIEWS);

            Glide.with(context).load(cursor.getString(columnURL))
                    .error(R.drawable.ic_google_circle)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.drawable.ic_account_circle_black_24px)
                    .into(imageView);
            //textView.setText(cursor.getString(columnURL));
        }
    }
}
