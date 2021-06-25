package com.example.digitalshop.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.digitalshop.Activities.Authentication.Login_SignUp;
import com.example.digitalshop.Activities.Seller.SellerDashBoard;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;

public class SplashScreen extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if(SharedPref.getUser(this)==null)
        {
            //finish();
            startActivity(new Intent(this, Login_SignUp.class));
        }
        else
        {
            if(SharedPref.getUser(this).getRole().equalsIgnoreCase("seller"))
            {
                finish();
                startActivity(new Intent(this, SellerDashBoard.class));
            }

        }
    }
}