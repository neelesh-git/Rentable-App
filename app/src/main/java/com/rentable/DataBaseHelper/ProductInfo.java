package com.rentable.DataBaseHelper;


public class ProductInfo implements IProductInfo {

    private String mProductId;
    private String mProcuctName;
    private String mPrice;
    private String mPostedDate;
    private CATEGORY mCategory;
    private String mImageId;
    private String mUserId;

    public ProductInfo(String mProductId, String mProcuctName, String mPrice, String mPostedDate, CATEGORY mCategory, String mImageId,String mUserId) {
        this.mProductId = mProductId;
        this.mProcuctName = mProcuctName;
        this.mPrice = mPrice;
        this.mPostedDate = mPostedDate;
        this.mCategory = mCategory;
        this.mImageId = mImageId;
        this.mUserId = mUserId;
    }

    public ProductInfo(){

    }

    public String getProductId() {
        return mProductId;
    }

    public void setProductId(String mProductId) {
        this.mProductId = mProductId;

    }

    public String getProcuctName() {
        return mProcuctName;
    }

    public void setProcuctName(String mProcuctName) {
        this.mProcuctName = mProcuctName;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getPostedDate() {
        return mPostedDate;
    }

    public void setPostedDate(String mPostedDate) {
        this.mPostedDate = mPostedDate;
    }

    public CATEGORY getCategory() {
        return mCategory;
    }

    public void setCategory(CATEGORY mCategory) {
        this.mCategory = mCategory;
    }

    public String getImageId() {
        return mImageId;
    }

    public void setImageId(String mImageId) {
        this.mImageId = mImageId;
    }

    public void setUserId(String mUserId)
    {
        this.mUserId = mUserId;
    }

    public String getUserId()
    {
        return mUserId;
    }

}
