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
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.esy.vivekrajendran.news.util.NetworkChecker;

/**
 * This activity provides registration functionality.
 * It makes use of firebase-auth to store user provided credentials
 */

public class RegisterActivity extends AppCompatActivity {

    //Creating private field to reference view from java code inorder to work with the views
    private EditText email;
    private EditText password;
    private EditText name;
    private ProgressDialog progressDialog;

    /**
     * onCreate method will be called when the acitvity initilizes for ther first time.
     * It also sets the content
     * Here we initilize the view fields with corresponding views
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.register));
        }

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cool_activity_register);
        coordinatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
            }
        });

        //Initializing views with with their corresponding views from activity_register.xml file.
        name = (EditText) findViewById(R.id.reg_name);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_pass);
        Button register = (Button) findViewById(R.id.reg_register);
        TextView return_to = (TextView) findViewById(R.id.reg_return);
        progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme);
        progressDialog.setMessage("Registering in");
        progressDialog.setIndeterminate(true);

        //setting up onclicklistener to listen return button pressed.
        return_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //setting up onclicklistener to listern click of register button.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkChecker.getInstance(getApplicationContext()).isNetworkAvailable()) {
                    Snackbar.make(coordinatorLayout, getString(R.string.network_unavailable), Snackbar.LENGTH_LONG)
                            .show();
                }

                if (verifyName() && verifyEmail() && verifyPassword()) {
                    progressDialog.show();
                    FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(
                                    name.getText().toString().trim(),
                                    password.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressDialog.cancel();
                                    Toast.makeText(RegisterActivity.this, "Registered Successfully " + authResult.getUser().getEmail(), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(RegisterActivity.this, "Failed to register " + e.getMessage()
                                            , Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Snackbar.make(coordinatorLayout, "Unable to register", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    /**
     * This method check validity of the given email and if it is valid it will give ture otherwise returns false.
     * @return true if email is valid otherwise false.
     */
    private boolean verifyEmail() {
        TextInputLayout emaillayout = (TextInputLayout) findViewById(R.id.reg_email_layout);
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
     * This method check strength of password.
     * @return true if given password is eight or more than eight charactors long otherwise returns false.
     */
    private boolean verifyPassword() {
        TextInputLayout passlay  = (TextInputLayout) findViewById(R.id.reg_pass_layout);
        String pass = password.getText().toString().trim();

        if (pass.length() >= 8){
            passlay.setError("");
            return true;
        } else {
            passlay.setError(getResources().getString(R.string.err_password));
            return false;
        }
    }

    /**
     * This method check strenth of given name.
     * @return true if given name is more than four charactors long otherwise returns false.
     */
    private boolean verifyName() {
        TextInputLayout namelay = (TextInputLayout) findViewById(R.id.reg_name_layout);
        String name_ref = name.getText().toString().trim();

        if (name_ref.length() > 4) {
            return true;
        } else {
            namelay.setError(getResources().getString(R.string.err_name)); return false;
        }
    }
}