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

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.esy.vivekrajendran.news.data.NewsContract;
import es.esy.vivekrajendran.news.dialogs.DevDialog;
import es.esy.vivekrajendran.news.fragments.FavouritesFragment;
import es.esy.vivekrajendran.news.fragments.LatestNewsFragment;
import es.esy.vivekrajendran.news.fragments.ProviderFragment;


/**
 * This activity is the entry point after user is logged in.
 * It also includes NavigationView and BottomNavigationView.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

     //private field for FirebaseAuth instance for firebase reference
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private NavigationView navigationView;
    private SearchView searchView;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        initListener();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//            searchView = (SearchView) findViewById(R.id.sv_main);
//        searchView.setOnQueryTextListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new LatestNewsFragment())
                .commit();

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                changeFragment(item.getItemId());
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * This method will be called whenever overflow options menu item is clicked.
     * @param item represents clicked overflow item.
     * @return always return true.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_gallery:
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_developer:
                //getting fragmenttransaction object then show Dialog fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fm_dev = fragmentManager.beginTransaction();
                DevDialog dev = new DevDialog();
                dev.setContext(getApplicationContext());
                //dev.show(fm_dev, "dev");
                break;
            case R.id.nav_share:
                //initializing sharecompat to share the app
                ShareCompat.IntentBuilder.from(MainActivity.this)
                        .createChooserIntent()
                        .setData(Uri.parse("mailto:"));
                break;
            case R.id.nav_feedback:
                //initializing intent to share feedback
                Intent feedbackIntent = new Intent();
                feedbackIntent.setAction(Intent.ACTION_SENDTO);
                feedbackIntent.setData(Uri.parse("mailto:vivekrajendrn@gmail.com"));
                startActivity(Intent.createChooser(feedbackIntent, "Send feedback with"));
                break;
            case R.id.nav_logout:
                //sinning out current user
                mFirebaseAuth.signOut();
                break;
        }

        //Initializing drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * mehod to initialize the firebaseauth and authstatelistener object
     */
    private void initListener() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser == null) {
                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                    intent.putExtra("show_image", true);
                    startActivity(intent);
                } else {
                    //Setting up user profile in drawer layout
                    View header = navigationView.getHeaderView(0);
                    final ImageView profPic = (ImageView) header.findViewById(R.id.header_imageView);
                    TextView name = (TextView) header.findViewById(R.id.header_name);
                    TextView email = (TextView) header.findViewById(R.id.header_email);
                    Glide.with(getApplicationContext())
                            .load(mUser.getPhotoUrl())
                            .asBitmap()
                            .centerCrop()
                            .placeholder(R.drawable.ic_account_circle_black_24px)
                            .into(new BitmapImageViewTarget(profPic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    profPic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                    name.setText(mUser.getDisplayName());
                    email.setText(mUser.getEmail());
                }
            }
        };
    }

    /**
     * adding authstatelistener object when activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthStateListener != null) {
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
    }

    /**
     * removing authstatelistener object when activity stops
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    /**
     * A helper method to change fragments effortlessly
     * @param id param passed to specify fragment needs to be shown
     */
    private void changeFragment(int id) {
        Log.i("TAG", "changeFragment: " + id);
        switch (id) {
            case R.id.menu_btmnav_latest:
                Bundle bundle = new Bundle();
                bundle.putString("uri", NewsContract.News.CONTENT_URI.toString());
                bundle.putStringArray("projection", null);
                bundle.putString("selection", null);
                bundle.putStringArray("selectionArgs", null);
                bundle.putString("sortOrder", null);

                LatestNewsFragment latestNewsFragment = new LatestNewsFragment();
                latestNewsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, latestNewsFragment)
                        .commit();
                Log.i("TAG", "changeFragment: LatestNewsFragment");
                break;
            case R.id.menu_btmnav_provider:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new ProviderFragment())
                        .commit();
                break;
            case R.id.menu_btmnav_starred:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new FavouritesFragment())
                        .commit();
                break;
            default:
                throw new IllegalArgumentException("changeFragment: invalid id passed");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.setVisibility(View.VISIBLE);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSearch", true);
        bundle.putStringArray("query", new String[] {query});
        LatestNewsFragment latestNewsFragment = new LatestNewsFragment();
        latestNewsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, latestNewsFragment)
                .commit();
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        mBottomNavigationView.
        return false;
    }
}