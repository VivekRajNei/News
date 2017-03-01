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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.esy.vivekrajendran.news.util.NetworkChecker;

/**
 * This activity provides Login functionality by making use of firebase-auth.
 * It also have signin with google functionality
 */

public class LoginActivity extends AppCompatActivity {

    //Creating private field to bind view and java code
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;

    /**
     * onCreate will be called when the activity created for the first time
     * here in this methos we initialize ther private fields
     * @param savedInstanceState
     */
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
        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme);
        progressDialog.setMessage("Signning in");
        progressDialog.setIndeterminate(true);

        //setting up click event listener to listen for click of register button
        //when click event occur it will take to the register activity
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);

        //setting up click event listener to listen for click of login button
        //when login button is clicked it checks validity of email and password then check against firebase
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!NetworkChecker.getInstance(getApplicationContext()).isNetworkAvailable()) {
                    RelativeLayout mParentView = (RelativeLayout) findViewById(R.id.rl_activity_login_parent);
                    Snackbar.make(mParentView, getString(R.string.network_unavailable), Snackbar.LENGTH_LONG)
                            .show();
                }
                progressDialog.show();
                if (!verifyEmail() && verifyPassword()){
                    progressDialog.cancel();
                    return;
                }
                //setting up firebase instance to check against firebase auth provider
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email.getText().toString().trim(),
                                password.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.cancel();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(LoginActivity.this, "Login Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //setting up click listener to hide keyboard when user touches outside to edittext
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(constraintLayout.getWindowToken(), 0);
            }
        });
    }

    /**
     * this method verifies validity of user given email and sets error if given email is invalid
     * @return false if email is valid otherwise true
     */
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

    /**
     * this methos verifies validity of password and sets error if given password invalid
     * @return true if given password is more than 1 character long else false
     */
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