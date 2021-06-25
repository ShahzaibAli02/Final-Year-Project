package com.example.digitalshop.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.digitalshop.R;
import com.example.digitalshop.Utils.Util;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void onClick (View view)
    {
        System.out.println("CLICKED");
       // Util.showCustomToast(this,"CLICKED",false);
    }
}