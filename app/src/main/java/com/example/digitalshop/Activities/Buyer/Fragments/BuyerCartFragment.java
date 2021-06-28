package com.example.digitalshop.Activities.Buyer.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.digitalshop.Activities.Buyer.BuyerDashBoardActivity;
import com.example.digitalshop.Adapters.CartAdapter;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.ClickListener;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.SwipeToDeleteCallback;
import com.example.digitalshop.Utils.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class BuyerCartFragment extends Fragment implements ClickListener, View.OnClickListener
{

    RecyclerView recyclerView;
    LinearLayout lyt_empty;
    List<Order> orderList;
    CartAdapter cartAdapter;
    TextView txtTotalPrice;
    ConstraintLayout layout_checkout;
    LinearLayout layout_bottom;
    int count=0;
    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buyer_cart , container , false);
    }

    @Override
    public void onViewCreated (@NonNull @NotNull View view , @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        initViews(view);
        loadData();

    }

    private void loadData ()
    {
        orderList.clear();
        orderList.addAll(SharedPref.getCartItems(getActivity()));

        layout_bottom.setVisibility(orderList.isEmpty()?View.GONE:View.VISIBLE);
        recyclerView.setVisibility(orderList.isEmpty()?View.GONE:View.VISIBLE);
        lyt_empty.setVisibility(orderList.isEmpty()?View.VISIBLE:View.GONE);
        updateTotalPrice();
        ((BuyerDashBoardActivity)getActivity()).updateCartBadge();
    }

    private void initViews (View view)
    {

        orderList=new ArrayList<>();
        txtTotalPrice=view.findViewById(R.id.txtTotalPrice);
        layout_bottom=view.findViewById(R.id.layout_bottom);
        layout_checkout=view.findViewById(R.id.layout_checkout);
        recyclerView=view.findViewById(R.id.recyclerView);
        lyt_empty=view.findViewById(R.id.lyt_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartAdapter=new CartAdapter(orderList,getActivity(),this);
        recyclerView.setAdapter(cartAdapter);

        layout_checkout.setOnClickListener(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(cartAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClicked (int position)
    {
        loadData();
    }
    public  void  updateTotalPrice()
    {
        Double totalPrice=0.0;
        for(Order order:orderList)
            totalPrice+=order.getTotalprice();

        txtTotalPrice.setText(String.format("Rs %.1f" , totalPrice.floatValue()));

    }

    @Override
    public void onClick (View v)
    {

        count=0;
        int total=orderList.size();

        if(v==layout_checkout)
        {

            AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(getActivity());
            progressDialog.show();
            for(Order order:orderList)
            {
                FireStoreDatabaseManager.addOrder(order , new DataBaseResult()
                {
                    @Override
                    public void onResult (boolean error , String Message , Object data)
                    {
                        count++;


                        if(count==total)
                        {
                            progressDialog.dismiss();
                            Util.showCustomToast(getActivity(),Message,error);
                            if(!error)
                            {
                                SharedPref.clearCart(getActivity());
                                loadData();
                            }
                        }

                    }
                });
            }

        }
    }
}