package com.example.digitalshop.Activities.Authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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

import com.example.digitalshop.Activities.MapsActivity;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Interfaces.ImageListener;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;



public class SignUpFragment extends Fragment implements View.OnClickListener {



    TextView txtLogin;
    TextView txtStoreName;
    TextView txtStoreDisc;
    TextView txtStoreImage;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPhone;
    EditText editTextPass;
    EditText editTextStoreName;
    EditText editTextStoreDesc;
    EditText editTextConfirmPass;
    TextView txtAddress;
    CircleImageView circleImageView;
    Uri imgUri;

    Button btnSignUp;
    LatLng latLng;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);


        showhideViews();
    }

    private void showhideViews ()
    {

        int visibility=isSeller()?View.VISIBLE:View.GONE;
        txtStoreName.setVisibility(visibility);
        txtStoreImage.setVisibility(visibility);
        txtStoreDisc.setVisibility(visibility);
        editTextStoreName.setVisibility(visibility);
        editTextStoreDesc.setVisibility(visibility);
    }

    public  boolean isSeller()
    {

        return  SharedPref.getUserType(getActivity()).equals("seller");
    }

    private void initViews(View view)
    {

        editTextName=view.findViewById(R.id.editTextName);
        editTextEmail=view.findViewById(R.id.editTextEmail);
        editTextPhone=view.findViewById(R.id.editTextPhone);
        editTextPass=view.findViewById(R.id.editTextPass);
        editTextStoreDesc=view.findViewById(R.id.editTextStoreDesc);
        editTextStoreName=view.findViewById(R.id.editTextStoreName);
        editTextConfirmPass=view.findViewById(R.id.editTextConfirmPass);
        txtAddress=view.findViewById(R.id.TextAddress);
        txtStoreImage=view.findViewById(R.id.txtStoreImage);
        circleImageView=view.findViewById(R.id.circleImageView);

        btnSignUp=view.findViewById(R.id.btnSignUp);
        txtLogin=view.findViewById(R.id.txtLogin);
        txtStoreName=view.findViewById(R.id.txtStoreName);
        txtStoreDisc=view.findViewById(R.id.txtStoreDisc);


        btnSignUp.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        txtAddress.setOnClickListener(this);



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

        if(v==txtAddress)
        {
            Intent n=new Intent(getActivity(), MapsActivity.class);
            startActivityForResult(n,2);
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
        user.setAddress(txtAddress.getText().toString());
        user.setPhone(editTextPhone.getText().toString());
        user.setEmail(editTextEmail.getText().toString());
        user.setRole(SharedPref.getUserType(getActivity()));
        user.setLat(latLng.latitude);
        user.setLng(latLng.longitude);

        if(isSeller())
        {
            user.setShop_name(editTextStoreName.getText().toString());
            user.setShop_description(editTextStoreDesc.getText().toString());
        }
        return user;
    }

    private void clear()
    {

        for(EditText editText:new EditText[]{editTextName,editTextEmail,editTextPhone,editTextPass,editTextConfirmPass})
        {
            editText.setText(null);
        }

    }

    private boolean validate()
    {




        if(Util.isEmpty(new EditText[]{editTextName,editTextEmail,editTextPhone,editTextPass,editTextConfirmPass}))
            return false;

        if(txtAddress.getText().toString().isEmpty())
        {
            Util.showSnackBar(getActivity(),"Please Select Location");
            return false;
        }

        if(!Util.isEmailValid(editTextEmail.getText().toString()))
        {
            editTextEmail.setError("Invalid Email");
            editTextEmail.requestFocus();
            return false;
        }


        if(!Util.isPhoneValid(editTextPhone.getText().toString()))
        {
            editTextPhone.setError("Phone number should be of 11 digits");
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


        if(isSeller())
        {
            if(editTextStoreName.getText().toString().length()<6)
            {
                editTextStoreName.setError("Required Field");
                editTextStoreName.requestFocus();
                return false;
            }
            if(editTextStoreDesc.getText().toString().length()<6)
            {
                editTextStoreDesc.setError("Required Field");
                editTextStoreDesc.requestFocus();
                return false;
            }

        }

        return true;
    }

    @Override
    public void onActivityResult (int requestCode , int resultCode , @Nullable Intent data) {
        super.onActivityResult(requestCode , resultCode , data);

        if(requestCode==2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Bundle extras = data.getExtras();
                latLng= new LatLng(extras.getDouble("Lat"),extras.getDouble("Long"));
                txtAddress.setText(extras.getString("Address",""));
            }

        }
    }
}