package com.example.digitalshop.Activities.Buyer.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.digitalshop.Activities.Buyer.BuyerOrderDetailsActivity;
import com.example.digitalshop.Adapters.OrderAdapter;
import com.example.digitalshop.Enums.OrderStatus;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.ClickListener;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.github.ybq.android.spinkit.SpinKitView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class BuyerOrdersFragment extends Fragment implements View.OnClickListener
{


    ImageView imgActiveOrders;
    ImageView imgPastOrders;

    LinearLayout lyt_activeorders;
    LinearLayout lyt_pastorders;

    RecyclerView recyclerViewPastOrders;
    RecyclerView recyclerViewActiveOrders;

    SpinKitView spin_kit_active_orders;
    SpinKitView spin_kit_past_orders;

    TextView txtEmptyActiveOrders;
    TextView txtEmptyPastOrders;


    List<Order> activeOrderList=new ArrayList<>();
    List<Order> pastOrderList=new ArrayList<>();

    OrderAdapter activeOrderAdapter;
    OrderAdapter pastOrderAdapter;
    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_buyer_orders , container , false);
    }

    @Override
    public void onViewCreated (@NonNull @NotNull View view , @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        initViews(view);
    }

    private void initViews (View view)
    {

        imgActiveOrders=view.findViewById(R.id.imgActiveOrders);
        imgPastOrders=view.findViewById(R.id.imgPastOrders);

        lyt_activeorders=view.findViewById(R.id.lyt_activeorders);
        lyt_pastorders=view.findViewById(R.id.lyt_pastorders);

        recyclerViewPastOrders=view.findViewById(R.id.recyclerViewPastOrders);
        recyclerViewActiveOrders=view.findViewById(R.id.recyclerViewActiveOrders);

        spin_kit_active_orders=view.findViewById(R.id.spin_kit_active_orders);
        spin_kit_past_orders=view.findViewById(R.id.spin_kit_past_orders);

        txtEmptyActiveOrders=view.findViewById(R.id.txtEmptyActiveOrders);
        txtEmptyPastOrders=view.findViewById(R.id.txtEmptyPastOrders);



        activeOrderAdapter=new OrderAdapter(activeOrderList , getActivity() , new ClickListener()
        {
            @Override
            public void onClicked (int position) {

                startActivity(new Intent(getActivity(), BuyerOrderDetailsActivity.class).putExtra("order",activeOrderList.get(position)));
            }
        });
        pastOrderAdapter=new OrderAdapter(pastOrderList , getActivity() , new ClickListener()
        {
            @Override
            public void onClicked (int position) {
                startActivity(new Intent(getActivity(), BuyerOrderDetailsActivity.class).putExtra("order",pastOrderList.get(position)));

            }
        });


        recyclerViewActiveOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPastOrders.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewActiveOrders.setAdapter(activeOrderAdapter);
        recyclerViewPastOrders.setAdapter(pastOrderAdapter);

        imgActiveOrders.setTag(false);
        imgPastOrders.setTag(false);
        imgActiveOrders.setOnClickListener(this);
        imgPastOrders.setOnClickListener(this);
    }

    @Override
    public void onClick (View v)
    {

        if(v==imgActiveOrders)
        {


            imgActiveOrders.setImageResource((boolean)imgActiveOrders.getTag()?R.drawable.ic_not_expanded:R.drawable.ic_expanded);

            if((boolean)imgActiveOrders.getTag())
            {
                lyt_activeorders.setVisibility(View.GONE);
            }
            else
            {
                lyt_activeorders.setVisibility(View.VISIBLE);
                loadActiveOrders();
            }
            imgActiveOrders.setTag(!(boolean)imgActiveOrders.getTag());

        }


        if(v==imgPastOrders)
        {


            imgPastOrders.setImageResource((boolean)imgPastOrders.getTag()?R.drawable.ic_not_expanded:R.drawable.ic_expanded);

            if((boolean)imgPastOrders.getTag())
            {
                lyt_pastorders.setVisibility(View.GONE);
            }
            else
            {
                lyt_pastorders.setVisibility(View.VISIBLE);
                loadPastOrders();
            }
            imgPastOrders.setTag(!(boolean)imgPastOrders.getTag());

        }

    }

    private void loadActiveOrders ()
    {

        recyclerViewActiveOrders.setVisibility(View.GONE);
        spin_kit_active_orders.setVisibility(View.VISIBLE);
        txtEmptyActiveOrders.setVisibility(View.GONE);

        FireStoreDatabaseManager.loadOrderByIdAndStatus(SharedPref.getUser(getActivity()).getUid() , OrderStatus.PROCESSING , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data)
            {

                activeOrderList.clear();
                if(!error)
                {
                    activeOrderList.addAll(( List<Order> )data);
                }
                spin_kit_active_orders.setVisibility(View.GONE);
                txtEmptyActiveOrders.setVisibility(activeOrderList.isEmpty()?View.VISIBLE:View.GONE);
                recyclerViewActiveOrders.setVisibility(activeOrderList.isEmpty()?View.GONE:View.VISIBLE);
                activeOrderAdapter.notifyDataSetChanged();
            }
        });


    }

    private void loadPastOrders ()
    {

        recyclerViewPastOrders.setVisibility(View.GONE);
        spin_kit_past_orders.setVisibility(View.VISIBLE);
        txtEmptyPastOrders.setVisibility(View.GONE);

        FireStoreDatabaseManager.loadOrderByIdAndStatus(SharedPref.getUser(getActivity()).getUid() , OrderStatus.DELIVERED , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data)
            {

                pastOrderList.clear();
                if(!error)
                {
                    pastOrderList.addAll(( List<Order> )data);
                }
                spin_kit_past_orders.setVisibility(View.GONE);
                txtEmptyPastOrders.setVisibility(pastOrderList.isEmpty()?View.VISIBLE:View.GONE);
                recyclerViewPastOrders.setVisibility(pastOrderList.isEmpty()?View.GONE:View.VISIBLE);
                pastOrderAdapter.notifyDataSetChanged();
            }
        });


    }
}