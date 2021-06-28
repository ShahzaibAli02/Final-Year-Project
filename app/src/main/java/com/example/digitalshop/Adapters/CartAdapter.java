package com.example.digitalshop.Adapters;

import android.app.Activity;
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
import com.example.digitalshop.Utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Order>listData;
    Context context;
    ClickListener clickListener;
    public CartAdapter (List<Order> listData, Context context, ClickListener clickListener)
    {
        this.listData = listData;
        this.context=context;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lyt_cart_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        Order order=listData.get(i);
        holder.txtName.setText(order.getProduct().getName());
        holder.txtprice.setText(String.format("Rs %s" , order.getProduct().getPrice()));

        holder.txtTotalPrice.setText(String.format("%.1f" , order.getTotalprice().floatValue()));
        holder.txtQuantity.setText(String.valueOf(order.getQuantity()));
        Picasso.get().load(order.getProduct().getImages().get(0)).placeholder(R.drawable.logo).into(holder.imgproduct);

        holder.btnPlus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {

                int quantity=order.getQuantity();
                if(quantity < 5)
                {
                    order.setQuantity(quantity+1);
                    order.setTotalprice(order.getProduct().getPrice()*order.getQuantity());
                    listData.set(i,order);
                    notifyDataSetChanged();
                    SharedPref.updateCart(context,listData);
                    clickListener.onClicked(i);
                }
                else
                {
                    Util.showSnackBar((Activity) context ,"Cannot add more then 5");
                }
            }

        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                int quantity=order.getQuantity();
                if(quantity >1)
                {
                    order.setQuantity(quantity-1);
                    order.setTotalprice(order.getProduct().getPrice()*order.getQuantity());
                    listData.set(i,order);
                    SharedPref.updateCart(context,listData);
                    notifyDataSetChanged();
                    clickListener.onClicked(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Context getContext () {
        return context;
    }

    public void deleteTask (int position)
    {
        listData.remove(position);
        SharedPref.updateCart(context,listData);
        notifyDataSetChanged();
        clickListener.onClicked(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {


        private ImageView imgproduct;
        private TextView txtprice;
        private TextView txtName;
        private TextView txtQuantity;
        private TextView txtTotalPrice;
        private Button btnPlus;
        private  Button btnMinus;


        public ViewHolder(View itemView)
        {
            super(itemView);


            btnPlus=itemView.findViewById(R.id.btnPlus);
            btnMinus=itemView.findViewById(R.id.btnMinus);

            imgproduct=itemView.findViewById(R.id.imgproduct);
            txtprice=itemView.findViewById(R.id.txtPrice);
            txtName=itemView.findViewById(R.id.txtName);

            txtQuantity=itemView.findViewById(R.id.txtQuantity);
            txtTotalPrice=itemView.findViewById(R.id.txtTotalPrice);

        }
    }



}
