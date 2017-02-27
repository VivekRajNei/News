package es.esy.vivekrajendran.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

//    private FirebaseAuth mFirebaseAuth;
//    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

//        mFirebaseAuth = FirebaseAuth.getInstance();
//        initListener();
    }

//    private void initListener() {
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser mFireUser = firebaseAuth.getCurrentUser();
//                if (mFireUser != null) {
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    intent.putExtra("existing", true);
//                    startActivity(intent);
//                    SplashActivity.this.finish();
//                } else {
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setProviders(
//                                    AuthUI.EMAIL_PROVIDER,
//                                    AuthUI.GOOGLE_PROVIDER)
//                            .setIsSmartLockEnabled(false)
//                            .build(),
//                            RC_SIGN_IN);
//                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    SplashActivity.this.finish();
//                }
//            }
//        };
//    }

    @Override
    protected void onStart() {
        super.onStart();
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int RC_SIGN_IN = 102;
        if (requestCode == RC_SIGN_IN) {
            Intent intent;
            if (resultCode == RESULT_OK) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("existing", true);
            } else if (resultCode == RESULT_FIRST_USER) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("existing", false);
            } else if (resultCode == RESULT_CANCELED) {
                SplashActivity.this.finish();
            }
        }
    }
}