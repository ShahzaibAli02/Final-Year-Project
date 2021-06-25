package com.example.digitalshop.Activities.Seller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.digitalshop.Activities.Seller.Fragments.SellerAddProductFragment;
import com.example.digitalshop.Activities.Seller.Fragments.SellerHomeFragment;
import com.example.digitalshop.Activities.Seller.Fragments.SellerProductsFragment;
import com.example.digitalshop.Activities.Seller.Fragments.SellerProfileFragment;
import com.example.digitalshop.R;
import com.example.digitalshop.Utils.Util;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class SellerDashBoard extends AppCompatActivity
{


    ChipNavigationBar navigationbar;
    Window window;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dash_board);
        init();
        navigateTo(new SellerHomeFragment());
    }
    public  void  init()
    {

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.background));
        }




        navigationbar=findViewById(R.id.navigationbar);
        navigationbar.setItemSelected(R.id.home,true);
        navigationbar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected (int i)
            {

                if(window!=null)
                    window.setStatusBarColor(getColor(R.color.background));
                if(i==R.id.home)
                {
                    navigateTo(new SellerHomeFragment());
                }
                if(i==R.id.orders)
                {
                  //
                }
                if(i==R.id.products)
                {
                    navigateTo(new SellerProductsFragment());
                }
                if(i==R.id.addproduct)
                {
                    navigateTo(new SellerAddProductFragment());
                }
                if(i==R.id.profile)
                {
                    navigateTo(new SellerProfileFragment());
                    if(window!=null)
                        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
                }


            }
        });


    }

    public  void  navigateTo(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout,fragment , null)
                .commit();
    }
}