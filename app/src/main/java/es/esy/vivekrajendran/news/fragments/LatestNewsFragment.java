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

package es.esy.vivekrajendran.news.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import es.esy.vivekrajendran.news.R;
import es.esy.vivekrajendran.news.data.DBHelper;
import es.esy.vivekrajendran.news.data.NewsContract;
import es.esy.vivekrajendran.news.data.UserPref;
import es.esy.vivekrajendran.news.network.NewsAsync;
import es.esy.vivekrajendran.news.util.NetworkChecker;
import es.esy.vivekrajendran.news.util.NewsCursorAdapter;
import es.esy.vivekrajendran.news.util.SearchListener;

public class LatestNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

    private static final int LATEST_NEWS_LOADER = 452;
    private static final int LATEST_NEWS_SEARCH_LOADER = 751;
    private NewsCursorAdapter newsCursorAdapter;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
            Log.i("TAG", "onViewCreated: LatestNewsFragment loading finished");
            ListView latestNewsListView = (ListView) view.findViewById(R.id.lv_frag_latest);
            View emptyView = view.findViewById(R.id.empty_view);
//            getData();
            latestNewsListView.setEmptyView(emptyView);
            newsCursorAdapter = new NewsCursorAdapter(getActivity(), null);
            latestNewsListView.setAdapter(newsCursorAdapter);
            getLoaderManager().initLoader(LATEST_NEWS_LOADER, null, LatestNewsFragment.this);
        } catch (Exception e) {
            Log.i("TAG", "onViewCreated: LatestNewsFragment "+ e.getMessage());
        }
    }

    public void getData() {
        String url = "https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=a65e2431ef9141ab93e78509b14554d0";

        if (NetworkChecker.getInstance(getContext()).isNetworkAvailable()
                && UserPref.getInstance(getContext()).isImageFetchable()) {
            new NewsAsync(getContext())
                    .execute(url, NewsAsync.NEWS);
            Log.i("TAG", "onViewCreated: Async Called" + System.currentTimeMillis());

        } else {
            Log.i("TAG", "LatestNewsFragment getData: Network unavailable: " + "Time " + System.currentTimeMillis()
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String queryString = "SELECT * FROM " + NewsContract.News.TABLE_NAME + " WHERE  "
                + NewsContract.News.COLUMN_DESCRIPTION + " LIKE '%" + newText + "%'";

        Cursor cursor = new DBHelper(getContext()).getReadableDatabase().rawQuery(queryString, null);
        onLoadFinished(new Loader<Cursor>(getContext()), cursor);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });

        searchView.setOnCloseListener( new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getLoaderManager().initLoader(LATEST_NEWS_LOADER, null, LatestNewsFragment.this);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search) {

        }
        return true;
    }
}
