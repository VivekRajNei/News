package es.esy.vivekrajendran.news;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.login));
        }

        email = (EditText) findViewById(R.id.et_email_login);
        password = (EditText) findViewById(R.id.et_password_login);
        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.btn_register_login);
        mProgressbar = (ProgressBar) findViewById(R.id.pgbar_login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressbar.setVisibility(View.VISIBLE);
                if (!verifyEmail() && verifyPassword()){
                    mProgressbar.setVisibility(View.GONE);
                    return;
                }

//                FirebaseAuth.getInstance()
//                        .signInWithEmailAndPassword(email.getText().toString().trim(),
//                                password.getText().toString().trim())
//                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                            @Override
//                            public void onSuccess(AuthResult authResult) {
//                                mProgressbar.setVisibility(View.GONE);
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                LoginActivity.this.finish();
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                mProgressbar.setVisibility(View.GONE);
//                                Toast.makeText(LoginActivity.this, "Login Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
            }
        });
    }



    private boolean verifyEmail() {
        TextInputLayout emaillayout  = (TextInputLayout) findViewById(R.id.emailInputLayout_email_login);
        String emailText = email.getText().toString().trim();
        String regEx =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Matcher matcher = Pattern.compile(regEx).matcher(emailText);

        if (matcher.matches()) {
            emaillayout.setError("");
            return true;
        } else {
            email.setError(getResources().getString(R.string.err_email));
            emaillayout.setError(getResources().getString(R.string.err_email));
            return false;
        }
    }

    private boolean verifyPassword() {
        TextInputLayout passlay  = (TextInputLayout) findViewById(R.id.passwordInputLayout_password_login);
        String pass = password.getText().toString().trim();

        if (pass.length() >= 0){
            passlay.setError("");
            return true;
        } else {
            passlay.setError(getResources().getString(R.string.err_pass_man));
            return false;
        }
    }
}