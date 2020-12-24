package com.rentable.ProductInfo;

public class Upload {
    private String mName;
    private String mImageUrl;

    public Upload(){
        //constructor
    }

    public Upload(String name, String url){
        if(name.trim().equals("")){
            name="NoName";
        }
        mName=name;
        mImageUrl=url;
    }

    public String getmName(){
        return mName;
    }
    public void setmName(String name){
        mName=name;
    }
    public String getmImageUrl(){
        return mImageUrl;
    }
    public void setmImageUrl(String url){
        mImageUrl=url;
    }
}
