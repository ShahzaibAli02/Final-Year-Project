package com.example.digitalshop.Activities.Buyer.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.digitalshop.Activities.Authentication.Login_SignUp;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Interfaces.ImageListener;
import com.example.digitalshop.Interfaces.ImageUploadListener;
import com.example.digitalshop.Model.Analytics;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.github.ybq.android.spinkit.sprite.RectSprite;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class BuyerProfileFragment extends Fragment implements View.OnClickListener
{


    CircleImageView circleImageView;
    TextView txtName;
    TextView txtEmail;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPhone;
    EditText editTextAddress;
    Button btnUpdate;
    ImageView imgLogout;
    User user;
    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_buyer_profile , container , false);
    }

    @Override
    public void onViewCreated (@NonNull @NotNull View view , @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        init(view);
        setVals();
        imgLogout.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    private void setVals ()
    {

        user= SharedPref.getUser(getActivity());


        Picasso.get().load(user.getImage()).placeholder(R.drawable.loading_gif).error(R.drawable.ic_person).into(circleImageView);


        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());
        editTextName.setText(user.getName());
        editTextEmail.setText(user.getEmail());
        editTextAddress.setText(user.getAddress());
        editTextPhone.setText(user.getPhone());



    }

    public  void  init(View view)
    {



        imgLogout=view.findViewById(R.id.imgLogout);
        circleImageView=view.findViewById(R.id.circleImageView);
        txtName=view.findViewById(R.id.txtName);
        txtEmail=view.findViewById(R.id.txtEmail);
        editTextName=view.findViewById(R.id.editTextName);
        editTextEmail=view.findViewById(R.id.editTextEmail);
        editTextPhone=view.findViewById(R.id.editTextPhone);
        editTextAddress=view.findViewById(R.id.editTextAddress);
        btnUpdate=view.findViewById(R.id.btnUpdate);
    }

    private void updateImage ()
    {

        Util.readImageFromGallery(getActivity() , new ImageListener()
        {
            @Override
            public void onImageLoaded (boolean error , Uri uri , Bitmap bitmap)
            {


                if(!error)
                {
                    Picasso.get().load(R.drawable.loading_gif).into(circleImageView);
                    FireStoreDatabaseManager.uploadImage(uri , new ImageUploadListener()
                    {
                        @Override
                        public void onUpload (boolean Error , String Message , String url)
                        {

                            if(!Error)
                            {
                                Picasso.get().load(uri).into(circleImageView);
                                user.setImage(url);
                                Util.showSnackBar(getActivity(),"Image Uploaded Click On Save Button To Save Changes");
                            }
                            else
                            {
                                Util.showCustomToast(getActivity(),"Failed to Upload Image",true);
                                Picasso.get().load(user.getImage()).into(circleImageView);
                            }
                        }
                    });
                }

            }
        });
    }



    private void saveChanges ()
    {

        AlertDialog progressDialogManager= ProgressDialogManager.getProgressDialog(getActivity());
        progressDialogManager.show();

        user.setPhone(editTextPhone.getText().toString());
        user.setName(editTextName.getText().toString());
        user.setAddress(editTextAddress.getText().toString());

        FireStoreDatabaseManager.updateProfile(user , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data)
            {

                progressDialogManager.dismiss();
                if(!error)
                {
                    Util.showCustomToast(getActivity(),Message,false);
                    SharedPref.saveUser(getActivity(),user);
                    setVals();
                }
                else
                {
                    Util.showCustomToast(getActivity(),Message,true);
                }

            }
        });

    }

    @Override
    public void onClick (View v) {

        if(v==imgLogout)
        {
            ActivityCompat.finishAffinity(getActivity());
            SharedPref.clearUser(getActivity());
            getActivity().finish();
            startActivity(new Intent(getActivity(), Login_SignUp.class));
        }
        if(v==circleImageView)
        {
            updateImage();
        }
        if(v==btnUpdate)
        {

            if(Util.isEmpty(new EditText[]{editTextName,editTextPhone,editTextAddress}))
            {
                return;
            }

            if(editTextPhone.getText().toString().length()<10)
            {
                editTextPhone.setError("Invalid Phone");
                editTextPhone.requestFocus();
                return;
            }
            if(editTextAddress.getText().toString().length()<20)
            {
                editTextAddress.setError("Invalid Address");
                editTextAddress.requestFocus();
                return;
            }

            saveChanges();
        }

    }



}