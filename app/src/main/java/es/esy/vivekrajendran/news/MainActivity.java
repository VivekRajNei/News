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
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

import es.esy.vivekrajendran.news.dialogs.DevDialog;
import es.esy.vivekrajendran.news.fragments.ImageFragment;
import es.esy.vivekrajendran.news.fragments.LatestNewsFragment;
import es.esy.vivekrajendran.news.fragments.ProviderFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private FirebaseAuth mFirebaseAuth;
//    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
            setSupportActionBar(toolbar);

            //initListener();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new LatestNewsFragment())
                .commit();

        BottomNavigationView mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                changeFrag(item.getItemId());
                return true;
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_gallery:
                //new NewsAsync(getApplicationContext()).execute(" https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=a65e2431ef9141ab93e78509b14554d0");
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.nav_developer:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fm_dev = fragmentManager.beginTransaction();
                DevDialog dev = new DevDialog();
                dev.setContext(getApplicationContext());
                //dev.show(fm_dev, "dev");
                break;
            case R.id.nav_share:
//                ShareCompat.IntentBuilder.from(MainActivity.this)
//                        .createChooserIntent()
//                        .setData()
                break;
            case R.id.nav_feedback:
                Intent feedbackIntent = new Intent();
                feedbackIntent.setAction(Intent.ACTION_SENDTO);
                feedbackIntent.setData(Uri.parse("mailto:vivekrajendrn@gmail.com"));
                startActivity(Intent.createChooser(feedbackIntent, "Send feedback with"));
                break;
            case R.id.nav_logout:
//                mFirebaseAuth.signOut();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void initListener() {
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser mUser = firebaseAuth.getCurrentUser();
//                if (mUser == null) {
//                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
//                    intent.putExtra("show_image", true);
//                    startActivity(intent);
//                } else {
//                    View header = navigationView.getHeaderView(0);
//                    final ImageView profPic = (ImageView) header.findViewById(R.id.header_imageView);
//                    TextView name = (TextView) header.findViewById(R.id.header_name);
//                    TextView email = (TextView) header.findViewById(R.id.header_email);
//                    Glide.with(getApplicationContext())
//                            .load(mUser.getPhotoUrl())
//                            .asBitmap()
//                            .centerCrop()
//                            .placeholder(R.drawable.ic_account_circle_black_24px)
//                            .into(new BitmapImageViewTarget(profPic) {
//                                @Override
//                                protected void setResource(Bitmap resource) {
//                                    RoundedBitmapDrawable circularBitmapDrawable =
//                                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
//                                    circularBitmapDrawable.setCircular(true);
//                                    profPic.setImageDrawable(circularBitmapDrawable);
//                                }
//                            });
//                    name.setText(mUser.getDisplayName());
//                    email.setText(mUser.getEmail());
//                }
//            }
////        };
//    }

    @Override
    protected void onStart() {
        super.onStart();
        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void changeFrag(int id) {
        switch (id) {
            case R.id.menu_btmnav_latest:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new LatestNewsFragment())
                        .commit();
                break;
            case R.id.menu_btmnav_provider:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new ProviderFragment())
                        .commit();
                break;
            case R.id.menu_btmnav_starred:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new StarredFragment())
                        .commit();
                break;
        }

    }
}