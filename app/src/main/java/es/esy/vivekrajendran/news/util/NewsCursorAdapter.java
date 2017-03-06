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

package es.esy.vivekrajendran.news.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import es.esy.vivekrajendran.news.R;
import es.esy.vivekrajendran.news.MoreNewsActivity;
import es.esy.vivekrajendran.news.data.NewsContract;

public class NewsCursorAdapter extends CursorAdapter {

    private Activity activity;
    private int position;

    public NewsCursorAdapter(Activity activity, Cursor c) {
        super(activity.getApplicationContext(), c, 0);
        this.activity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        try {
            return LayoutInflater.from(context).inflate(R.layout.item_news_heading, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
            return new TextView(context);
        }
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        if (cursor != null) {
            ImageView newsImage = (ImageView) view.findViewById(R.id.img_item_news_main);
            ImageView newsProviderImage = (ImageView) view.findViewById(R.id.img_item_news_provider);
            TextView newsHeadline = (TextView) view.findViewById(R.id.tv_item_news_headline);
            TextView newsProviderName = (TextView) view.findViewById(R.id.tv_item_news_providername);
            TextView newsPublished = (TextView) view.findViewById(R.id.tv_item_news_timepublished);
            final ImageView addToFav = (ImageView) view.findViewById(R.id.img_item_news_addtofav);
            ImageView share = (ImageView) view.findViewById(R.id.img_item_news_share);
            final ImageView optionsMenu = (ImageView) view.findViewById(R.id.img_item_news_optionsmenu);

            int columnNewsImage = cursor.getColumnIndexOrThrow(NewsContract.News.COLUMN_URL_TO_IMAGE);
            int columnHeadline = cursor.getColumnIndexOrThrow(NewsContract.News.COLUMN_TITLE);
            int columnProviderName = cursor.getColumnIndexOrThrow(NewsContract.News.COLUMN_AUTHOR);
            int columnNewsURL = cursor.getColumnIndexOrThrow(NewsContract.News.COLUMN_URL);
            int columnPublished = cursor.getColumnIndexOrThrow(NewsContract.News.COLUMN_URL);
            int columnDes = cursor.getColumnIndexOrThrow(NewsContract.News.COLUMN_DESCRIPTION);
            int columnFav = cursor.getColumnIndexOrThrow(NewsContract.News.COLUMN_FAV);

            final String newsImageUrl = cursor.getString(columnNewsImage);
            final String headline = cursor.getString(columnHeadline);
            final String newsUrl = cursor.getString(columnNewsURL);
            final String description = cursor.getString(columnDes);
            final String providerName = cursor.getString(columnProviderName);
            final String published = cursor.getString(columnPublished);
            final String fav = cursor.getString(columnFav);

            newsProviderImage.setImageResource(R.drawable.ic_account_circle_black_24px);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MoreNewsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("url", newsImageUrl);
                    intent.putExtra("title", headline);
                    intent.putExtra("urlNews", newsUrl);
                    intent.putExtra("description", description);
                    context.startActivity(intent);
                }
            });

            Glide.with(context)
                    .load(newsImageUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(newsImage);

            newsHeadline.setText(headline);
            newsProviderName.setText(providerName);
            optionsMenu.setImageResource(R.drawable.ic_more_vert_black_24dp);
            optionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPostion(cursor.getPosition());
                    showPopupMenu(optionsMenu);
                }
            });
            newsPublished.setText(published);

            if ("Y".equals(fav)) {
                addToFav.setImageResource(R.drawable.ic_turned_in_black_24dp);
            } else addToFav.setImageResource(R.drawable.ic_bookmark_border_black_24dp);

            addToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues contentValues = new ContentValues();
                    if ("Y".equals(fav)) {
                        addToFav.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                        contentValues.put(NewsContract.News.COLUMN_FAV, "N");
                    } else {
                        addToFav.setImageResource(R.drawable.ic_turned_in_black_24dp);
                        contentValues.put(NewsContract.News.COLUMN_FAV, "Y");
                    }

                    int i = context.getContentResolver().update(
                            Uri.withAppendedPath(NewsContract.News.CONTENT_URI, String.valueOf(position)),                            contentValues,
                            null,
                            null);
                    if(i > 0 ) {
                        Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to add " + i, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            share.setImageResource(R.drawable.ic_menu_share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, newsUrl);
                            sendIntent.setType("text/plain");
                            context.startActivity(sendIntent);
                        }
                    }).start();
                }
            });
        }
    }

    private void setPostion(int position) {
        this.position = position;
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_overflow, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_overflow_hide:
                    Uri deleteUri = Uri.withAppendedPath(NewsContract.News.CONTENT_URI, String.valueOf(position));
                    activity.getContentResolver().delete(deleteUri,
                            NewsContract.News.COLUMN_ID, new String[] {
                                    String.valueOf(position)});
                    Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
}
