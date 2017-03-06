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

public class ProviderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PROVIDER_LOADER = 142;
    private ProviderAdapter providerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_providers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = "https://newsapi.org/v1/sources?apikey=6e661062a47d4eac83dc8a7ee0dcc96b";
//        getData(url);
        providerAdapter = new ProviderAdapter(getContext(), null);
        GridView gridView = (GridView) view.findViewById(R.id.gv_providers_fragment);
        gridView.setNumColumns(2);
        gridView.setAdapter(providerAdapter);

        getLoaderManager().initLoader(PROVIDER_LOADER, null, this);

    }

    public void getData(String url) {
        if (NetworkChecker.getInstance(getContext()).isNetworkAvailable()
                && UserPref.getInstance(getContext()).isImageFetchable()) {
            new NewsAsync(getContext())
                    .execute(url, NewsAsync.PROVIDERS);
        } else {
            Log.i("TAG", "getData: Network unavailable: "
                   + "Fetchable " + UserPref.getInstance(getContext()).isProvidersFetchable()
            + ": NetworkStatus " + NetworkChecker.getInstance(getContext()).isNetworkAvailable());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                NewsContract.Provider.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        providerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        providerAdapter.swapCursor(null);
    }

    private class ProviderAdapter extends CursorAdapter {

        ProviderAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_provider, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView imageView = (ImageView) view.findViewById(R.id.img_item_provider_image);
            TextView title = (TextView) view.findViewById(R.id.tv_item_provider_name);

            int columnImage = cursor.getColumnIndexOrThrow(NewsContract.Provider.COLUMN_URL_TO_IMAGE);
            int columnName = cursor.getColumnIndexOrThrow(NewsContract.Provider.COLUMN_NAME);

            Glide.with(context)
                    .load(cursor.getString(columnImage))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
            title.setText(cursor.getString(columnName));
        }
    }
}