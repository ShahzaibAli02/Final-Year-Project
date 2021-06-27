package com.example.digitalshop.Activities.Seller.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.digitalshop.Activities.Seller.SellerDashBoard;
import com.example.digitalshop.Activities.Seller.SellerOrderDetailsActivity;
import com.example.digitalshop.Adapters.OrderAdapter;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.ClickListener;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

public class SellerOrdersFragment extends Fragment implements ClickListener
{


    List<Order> orderslist=new ArrayList<>();

    OrderAdapter orderAdapter;
    RecyclerView recyclerView;
    SpinKitView spin_kit;
    EditText edit_query;
    Object originalData;



    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_orders , container , false);
    }

    @Override
    public void onViewCreated (@NonNull View view , @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view , savedInstanceState);
        initViews(view);
        loadData();
        orderAdapter.notifyDataSetChanged();

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
                        orderslist.addAll((List<Order>)originalData);
                }
                else
                {
                    String query=edit_query.getText().toString();
                    List<Order> filteredList=new ArrayList<>();
                    for(Order event:orderslist)
                    {
                        if(event.getProduct().getName().toLowerCase().contains(query.toLowerCase()))
                        {
                            filteredList.add(event);

                        }
                    }
                    orderslist.clear();
                    orderslist.addAll(filteredList);
                }

                orderAdapter.notifyDataSetChanged();

            }
        });
    }

    private void loadData ()
    {



        FireStoreDatabaseManager.loadordersbyid(SharedPref.getUser(getContext()).getUid() , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data) {


                orderslist.clear();
                if(error)
                {

                    Util.showSnackBarMessage(getActivity(),Message);

                }
                else
                {
                    originalData=data;
                    orderslist.addAll((List<Order>)data);

                }
                orderAdapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
                spin_kit.setVisibility(orderslist.isEmpty()?View.VISIBLE:View.GONE);
                recyclerView.setVisibility(orderslist.isEmpty()?View.GONE:View.VISIBLE);



            }
        });


    }

    private void initViews (View view)
    {


        spin_kit=view.findViewById(R.id.spin_kit);
        recyclerView=view.findViewById(R.id.recyclerView);
        edit_query=view.findViewById(R.id.edit_query);

        orderAdapter =new OrderAdapter(orderslist,getActivity(),this);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(orderAdapter);
    }


    @Override
    public void onClicked (int position)
    {
        startActivity(new Intent(getActivity(), SellerOrderDetailsActivity.class).putExtra("order",orderslist.get(position)));
    }
}