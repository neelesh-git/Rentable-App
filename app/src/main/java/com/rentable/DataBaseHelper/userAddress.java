package com.rentable.DataBaseHelper;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rentable.SessionContext;
import com.rentable.rentableAuthentication.LoginActivity.LoginActivity;

public class userAddress {
    private String mFirstName;
    private String mLastName;
    private String mAddress1;
    private String mCity;
    private String mPin;
    private String mState;
    private String mAddress2;

    public userAddress(String mFirstName, String mLastName, String mAddress1, String mAddress2, String mCity, String mPin, String mState) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mAddress1 = mAddress1;
        this.mAddress2 = mAddress2;
        this.mCity = mCity;
        this.mPin = mPin;
        this.mState = mState;
    }
    public userAddress() {

    }

    public String getFirstName() {
        return mFirstName;
    }
    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }
    public void setFirstNameDb(String mFirstName) {
        this.mFirstName = mFirstName;
        updateDatabase("address/firstName", mFirstName);
    }

    public String getLastName() {
        return mLastName;
    }
    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }
    public void setLastNameDb(String mLastName) {
        this.mLastName = mLastName;
        updateDatabase("address/lastName", mLastName);
    }

    public String getAddress1() {
        return mAddress1;
    }
    public void setAddress1(String mAddress1) {
        this.mAddress1 = mAddress1;
    }
    public void setAddress1Db(String mAddress1) {
        this.mAddress1 = mAddress1;
        updateDatabase("address/address1", mAddress1);
    }

    public String getAddress2() {
        return mAddress2;
    }
    public void setAddress2(String mAddress2) {
        this.mAddress2 = mAddress2;
    }
    public void setAddress2Db(String mAddress2) {
        this.mAddress2 = mAddress2;
        updateDatabase("address/address2", mAddress2);
    }

    public String getCity() {
        return mCity;
    }
    public void setCity(String mCity) {
        this.mCity = mCity;
    }
    public void setCityDb(String mCity) {
        this.mCity = mCity;
        updateDatabase("address/city", mCity);
    }

    public String getPin() {
        return mPin;
    }
    public void setPin(String mPin) {
        this.mPin = mPin;
    }
    public void setPinDb(String mPin) {
        this.mPin = mPin;
        updateDatabase("address/pin", mPin);
    }

    public String getState() {
        return mState;
    }
    public void setState(String mState) {
        this.mState = mState;
    }
    public void setStateDb(String mState) {
        this.mState = mState;
        updateDatabase("address/state", mState);
    }

    //Util
    public void print() {
        System.out.println(mFirstName + " " + mLastName);
        System.out.println(mAddress1);
        System.out.println(mAddress2);
        System.out.println(mCity + " " + mState);
        System.out.println(mPin);
    }

    private void updateDatabase(String path, String value) {
        SessionContext.getDbReference().getReference("userData").child(SessionContext.getEmail().split("@")[0]).child(path).setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });;
    }
}
