package com.rentable.DataBaseHelper;

import com.rentable.DataBaseHelper.userAddress;

import java.util.ArrayList;
import java.util.HashMap;

public interface  IuserData {

    String getPhoneNumber();
    String getEmail();
    String getUserId();


    HashMap<String, String> getProductInfo();

    void addNewProduct(String productInfo);
    void removeProduct(String productInfo);

    String getFirstName();
    void setFirstNameDb(String mFirstName);

    String getLastName();
    void setLastNameDb(String mLastName);
    userAddress getAddress();

    String uniqueProductId();
    String generateProductId();

}
