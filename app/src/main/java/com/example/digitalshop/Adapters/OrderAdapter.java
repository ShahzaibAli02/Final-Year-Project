package com.example.digitalshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalshop.Interfaces.ClickListener;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order>listData;
    Context context;
    ClickListener clickListener;
    boolean isSeller=false;
    public OrderAdapter (List<Order> listData, Context context, ClickListener clickListener) {
        this.listData = listData;
        this.context=context;
        this.clickListener=clickListener;
        isSeller=SharedPref.getUser(context).getRole().equalsIgnoreCase("seller");

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_order,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        Order order=listData.get(i);
        holder.txtName.setText(order.getProduct().getName());
        holder.txtprice.setText(String.format("PKR %s" , order.getProduct().getPrice()));

        holder.txtprice.setText(String.format("PKR %s" , order.getProduct().getPrice()));
        holder.txtTotalPrice.setText(String.format("PKR %s" , order.getTotalprice()));
        holder.txtQuantity.setText(String.valueOf(order.getQuantity()));
        holder.txtStatus.setText(String.valueOf(order.getOrderStatus()));
        holder.ratingBar.setRating(order.getProduct().getRating());



        boolean isFvrt=SharedPref.isFavorite(context,order.getOrderid());
        int Color= isFvrt?R.color.Red:R.color.Silver;
        holder.imgFvrt.setColorFilter(ContextCompat.getColor(context, Color), android.graphics.PorterDuff.Mode.SRC_IN);
        Picasso.get().load(order.getProduct().getImages().get(0)).placeholder(R.drawable.logo).into(holder.imgproduct);
        holder.imgFvrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v)
            {
                SharedPref.saveFavourite(context,order.getOrderid(),!isFvrt);
                notifyDataSetChanged();
            }
        });
        holder.txtDetail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                clickListener.onClicked(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {


        private ImageView imgproduct;
        private ImageView imgFvrt;
        private TextView txtprice;
        private TextView txtName;
        private TextView txtDetail;

        private TextView txtQuantity;
        private TextView txtTotalPrice;
        private TextView txtStatus;

        private RatingBar ratingBar;

        public ViewHolder(View itemView)
        {
            super(itemView);

            imgproduct=itemView.findViewById(R.id.imgproduct);
            imgFvrt=itemView.findViewById(R.id.imgFavourite);
            txtprice=itemView.findViewById(R.id.txtPrice);
            txtName=itemView.findViewById(R.id.txtName);
            txtDetail=itemView.findViewById(R.id.txtDetail);
            ratingBar=itemView.findViewById(R.id.ratingBar);

            txtQuantity=itemView.findViewById(R.id.txtQuantity);
            txtTotalPrice=itemView.findViewById(R.id.txtTotalPrice);
            txtStatus=itemView.findViewById(R.id.txtStatus);

        }
    }



}
