package com.example.digitalshop.Activities.Authentication;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Interfaces.ImageListener;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.R;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;



public class SignUpFragment extends Fragment implements View.OnClickListener {



    TextView txtLogin;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPhone;
    EditText editTextPass;
    EditText editTextConfirmPass;
    EditText editTextAddress;
    CircleImageView circleImageView;
    Uri imgUri;

    Button btnSignUp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view)
    {

        editTextName=view.findViewById(R.id.editTextName);
        editTextEmail=view.findViewById(R.id.editTextEmail);
        editTextPhone=view.findViewById(R.id.editTextPhone);
        editTextPass=view.findViewById(R.id.editTextPass);
        editTextConfirmPass=view.findViewById(R.id.editTextConfirmPass);
        editTextAddress=view.findViewById(R.id.editTextAddress);
        circleImageView=view.findViewById(R.id.circleImageView);

        btnSignUp=view.findViewById(R.id.btnSignUp);
        txtLogin=view.findViewById(R.id.txtLogin);


        btnSignUp.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        circleImageView.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {


        if(v==btnSignUp)
        {

            if(validate())
            {
                uploadData();
            }

        }
        if(v==circleImageView)
        {
            Util.readImageFromGallery(getActivity() , new ImageListener()
            {
                @Override
                public void onImageLoaded (boolean error , Uri uri , Bitmap bitmap)
                {
                    circleImageView.setImageBitmap(bitmap);
                    imgUri=uri;
                }
            });
        }
        if(v==txtLogin)
        {
           navigatetoLogin();
        }

    }

    private void navigatetoLogin () {
        ((Login_SignUp)getActivity()).txtLogIn.performClick();
    }

    private void uploadData()
    {
        AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(getActivity());
        progressDialog.show();

        User user=createUser();


        FireStoreDatabaseManager.AddUser(user , editTextPass.getText().toString() ,imgUri, new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message,Object object)
            {

                progressDialog.dismiss();
                if(error)
                {
                    Util.showCustomToast(getActivity(),Message,true);
                }
                else
                {
                    Util.showCustomToast(getActivity(),Message,false);
                    clear();
                    navigatetoLogin();
                }

            }
        });


    }

    private User createUser ()
    {

        User user=new User();
        user.setName(editTextName.getText().toString());
        user.setAddress(editTextAddress.getText().toString());
        user.setPhone(editTextPhone.getText().toString());
        user.setEmail(editTextEmail.getText().toString());
        user.setRole("seller");
        return user;
    }

    private void clear()
    {

        for(EditText editText:new EditText[]{editTextName,editTextEmail,editTextPhone,editTextAddress,editTextPass,editTextConfirmPass})
        {
            editText.setText(null);
        }

    }

    private boolean validate()
    {




        if(Util.isEmpty(new EditText[]{editTextName,editTextEmail,editTextPhone,editTextAddress,editTextPass,editTextConfirmPass}))
            return false;

        if(!Util.isEmailValid(editTextEmail.getText().toString()))
        {
            editTextEmail.setError("Invalid Email");
            editTextEmail.requestFocus();
            return false;
        }

        if(editTextEmail.getText().toString().length()<20)
        {
            editTextEmail.setError("Invalid Address | Address must be of At least 20 digits");
            editTextEmail.requestFocus();
            return false;
        }

        if(!Util.isPhoneValid(editTextPhone.getText().toString()))
        {
            editTextPhone.setError("Invalid Phone");
            editTextPhone.requestFocus();
            return false;
        }

        if(!editTextPass.getText().toString().equals(editTextConfirmPass.getText().toString()))
        {
            editTextPass.setError("Password Miss Match");
            editTextPass.requestFocus();
            return false;
        }
        if(editTextPass.getText().toString().length()<6)
        {
            editTextPass.setError("Pin Should be Of Minimum 6 Digits");
            editTextPass.requestFocus();
            return false;
        }

        return true;
    }
}