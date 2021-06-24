package com.example.digitalshop.Activities.Authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.digitalshop.Activities.MainActivity;
import com.example.digitalshop.R;


public class Login_SignUp extends AppCompatActivity implements View.OnClickListener {

    LoginFragment Loginfragment;
    Fragment SignUpfragment;
    public TextView txtLogIn;
    public TextView txtSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        initViews();
        navigateTo(Loginfragment);
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
            selectL_SU(txtSignUp);
            navigateTo(SignUpfragment);
        }


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

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frameLayout,fragment, null)
                .commit();

    }
}