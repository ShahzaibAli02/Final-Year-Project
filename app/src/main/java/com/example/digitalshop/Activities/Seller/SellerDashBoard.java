package com.example.digitalshop.Activities.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.digitalshop.Activities.Seller.Fragments.SellerHomeFragment;
import com.example.digitalshop.R;



public class SellerDashBoard extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dash_board);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout,new SellerHomeFragment() , null)
                .commit();
    }
}