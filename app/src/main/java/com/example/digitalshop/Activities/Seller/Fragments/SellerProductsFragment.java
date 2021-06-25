package com.example.digitalshop.Activities.Seller.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.digitalshop.Adapters.ProductAdapter;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.ClickListener;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.Product;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;


public class SellerProductsFragment extends Fragment implements ClickListener
{


    List<Product> eventList=new ArrayList<>();
    ProductAdapter eventAdapter;
    RecyclerView recyclerView;
    SpinKitView spin_kit;
    EditText edit_query;
    Object originalData;
    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_products , container , false);
    }

    @Override
    public void onViewCreated (@NonNull View view , @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view , savedInstanceState);
        initViews(view);
        loadData();
        eventAdapter.notifyDataSetChanged();

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
                    eventList.clear();
                    if(originalData!=null)
                    eventList.addAll((List<Product>)originalData);
                }
                else
                {
                    String query=edit_query.getText().toString();
                    List<Product> filteredList=new ArrayList<>();
                    for(Product event:eventList)
                    {
                        if(event.getName().toLowerCase().contains(query.toLowerCase()))
                        {
                            filteredList.add(event);

                        }
                    }
                    eventList.clear();
                    eventList.addAll(filteredList);
                }

                eventAdapter.notifyDataSetChanged();

            }
        });
    }

    private void loadData ()
    {


        AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(getActivity());
       // progressDialog.show();

        FireStoreDatabaseManager.loadProductsbyid(SharedPref.getUser(getContext()).getUid() , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data) {

              //  progressDialog.dismiss();
                eventList.clear();
                if(error)
                {

                    Util.showSnackBarMessage(getActivity(),Message);

                }
                else
                {
                    originalData=data;
                    eventList.addAll((List<Product>)data);
                    eventList.addAll((List<Product>)data);
                    eventList.addAll((List<Product>)data);
                }
                eventAdapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
                spin_kit.setVisibility(eventList.isEmpty()?View.VISIBLE:View.GONE);
                recyclerView.setVisibility(eventList.isEmpty()?View.GONE:View.VISIBLE);
            }
        });


    }

    private void initViews (View view)
    {


        spin_kit=view.findViewById(R.id.spin_kit);
        recyclerView=view.findViewById(R.id.recyclerView);
        edit_query=view.findViewById(R.id.edit_query);

        eventAdapter=new ProductAdapter(eventList,getActivity(),this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(eventAdapter);
    }


    @Override
    public void onClicked (int position)
    {

    }
}