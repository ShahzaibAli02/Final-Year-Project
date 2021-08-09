package com.example.digitalshop.Activities.Seller.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.digitalshop.Adapters.BuyerRequestAdapter;
import com.example.digitalshop.Adapters.OrderAdapter;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.BuyerRequest;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.Util;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class SellerBuyerRequestFragment extends Fragment
{

    List <BuyerRequest> orderslist=new ArrayList <>();

    BuyerRequestAdapter buyerRequestAdapter;
    RecyclerView recyclerView;
    SpinKitView spin_kit;
    EditText edit_query;
    Object originalData;
    LinearLayout lyt_empty;
    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_buyer_request , container , false);
    }

    @Override
    public void onViewCreated (@NonNull View view , @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view , savedInstanceState);
        initViews(view);
        loadData();

        edit_query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s , int start , int count , int after) {

            }

            @Override
            public void onTextChanged (CharSequence s , int start , int before , int count) {

            }

            @Override
            public void afterTextChanged (Editable s)
            {
                if(edit_query.getText().toString().isEmpty())
                {
                    orderslist.clear();
                    if(originalData!=null)
                        orderslist.addAll((List<BuyerRequest>)originalData);
                }
                else
                {
                    String query=edit_query.getText().toString();
                    List<BuyerRequest> filteredList=new ArrayList<>();
                    for(BuyerRequest buyerRequest:(List<BuyerRequest>)originalData)
                    {
                        if(buyerRequest.getTitle().toLowerCase().contains(query.toLowerCase()) || buyerRequest.getRequest_discription().toLowerCase().contains(query.toLowerCase()))
                        {
                            filteredList.add(buyerRequest);

                        }
                    }
                    orderslist.clear();
                    orderslist.addAll(filteredList);
                }

                buyerRequestAdapter.notifyDataSetChanged();

            }
        });
    }

    private void loadData ()
    {



        FireStoreDatabaseManager.getAllBuyerRequests(FirebaseAuth.getInstance().getCurrentUser().getUid() , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data) {


                orderslist.clear();
                if(!error)
                {
                    originalData=data;
                    orderslist.addAll((List<BuyerRequest>)data);
                }

                buyerRequestAdapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
                spin_kit.setVisibility(View.GONE);
                lyt_empty.setVisibility(orderslist.isEmpty()?View.VISIBLE:View.GONE);
                recyclerView.setVisibility(orderslist.isEmpty()?View.GONE:View.VISIBLE);



            }
        });


    }

    private void initViews (View view)
    {


        lyt_empty=view.findViewById(R.id.lyt_empty);
        spin_kit=view.findViewById(R.id.spin_kit);
        recyclerView=view.findViewById(R.id.recyclerView);
        edit_query=view.findViewById(R.id.edit_query);

        buyerRequestAdapter =new BuyerRequestAdapter(orderslist,getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(buyerRequestAdapter);
    }



}