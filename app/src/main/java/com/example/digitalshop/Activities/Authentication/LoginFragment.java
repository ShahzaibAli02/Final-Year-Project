package com.example.digitalshop.Activities.Authentication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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

import com.example.digitalshop.Activities.Seller.SellerDashBoard;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;


public class LoginFragment extends Fragment implements View.OnClickListener {





    EditText editTextEmail,editTextPin;

    TextView txtforgotPass;
    TextView txtSignUp;
    Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view)
    {


        editTextEmail=view.findViewById(R.id.editTextEmail);
        editTextPin=view.findViewById(R.id.editTextPin);


        btnLogin=view.findViewById(R.id.btnLogin);
        txtforgotPass=view.findViewById(R.id.txtforgotPass);
        txtSignUp=view.findViewById(R.id.txtSignUp);



        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        txtforgotPass.setOnClickListener(this);

    }


    @Override
    public void onClick(View v)
    {

        if(v==txtforgotPass)
        {
            showForgotMessageDialog();
        }
        if(v==txtSignUp)
        {

            ((Login_SignUp)getActivity()).txtSignUp.performClick();
        }

        if(v==btnLogin)
        {

            if(!Util.isEmailValid(editTextEmail.getText().toString()))
            {
                editTextEmail.setError("Invalid Email");
                editTextEmail.requestFocus();
                return;
            }

            if(editTextPin.getText().toString().isEmpty())
            {
                editTextPin.setError("Required field");
                editTextPin.requestFocus();
                return;
            }

            authenticateUser();
        }


    }

    private void authenticateUser ()
    {

        AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(getActivity());
        progressDialog.show();

        FireStoreDatabaseManager.authenticateUser(editTextEmail.getText().toString() , editTextPin.getText().toString() , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data) {


                progressDialog.dismiss();
                if(error)
                {
                    Util.showCustomToast(getActivity(),Message,true);
                }
                else
                {

                    User user=(User) data;
                    SharedPref.saveUser(getActivity(),user);


                    if(user.getRole().equalsIgnoreCase("seller"))
                    {
                        getActivity().finish();
                        getActivity().startActivity(new Intent(getActivity(), SellerDashBoard.class));

                    }
                    else
                    {
                        //TODO
                        Util.showSnackBar(getActivity(),"USER ");
                    }


                }

            }
        });
    }

    private void showForgotMessageDialog ()
    {
        Dialog dialog= Util.createDialog(getActivity(),R.layout.lyt_dialog_foget_pass);

        EditText editTextEmail=dialog.findViewById(R.id.editTextEmail);
        Button btnSubmit=dialog.findViewById(R.id.btn);

        btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v) {

                if(!editTextEmail.getText().toString().isEmpty()  && Util.isEmailValid(editTextEmail.getText().toString()))
                {
                    dialog.dismiss();

                    FirebaseAuth.getInstance().sendPasswordResetEmail(editTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete (@NonNull @NotNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Util.showSnackBar(getActivity(),"Reset Email is sent to "+editTextEmail.getText().toString());
                            }
                            else
                            {
                                Util.showSnackBar(getActivity(),task.getException().getMessage());
                            }

                        }
                    });


                }
                else
                {
                    editTextEmail.setError("Invalid Email");
                    editTextEmail.requestFocus();
                }

            }
        });


        dialog.show();

    }


}