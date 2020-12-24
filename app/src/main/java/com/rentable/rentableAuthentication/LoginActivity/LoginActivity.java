package com.rentable.rentableAuthentication.LoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rentable.R;
import com.rentable.SessionContext;
import com.rentable.HomeScreen.HomeScreenNavigation.NavigationActivity;
import com.rentable.rentableAuthentication.SignupActivity.SignupActivity;

public class LoginActivity extends AppCompatActivity {
    final static String TAG = LoginActivity.class.getName();
    EditText mpassword,memail;
    TextView mregisterlink;
    Button mloginbtn;
    FirebaseAuth fireAuth;
    SharePrefenceHelper mSharePrefenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mSharePrefenceHelper = SharePrefenceHelper.getInstance(getApplicationContext());
        if (!mSharePrefenceHelper.isUserLogedOut()) {
            // Already Logged In

            SessionContext.setEmail(mSharePrefenceHelper.getEmail());
            SessionContext.userData();
            Log.e(TAG, "onCreate: got " + SessionContext.getEmail());
            startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
        }

        memail = findViewById(R.id.Email);
        mpassword = findViewById(R.id.Password);
        mregisterlink = findViewById(R.id.Register_link);
        mloginbtn = findViewById(R.id.Login);
        fireAuth = FirebaseAuth.getInstance();



        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = memail.getText().toString().trim();
                final String password = mpassword.getText().toString().trim();
                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter Login Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                    //memail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                    //mpassword.setError("Password is required");
                    return;
                }
                fireAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            /*
                             *Todo: Change to phone Number and email passing once login UI is modified.
                             */
                            mSharePrefenceHelper.saveLoginDeatils(email, password);
                            //DataBaseTesting.testdatabase(); // REMOVE AFTER TESTING
                            SessionContext.setEmail(email);
                            SessionContext.userData();
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        mregisterlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
    }
}