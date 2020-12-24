package com.rentable.DataBaseHelper;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rentable.SessionContext;

import java.util.HashMap;

public class userData implements IuserData, Cloneable {
    private String mEmail;
    private String mUserId;
    private String mFirstName;
    private String mLastName;
    private String mPhoneNumber;
    private userAddress address;
    private HashMap<String, String> mProductInfo = new HashMap<>();

    public userData(String mUserId, String mEmail, String mFirstName, String mLastName, String mPhoneNumber, userAddress address) {
        this.mEmail = mEmail;
        this.mUserId = mUserId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mPhoneNumber = mPhoneNumber;
        this.address = address;
    }
    public userData(String mUserId, String mEmail, String mFirstName, String mLastName, String mPhoneNumber, userAddress address, HashMap<String, String> mProductInfo) {
        this.mEmail = mEmail;
        this.mUserId = mUserId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mPhoneNumber = mPhoneNumber;
        this.address = address;
        this.mProductInfo = mProductInfo;
    }
    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

    public userData() {

    }

    public String getEmail()
    {
        return mEmail;
    }

    public void setEmail(String mEmail)
    {
        this.mEmail = mEmail;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }
    public void setFirstNameDb(String mFirstName) {
        this.mFirstName = mFirstName;
        updateDatabase("firstName", mFirstName);
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }
    public void setLastNameDb(String mLastName) {
        this.mLastName = mLastName;
        updateDatabase("lastName", mLastName);
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public userAddress getAddress() {
        return address;
    }

    public void setAddress(userAddress address) {
        this.address = address;
    }

    public HashMap<String, String> getProductInfo() {
        return mProductInfo;
    }

    public void setProductInfo(HashMap<String, String> productInfo)
    {
        mProductInfo = productInfo;
    }

    public String generateProductId() {
        return Integer.toString((SessionContext.getEmail() + mProductInfo.size()).hashCode());
    }

    public String uniqueProductId()
    {
        return "value"+ mProductInfo.size();
    }

    public void addNewProduct(String productInfo) {
        String key = uniqueProductId();
        this.mProductInfo.put(key, productInfo);
        SessionContext.getDbReference().getReference("userData").child(SessionContext.getEmail().split("@")[0]).child("productInfo").child(key).setValue(productInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
    }

    public void removeProduct(String productInfo) {
        if(mProductInfo.containsValue(productInfo))
            mProductInfo.remove(productInfo);
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
                        // Write failed
                        // ...
                    }
                });
    }

}
