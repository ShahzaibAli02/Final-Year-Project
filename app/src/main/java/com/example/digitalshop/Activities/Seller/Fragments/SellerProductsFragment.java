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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.digitalshop.Activities.Seller.SellerDashBoard;
import com.example.digitalshop.Adapters.ProductAdapter;
import com.example.digitalshop.Enums.OrderStatus;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.ClickListener;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.Model.Product;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;


public class SellerProductsFragment extends Fragment implements ClickListener
{


    List<Product> orderslist=new ArrayList<>();
    ProductAdapter eventAdapter;
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
                    orderslist.clear();
                    if(originalData!=null)
                    orderslist.addAll((List<Product>)originalData);
                }
                else
                {
                    String query=edit_query.getText().toString();
                    List<Product> filteredList=new ArrayList<>();
                    for(Product event:orderslist)
                    {
                        if(event.getName().toLowerCase().contains(query.toLowerCase()))
                        {
                            filteredList.add(event);

                        }
                    }
                    orderslist.clear();
                    orderslist.addAll(filteredList);
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
                orderslist.clear();
                if(!error)
                {
                    originalData=data;
                    orderslist.addAll((List<Product>)data);
                }

                eventAdapter.notifyDataSetChanged();
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

        eventAdapter=new ProductAdapter(orderslist,getActivity(),this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(eventAdapter);
    }


    @Override
    public void onClicked (int position)
    {
        ((SellerDashBoard)getActivity()).changeSelection(R.id.addproduct,true);
        ((SellerDashBoard)getActivity()).navigateTo(SellerAddProductFragment.newInstance(orderslist.get(position)));
    }
}