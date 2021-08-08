package com.example.digitalshop.Activities.Buyer.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;

import java.util.List;


public class CreateBuyerRequestFragment extends Fragment
{



    Spinner SpinnerStoreName;
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
}