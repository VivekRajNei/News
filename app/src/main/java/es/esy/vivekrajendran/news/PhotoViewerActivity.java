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

package es.esy.vivekrajendran.news;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import es.esy.vivekrajendran.news.data.NewsContract;
import es.esy.vivekrajendran.news.fragments.ImageViewerFragment;

public class PhotoViewerActivity extends AppCompatActivity {

    private static final int LATEST_PHOTO_LOADER = 474;
    private Cursor cursor;
    private ViewPager mViewPager;
    private TextView noOfPhotos;
    private Button closeButton;
    LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        mViewPager = (ViewPager) findViewById(R.id.vp_photoviewer);
        noOfPhotos = (TextView) findViewById(R.id.tv_photoviewer);
        closeButton = (Button) findViewById(R.id.btn_activity_photo_close);
        initLoader();
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private class PhotViewPagerAdapter extends FragmentPagerAdapter {

        PhotViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            if (position == -1) {
//                cursor.move(position + 1);
//            } else
                cursor.move(position+1);
            int columnURL = cursor.getColumnIndexOrThrow(NewsContract.Images.COLUMN_URL);
            Bundle bundle = new Bundle();
            bundle.putString("url", cursor.getString(columnURL));
            ImageViewerFragment imageViewerFragment = new ImageViewerFragment();
            imageViewerFragment.setArguments(bundle);
            noOfPhotos.setText(position + "/" + getCount());
            return imageViewerFragment;
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }
    }

    private void initLoader() {
        cursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(
                        getApplicationContext(),
                        NewsContract.Images.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                cursor = data;
                mViewPager.setAdapter(new PhotViewPagerAdapter(getSupportFragmentManager()));
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                cursor = null;
            }
        };

            getSupportLoaderManager().initLoader(LATEST_PHOTO_LOADER, null, cursorLoaderCallbacks);
    }
}