package com.example.digitalshop.Activities.Authentication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.digitalshop.Activities.MainActivity;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.Util;


public class Login_SignUp extends AppCompatActivity implements View.OnClickListener {

    LoginFragment Loginfragment;
    Fragment SignUpfragment;
    public TextView txtLogIn;
    public TextView txtSignUp;

    boolean isSignupSelected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        initViews();
        navigateTo(Loginfragment);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setCancelable(true);
            alertBuilder.setMessage("Location Permission required show notification .. Please Click On Allow All Time ");
            alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick (DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q)
                    {
                        ActivityCompat.requestPermissions(Login_SignUp.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(Login_SignUp.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                                1);
                    }
                }
            });
            alertBuilder.show();

        }
    }

    private void initViews()
    {


        Loginfragment=new LoginFragment();
        SignUpfragment=new SignUpFragment();
        txtLogIn=findViewById(R.id.txtLogin);
        txtSignUp=findViewById(R.id.txtSignUp);


        txtLogIn.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);



    }

    @Override
    public void onClick(View v)
    {

        if(v==txtLogIn)
        {
            selectL_SU(txtLogIn);
            navigateTo(Loginfragment);
        }
        if(v==txtSignUp)
        {
            if(!isSignupSelected)
            {
                dialogChooseSellerBuyer();
            }

        }


    }

    private void dialogChooseSellerBuyer ()
    {

        Dialog dialog= Util.getDialog(this,R.layout.lyt_dialog_seller_buyer_selection);
        Button btnSeller=dialog.findViewById(R.id.btnSeller);
        Button btnBuyer=dialog.findViewById(R.id.btnBuyer);
        dialog.show();


        View.OnClickListener onClickListener= new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {

                dialog.dismiss();
                if(v==btnBuyer)
                {
                    SharedPref.saveUserType(Login_SignUp.this,"buyer");
                }
                if(v==btnSeller)
                {
                    SharedPref.saveUserType(Login_SignUp.this,"seller");
                }

                selectL_SU(txtSignUp);
                navigateTo(SignUpfragment);
            }
        };


        btnSeller.setOnClickListener(onClickListener);
        btnBuyer.setOnClickListener(onClickListener);
    }

    public  void selectL_SU(TextView textView) //Select Login Or SignUp
    {


        unSelect(new TextView[]{txtLogIn,txtSignUp});
        select(textView);

    }
    private void select(TextView txtView)
    {
        txtView.setTextColor(getResources().getColor(R.color.white));
        txtView.setBackgroundResource(R.drawable.back_blue);
    }

    public  void unSelect(TextView[] textViews)
    {
        for(TextView textView:textViews)
        {
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            textView.setBackground(null);
        }
    }


    private  void navigateTo(Fragment fragment)
    {

        isSignupSelected=fragment == SignUpfragment;

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout,fragment, null)
                .commit();

    }

    @Override
    public void onBackPressed () {
      //  super.onBackPressed();
    }
}