package com.example.digitalshop.Activities.Buyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.digitalshop.Activities.Buyer.Fragments.BuyerCartFragment;
import com.example.digitalshop.Activities.Buyer.Fragments.BuyerHomeFragment;
import com.example.digitalshop.Activities.Buyer.Fragments.BuyerOrdersFragment;
import com.example.digitalshop.Activities.Buyer.Fragments.BuyerProfileFragment;
import com.example.digitalshop.Activities.Buyer.Fragments.CreateBuyerRequestFragment;
import com.example.digitalshop.Activities.Seller.Fragments.SellerAddProductFragment;
import com.example.digitalshop.Activities.Seller.Fragments.SellerHomeFragment;
import com.example.digitalshop.Activities.Seller.Fragments.SellerOrdersFragment;
import com.example.digitalshop.Activities.Seller.Fragments.SellerProductsFragment;
import com.example.digitalshop.Activities.Seller.Fragments.SellerProfileFragment;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.Util;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class BuyerDashBoardActivity extends AppCompatActivity
{


    ChipNavigationBar navigationbar;
    Window window;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dash_board);
        init();
        navigationbar.setItemSelected(R.id.home,true);
        navigateTo(new BuyerHomeFragment());
    }

    public  void  init()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
           getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }




        navigationbar=findViewById(R.id.navigationbar);
        navigationbar.setItemSelected(R.id.home,true);
        navigationbar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected (int i)
            {


                window.setStatusBarColor(getColor(R.color.white));
                if(i==R.id.home)
                {

                    window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
                    navigateTo(new BuyerHomeFragment());
                }
                if(i==R.id.orders)
                {

                    if(!Util.needsLogIn(getActiity()))
                    {
                        navigateTo(new BuyerOrdersFragment());
                    }
                    else
                    {

                        navigationbar.setItemSelected(R.id.home,true);
                    }

                }
                if(i==R.id.buyerrequest)
                {
                    navigateTo(new CreateBuyerRequestFragment());
                }
                if(i==R.id.cart)
                {

                    if(!Util.needsLogIn(getActiity()))
                    {
                        navigateTo(new BuyerCartFragment());
                    }
                    else
                    {
                        navigationbar.setItemSelected(R.id.home,true);
                    }
                }
                if(i==R.id.profile)
                {

                    window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
                    if(!Util.needsLogIn(getActiity()))
                    {
                        navigateTo(new BuyerProfileFragment());
                    }
                    else
                    {

                        navigationbar.setItemSelected(R.id.home,true);
                    }
                }


            }
        });


    }

    @Override
    protected void onResume () {
        super.onResume();


        updateCartBadge();
    }

    public  void  updateCartBadge()
    {
        if(SharedPref.getCartItems(this).isEmpty())
        {
            dismissbadge();
        }
        else
        {
            showNumonCart(SharedPref.getCartItems(this).size());
        }
    }

    public  void showNumonCart (int val)
    {
        navigationbar.showBadge(R.id.cart,val);
    }
    public  void dismissbadge()
    {
        navigationbar.dismissBadge(R.id.cart);
    }
    public Activity getActiity()
    {
        return this;
    }
    public  void  changeSelection(int id,boolean selected)
    {
        navigationbar.setItemSelected(id,selected);
    }
    public  void  navigateTo (Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout,fragment , null)
                .commit();
    }
}