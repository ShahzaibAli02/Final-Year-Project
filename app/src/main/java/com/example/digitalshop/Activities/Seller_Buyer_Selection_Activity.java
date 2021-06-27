package com.example.digitalshop.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.digitalshop.Activities.Authentication.Login_SignUp;
import com.example.digitalshop.Activities.Buyer.BuyerDashBoardActivity;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;

public class Seller_Buyer_Selection_Activity extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_buyer_selection);
    }

    public void onClickSeller (View view)
    {
        movetoNextActivity("seller", Login_SignUp.class);
    }

    public void onClickBuyer (View view)
    {
        movetoNextActivity("buyer", BuyerDashBoardActivity.class);
    }


    public void  movetoNextActivity(String user_type, Class activity)
    {
        SharedPref.saveUserType(this,user_type);
        finish();
        startActivity(new Intent(this,activity));
    }
}