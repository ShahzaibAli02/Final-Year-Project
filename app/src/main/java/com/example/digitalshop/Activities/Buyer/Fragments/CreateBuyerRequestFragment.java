package com.example.digitalshop.Activities.Buyer.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.BuyerRequest;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.google.firebase.auth.FirebaseAuth;

import org.intellij.lang.annotations.Pattern;

import java.util.List;


public class CreateBuyerRequestFragment extends Fragment implements View.OnClickListener
{



    Spinner SpinnerStoreName;
    EditText editTextTitle;
    EditText editTextStoreDesc;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPhone;
    List<User> userList;
    Button btnSendRequest;
    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_create_buyer_request , container , false);
    }

    @Override
    public void onViewCreated (@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        initViews(view);
        loadStoresData();
        btnSendRequest.setOnClickListener(this);
    }

    private void loadStoresData ()
    {

        AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(getActivity());
        progressDialog.show();
        FireStoreDatabaseManager.getAllStores(new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data)
            {

                progressDialog.dismiss();
                if(!error)
                {
                    userList= (List <User>) data;
                    SpinnerStoreName.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,userList));


                }
                else
                {
                    Util.showCustomToast(getActivity(),Message,true);
                }

            }
        });

    }

    private void initViews (View view)
    {

        editTextTitle=view.findViewById(R.id.editTextTitle);
        SpinnerStoreName=view.findViewById(R.id.SpinnerStoreName);
        editTextStoreDesc=view.findViewById(R.id.editTextStoreDesc);
        editTextName=view.findViewById(R.id.editTextName);
        editTextEmail=view.findViewById(R.id.editTextEmail);
        editTextPhone=view.findViewById(R.id.editTextPhone);
        btnSendRequest=view.findViewById(R.id.btnSendRequest);


        User user = SharedPref.getUser(getActivity());
        if(user!=null)
        {

            editTextName.setText(user.getName());
            editTextEmail.setText(user.getEmail());
            editTextPhone.setText(user.getPhone());

        }

    }

    @Override
    public void onClick (View v)
    {

        if(v==btnSendRequest)
        {
            BuyerRequest buyerRequest=getBuyerRequest();
            if(buyerRequest!=null)
            {

                AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(getActivity());
                progressDialog.show();
                FireStoreDatabaseManager.addBuyerRequest(buyerRequest , new DataBaseResult()
                {
                    @Override
                    public void onResult (boolean error , String Message , Object data) {

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                        if(!error)
                        {
                            editTextStoreDesc.setText(null);
                            editTextTitle.setText(null);
                        }
                        Util.showCustomToast(getActivity(),Message,error);

                    }
                });

            }
        }

    }

    private BuyerRequest getBuyerRequest ()
    {

        if(Util.isEmpty(new EditText[]{editTextTitle,editTextStoreDesc,editTextName,editTextEmail,editTextPhone}))
            return null;



        if(!Util.isEmailValid(editTextEmail.getText().toString()))
        {
            editTextEmail.setError("Invalid Email");
            editTextEmail.requestFocus();
            return null;

        }
        if(!Util.isPhoneValid(editTextPhone.getText().toString()))
        {
            editTextPhone.setError("Phone number should be of 11 digits");
            editTextPhone.requestFocus();
            return null;
        }

        if(SpinnerStoreName.getSelectedItemPosition()<0)
            return null;

        BuyerRequest buyerRequest=new BuyerRequest();
        buyerRequest.setTitle(editTextTitle.getText().toString());
        buyerRequest.setRequest_discription(editTextStoreDesc.getText().toString());
        buyerRequest.setUser_name(editTextName.getText().toString());
        buyerRequest.setUser_phone(editTextPhone.getText().toString());
        buyerRequest.setUser_email(editTextEmail.getText().toString());
        buyerRequest.setUser_uid(FirebaseAuth.getInstance().getCurrentUser()==null?"":FirebaseAuth.getInstance().getCurrentUser().getUid());
        buyerRequest.setSeller_uid(userList.get(SpinnerStoreName.getSelectedItemPosition()).getUid());
        return buyerRequest;

    }



}