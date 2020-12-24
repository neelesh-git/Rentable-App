package com.rentable.DataBaseHelper;

public interface IProductInfo {
    enum CATEGORY
    {

        VEHICLES,
        LAPTOPS,
        MOBILE_PHONES,
        ELECTRONICS,
    }
     String getProductId();
     String getProcuctName();
     void setProcuctName(String mProcuctName);
     String getPrice();
     void setPrice(String mPrice);
     String getPostedDate();
     CATEGORY getCategory();
     void setCategory(CATEGORY mCategory);
     String getImageId();
     String getUserId();

}
