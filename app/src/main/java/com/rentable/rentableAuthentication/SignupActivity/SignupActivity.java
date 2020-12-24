package com.rentable.rentableAuthentication.SignupActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rentable.DataBaseHelper.DataBaseTesting;
import com.rentable.DataBaseHelper.IuserData;
import com.rentable.DataBaseHelper.userAddress;
import com.rentable.DataBaseHelper.userData;
import com.rentable.HomeScreen.HomeScreenNavigation.NavigationActivity;
import com.rentable.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rentable.SessionContext;
import com.rentable.rentableAuthentication.LoginActivity.LoginActivity;
import com.rentable.rentableAuthentication.LoginActivity.SharePrefenceHelper;

//import com.rentable.rentableAuthentication.SignupActivity.R;
public class SignupActivity extends AppCompatActivity {
    final String TAG = SignupActivity.class.getName();
    TextView mloginlink;
    FirebaseAuth fireAuth;
    Button mRgstrBtn;
    EditText mFirstName,mEmail,mPassword,mConfirmpassowrd,mPhonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mConfirmpassowrd = findViewById(R.id.confirmpassword);
        mPhonenumber = findViewById(R.id.Phone_number);
        mRgstrBtn = findViewById(R.id.register_btn);
        mloginlink = findViewById(R.id.login_link);

        fireAuth = FirebaseAuth.getInstance();
        if(fireAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
            finish();
        }

        mRgstrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                String confirm_password = mConfirmpassowrd.getText().toString().trim();
                final String phoneNumber = mPhonenumber.getText().toString().trim();
                final String firstName = mFirstName.getText().toString().trim();

                if(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(confirm_password) && TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(SignupActivity.this, "Please enter required Information", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(firstName)){
                    Toast.makeText(SignupActivity.this, "Full Name is required", Toast.LENGTH_SHORT).show();
                    //mFirstName.setError("Full Name is required");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignupActivity.this, "email is required", Toast.LENGTH_SHORT).show();
                    //mEmail.setError("email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignupActivity.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                    //mPassword.setError("Password cannot be blank");
                    return;
                }
                if(!TextUtils.equals(password,confirm_password)){
                    Toast.makeText(SignupActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    //mConfirmpassowrd.setError("Password doesn't match");
                    return;
                }
                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(SignupActivity.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
                    //mPhonenumber.setError("Phone Number is required");
                    return;
                }
                if(phoneNumber.length() != 12){
                    Toast.makeText(SignupActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phoneNumber.charAt(0) != '+' && phoneNumber.charAt(1) != '1'){
                    Toast.makeText(SignupActivity.this, "Please include country code", Toast.LENGTH_SHORT).show();
                    return;
                }
                fireAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            updateSharedPreference(email, password);
                            SessionContext.setEmail(email);
                            SessionContext.userData();
                            //DataBaseTesting.testdatabase();  // REMOVE AFTER TESTING
                            if (!createDataBaseEntry(email, phoneNumber, firstName, firstName))
                            {
                                /*
                                 * Todo handle db failure
                                 */
                            }
                            startActivity(new Intent(getApplicationContext(),NavigationActivity.class));
                        }else{
                            Toast.makeText(SignupActivity.this, "User not Created" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mloginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }


    void updateSharedPreference(String phoneNumber, String password)
    {
        SharePrefenceHelper sharedPreferences = SharePrefenceHelper.getInstance(getApplicationContext());
        sharedPreferences.saveLoginDeatils(phoneNumber, password);
    }

    boolean createDataBaseEntry(String email, String phoneNumber, String firstName, String lastName)
    {
        try {
            userAddress address = new userAddress(firstName, lastName, "", "", "", "", "");
            IuserData userData = new userData(firstName, email, firstName, lastName, phoneNumber, address);
            SessionContext.addUserData(userData);
            return true;
        }
        catch (Exception e)
        {
            Log.e(TAG, "createDataBaseEntry: Exception thrown" + e.getClass() + e.getMessage());
            return false;
        }
    }
}