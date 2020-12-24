package com.rentable.DataBaseHelper;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.rentable.SessionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBaseTesting {

    public static String TAG = DataBaseTesting.class.getName();


    static String getAlphaNumericString(int n) {

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static void testdatabaseSetAndGet() {
        FirebaseDatabase mDatabase = SessionContext.getDbReference();


        SessionContext.setEmail("68228348s");
        Log.e(TAG, "testdatabase:  DATBASE CALLED");

        userAddress address = new userAddress("Ravikiran", "sunnam", "1400 millersport Hwy", "APT 324", "buffalo", "14221", "NY");
        IuserData userData = new userData("ravi2301", SessionContext.getEmail(), "Ravikiran", "sunnam", "6822684814", address);
        SessionContext.addUserData(userData);

        //IuserData userData = SessionContext.getuserData();
        userData.setFirstNameDb("KRANNNNNNN");
        userData.setLastNameDb("KIRANAA");
        userAddress add = userData.getAddress();
        add.setAddress1("Address1");
        add.setAddress2("Address2");
        add.setCity("Williamsville");
        add.setState("NewYork");
        add.setPin("500055");
        add.setFirstName("NEWW");
        add.setLastName("KIRANNNN");
        String key = userData.generateProductId();
        IProductInfo product = new ProductInfo(SessionContext.getEmail(), getAlphaNumericString(7), "50000", "20March", IProductInfo.CATEGORY.VEHICLES, getAlphaNumericString(7), SessionContext.getEmail().split("@")[0]);
        SessionContext.addProductInfo(product, key);
        userData.addNewProduct(key);
        Log.e(TAG, "testdatabase:  DATBASE CALLED Next ");
        /*
        //IuserData mUserData = SessionContext.getuserData();
        Log.e(TAG, "User name: " + userData.getFirstName());
        Log.e(TAG, "User name: " + userData.getProductInfo());
        HashMap<String, String> example = userData.getProductInfo();
        for (String name : example.keySet()) {
            String keyTemp = name;
            String value = example.get(name);
            System.out.println(keyTemp + " " + value);
        }*/
        Log.e(TAG, "testdatabase: ");
    }

    public static void testdatabase() {
        FirebaseDatabase mDatabase = SessionContext.getDbReference();
        Log.e(TAG, "testdatabase:  DATBASE second");
        //mDatabase.getReference("userData").removeValue();
        //mDatabase.getReference("productInfo").removeValue();
        ArrayList<IProductInfo.CATEGORY> catgory = new ArrayList<IProductInfo.CATEGORY>(Arrays.asList(IProductInfo.CATEGORY.MOBILE_PHONES, IProductInfo.CATEGORY.VEHICLES,IProductInfo.CATEGORY.LAPTOPS,IProductInfo.CATEGORY.ELECTRONICS));
        for (int i = 0; i < 5; i++) {
            String phoneNumber = getAlphaNumericString(7);
            SessionContext.setEmail(phoneNumber);
            String firstName = getAlphaNumericString(7);
            String lastName = getAlphaNumericString(7);
            userAddress addressTemp = new userAddress(firstName, lastName, getAlphaNumericString(7), getAlphaNumericString(7), getAlphaNumericString(7), getAlphaNumericString(7), getAlphaNumericString(7));
            IuserData userDataTemp = new userData(getAlphaNumericString(7), SessionContext.getEmail(), firstName, lastName, phoneNumber, addressTemp);
            SessionContext.addUserData(userDataTemp);
            int n = (int) (5 * Math.random());

            for (int j = 0; j < n; j++) {
                String price = String.valueOf((int) (100000 * Math.random()));
                String productId = userDataTemp.generateProductId();
                int k = (int) (5 * Math.random());
                ProductInfo product = new ProductInfo("NEWP RODUCT TESTING", "NEWP RODUCT TESTING", price, "20March", IProductInfo.CATEGORY.values()[new Integer(k)], getAlphaNumericString(7), SessionContext.getEmail().split("@")[0]);
                userDataTemp.addNewProduct(productId);
                SessionContext.addProductInfo(product, productId);

            }
        }


    }

    public static void testProductInsertion()
    {
        for (int j = 0; j < 5; j++) {
            String price = String.valueOf((int) (100000 * Math.random()));
            String productId = SessionContext.getUserData().generateProductId();
            int k = (int) (5 * Math.random());
            ProductInfo product = new ProductInfo("NEWP RODUCT TESTING", "NEWP RODUCT TESTING", price, "20March", IProductInfo.CATEGORY.values()[new Integer(k)], getAlphaNumericString(7), SessionContext.getEmail().split("@")[0]);
            SessionContext.getUserData().addNewProduct(productId);
            SessionContext.addProductInfo(product, productId);

        }
    }
}
