package es.esy.vivekrajendran.news.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import es.esy.vivekrajendran.news.R;
import es.esy.vivekrajendran.news.data.NewsContract;
import es.esy.vivekrajendran.news.data.UserPref;
import es.esy.vivekrajendran.news.network.NewsAsync;
import es.esy.vivekrajendran.news.util.NetworkChecker;
import es.esy.vivekrajendran.news.util.NewsCursorAdapter;

public class LatestNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LATEST_NEWS_LOADER = 452;
    private NewsCursorAdapter newsCursorAdapter;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            return LayoutInflater.from(getContext()).inflate(R.layout.fragment_latest, container, false);
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading");

        try {
            ListView latestNewsListView = (ListView) view.findViewById(R.id.lv_frag_latest);
            View emptyView = view.findViewById(R.id.empty_view);

            String url = "https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=a65e2431ef9141ab93e78509b14554d0";
            getData(url);
            latestNewsListView.setEmptyView(emptyView);
            newsCursorAdapter = new NewsCursorAdapter(getActivity(), null);
            latestNewsListView.setAdapter(newsCursorAdapter);
            getLoaderManager().initLoader(LATEST_NEWS_LOADER, null, this);
            Log.i("TAG", "onViewCreated: All initialized");
        } catch (Exception e) {
            Log.i("TAG", "onViewCreated: LatestNewsFragment "+ e.getMessage());
        }
    }

    public void getData(String url) {
        if (NetworkChecker.getInstance(getContext()).isNetworkAvailable()
                && UserPref.getInstance(getContext()).isImageFetchable()) {
            new NewsAsync(getContext())
                    .execute(url, NewsAsync.NEWS);
            Log.i("TAG", "onViewCreated: Async Called");

        } else {
            Log.i("TAG", "LatestNewsFragment getData: Network unavailable: "
                    + "Fetchable " + UserPref.getInstance(getContext()).isNewsFetchable()
                    + ": NetworkStatus " + NetworkChecker.getInstance(getContext()).isNetworkAvailable());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressDialog.show();
        return new CursorLoader(
                getContext(),
                NewsContract.News.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressDialog.cancel();
        newsCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        newsCursorAdapter.swapCursor(null);
    }
}
