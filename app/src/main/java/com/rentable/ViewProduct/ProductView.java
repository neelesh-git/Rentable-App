package com.rentable.ViewProduct;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rentable.DataBaseHelper.IuserData;
import com.rentable.DataBaseHelper.userAddress;
import com.rentable.R;
import com.rentable.SessionContext;

public class ProductView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mProductName, mProductDescription, mProductPrice, mUserName , mRentSuccessful;
    public ImageView imageView;
    public ItemClickListener itemListener;
    public ImageView laptops, mobiles, vehicles , electronics;
    public Button mRentPrduct;


    public ProductView(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_image);
        mProductDescription = itemView.findViewById(R.id.product_description);
        mProductName = itemView.findViewById(R.id.product_name);
        mProductPrice = itemView.findViewById(R.id.product_price);



        laptops = itemView.findViewById(R.id.laptops);
        mobiles = itemView.findViewById(R.id.mobiles);
        vehicles = itemView.findViewById(R.id.vehicles);
        electronics = itemView.findViewById(R.id.electronics);

        mRentPrduct = itemView.findViewById(R.id.rent_product);
        mRentSuccessful = itemView.findViewById(R.id.rent_success);



        mUserName = itemView.findViewById(R.id.user_profile_name);



    }


    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemListener = listener;
    }




    @Override
    public void onClick(View view) {
        itemListener.onClick(view, getAdapterPosition(), false);
    }
}
