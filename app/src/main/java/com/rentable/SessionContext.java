package com.rentable;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rentable.DataBaseHelper.IProductInfo;
import com.rentable.DataBaseHelper.IuserData;
import com.rentable.DataBaseHelper.userAddress;
import com.rentable.DataBaseHelper.userData;

public class SessionContext {
    private static String TAG = SessionContext.class.getName();
    private static FirebaseDatabase mDatabase = null;
    private static String email = null;
    private static userData mUserData = null;


    public static FirebaseDatabase getDbReference() {
        if(mDatabase == null)
            mDatabase = mDatabase = FirebaseDatabase.getInstance();
        return mDatabase;
    }

    public static String getEmail() {
        if (email==null)
            Log.e(TAG, "NO PHONE NUMBER");
        else
            return email;
        return "";
    }

    // Only for Component testing
    public static void setEmail(String phone)
    {
        email = phone;
    }


    public static void addUserData(IuserData userData) {
        getDbReference().getReference("userData").child(SessionContext.getEmail().split("@")[0]).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public static void addProductInfo(IProductInfo productInfo, String key) {
        getDbReference().getReference("productInfo").child(key).setValue(productInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    public static void userData() {

            DatabaseReference databaseReference = getDbReference().getReference("userData").child(SessionContext.getEmail().split("@")[0]);
            if (databaseReference != null) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try
                        {
                        Log.e(TAG, "callback recieved ");
                            mUserData = (userData) dataSnapshot.getValue(userData.class).clone();
                        } catch (Exception e) {
                            Log.e(TAG, "onDataChange: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
    }

    public static IuserData getUserData() {
        return mUserData;
    }


}
