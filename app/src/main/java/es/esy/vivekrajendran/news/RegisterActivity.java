package es.esy.vivekrajendran.news;

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
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.register));
        }

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_register);
        coordinatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
            }
        });

        name = (EditText) findViewById(R.id.reg_name);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_pass);
        Button register = (Button) findViewById(R.id.reg_register);
        TextView return_to = (TextView) findViewById(R.id.reg_return);

        return_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyName() && verifyEmail() && verifyPassword()) {
//                    FirebaseAuth.getInstance()
//                            .createUserWithEmailAndPassword(
//                                    name.getText().toString().trim(),
//                                    password.getText().toString().trim())
//                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                                @Override
//                                public void onSuccess(AuthResult authResult) {
//                                    Toast.makeText(RegisterActivity.this, "Registered Successfully " + authResult.getUser().getEmail(), Toast.LENGTH_SHORT).show();
//                                    onBackPressed();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(RegisterActivity.this, "Failed to register " + e.getMessage()
//                                            , Toast.LENGTH_SHORT).show();
//                                }
//                            });
                } else {
                    Snackbar.make(coordinatorLayout, "Unable to register", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

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