package com.rentable.HomeScreen.HomeScreenNavigation;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rentable.DataBaseHelper.IuserData;
import com.rentable.DataBaseHelper.userAddress;
import com.rentable.R;
import com.rentable.SessionContext;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static com.rentable.SessionContext.getUserData;

public class SettingsActivity extends AppCompatActivity {


    private EditText mFirstName,mLastName,mAddress1,mAddress2,mCity,mState,mPin;
    private TextView mPicChangeBtn, mCloseBtn, mSaveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFirstName = findViewById(R.id.settings_firstName);
        mLastName = findViewById(R.id.settings_lastName);
        mAddress1 = findViewById(R.id.settings_address1);
        mAddress2 = findViewById(R.id.settings_address2);
        mCity = findViewById(R.id.settings_city);
        mState = findViewById(R.id.settings_state);
        mPin = findViewById(R.id.settings_pin);

        mPicChangeBtn = findViewById(R.id.profile_change_btn);
        mCloseBtn = findViewById(R.id.close_user_settings_btn);
        mSaveBtn = findViewById(R.id.save_user_settings_btn);


        displayUserInformation();

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                updateUserProfile();
            }
        });




    }

    private void updateUserProfile() {
        IuserData userData = SessionContext.getUserData();

        userAddress address =  userData.getAddress();

        String fNameDB = address.getFirstName();
        String lNameDB = address.getLastName();
        String address1DB = address.getAddress1();
        String address2DB = address.getAddress2();
        String cityDB = address.getCity();
        String stateDB = address.getState();
        String pinDB = address.getPin();

        String fNameUpdate = mFirstName.getText().toString().trim();
        String lNameUpdate = mLastName.getText().toString().trim();
        String address1Update = mAddress1.getText().toString().trim();
        String address2Update = mAddress2.getText().toString().trim();
        String cityUpdate = mCity.getText().toString().trim();
        String stateUpdate = mState.getText().toString().trim();
        String pinUpdate = mPin.getText().toString().trim();


        if(TextUtils.isEmpty(fNameUpdate))
        {
            Toast.makeText(this, "First Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(lNameUpdate))
        {
            Toast.makeText(this, "Last Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address1Update))
        {
            Toast.makeText(this, "Address 1 is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address2Update))
        {
            Toast.makeText(this, "Address 2 is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityUpdate))
        {
            Toast.makeText(this, "City is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(stateUpdate))
        {
            Toast.makeText(this, "State is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pinUpdate))
        {
            Toast.makeText(this, "Pin Code is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else
        {

            if (checkString(fNameDB,fNameUpdate)) {
                address.setFirstNameDb(fNameUpdate);
                userData.setFirstNameDb(fNameUpdate);
            }
            if (checkString(lNameDB,lNameUpdate)) {
                address.setLastNameDb(lNameUpdate);
                userData.setLastNameDb(lNameUpdate);
            }

            if (checkString(address1DB,address1Update))
                address.setAddress1Db(address1Update);

            if (checkString(address2DB,address2Update))
                address.setAddress2Db(address2Update);

            if (checkString(cityDB,cityUpdate))
                address.setCityDb(cityUpdate);

            if (checkString(stateDB,stateUpdate))
                address.setStateDb(stateUpdate);

            if (checkString(pinDB,pinUpdate))
                address.setPinDb(pinUpdate);

            finish();
        }





    }

    private boolean checkString(String s1, String s2) {
        if (!s1.equals(s2))
            return true;
        return false;
    }

    private void displayUserInformation() {

        IuserData userData = SessionContext.getUserData();

        userAddress address =  userData.getAddress();

        String fNameDB = address.getFirstName();
        String lNameDB = address.getLastName();
        String address1DB = address.getAddress1();
        String address2DB = address.getAddress2();
        String cityDB = address.getCity();
        String stateDB = address.getState();
        String pinDB = address.getPin();

        mFirstName.setText(fNameDB);
        mLastName.setText(lNameDB);
        mAddress1.setText(address1DB);
        mAddress2.setText(address2DB);
        mCity.setText(cityDB);
        mState.setText(stateDB);
        mPin.setText(pinDB);

    }
}
