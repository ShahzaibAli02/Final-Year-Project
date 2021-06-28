package com.example.digitalshop.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.digitalshop.Activities.Authentication.Login_SignUp;
import com.example.digitalshop.Activities.Buyer.BuyerDashBoardActivity;
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


        new CountDownTimer(2000,1000)
        {

            @Override
            public void onTick (long millisUntilFinished) {

            }

            @Override
            public void onFinish ()
            {
                openNextActivity();
            }
        }.start();


    }

    private void openNextActivity ()
    {

        if(SharedPref.getUser(this)==null)
        {

            if(SharedPref.getUserType(this).isEmpty())
            {
                finish();
                startActivity(new Intent(this, Seller_Buyer_Selection_Activity.class));
            }
            else
            {


                if(SharedPref.getUserType(this).equalsIgnoreCase("seller"))
                {
                    finish();
                    startActivity(new Intent(this, Login_SignUp.class));
                }
                else
                {
                    finish();
                    startActivity(new Intent(this, BuyerDashBoardActivity.class));
                }
            }

        }
        else
        {
            if(SharedPref.getUser(this).getRole().equalsIgnoreCase("seller"))
            {
                finish();
                startActivity(new Intent(this, SellerDashBoard.class));
            }
            else
            {
                finish();
                startActivity(new Intent(this, BuyerDashBoardActivity.class));
            }

        }

    }
}