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

import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.ClickListener;
import com.example.digitalshop.Model.Product;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BuyerHomeAdapter extends RecyclerView.Adapter<BuyerHomeAdapter.ViewHolder> {
    private List<Product>listData;
    Context context;
    ClickListener clickListener;
    public BuyerHomeAdapter (List<Product> listData, Context context, ClickListener clickListener) {
        this.listData = listData;
        this.context=context;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lyt_product,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int k)
    {
        Product product=listData.get(holder.getAdapterPosition());
        FireStoreDatabaseManager.updateAnalyticVal(product.getUid(),"views",1L);
        holder.txtName.setText(product.getName());
        holder.txtby.setText(String.format("By %s" , product.getUploadername()));
        holder.txtprice.setText(String.format("PKR %s" , product.getPrice()));
        holder.txtDetail.setText(product.getDetail());

        holder.btn.setText("Buy");

        holder.ratingBar.setRating(product.getRating());
        boolean isFvrt=SharedPref.isFavorite(context,product.getId());
        int Color= isFvrt?R.color.Red:R.color.Silver;
        holder.imgFvrt.setColorFilter(ContextCompat.getColor(context, Color), android.graphics.PorterDuff.Mode.SRC_IN);
        Picasso.get().load(product.getImages().get(0)).placeholder(R.drawable.logo).into(holder.imgEvent);


        if(product.getDistance()!=null && !product.getDistance().isEmpty())
        {
            holder.txtDistance.setVisibility(View.VISIBLE);
            holder.txtDistance.setText(product.getDistance());
        }
        else
        {
            holder.txtDistance.setVisibility(View.GONE);
            holder.txtDistance.setText(null);
        }
        holder.imgFvrt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {

                if(SharedPref.getUser(context)==null)
                {
                    Util.showSnackBarMessage((Activity) context ,"Login To Mark As Favorite");
                    return;
                }
                SharedPref.saveFavourite(context,product.getId(),!isFvrt);
                notifyDataSetChanged();
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                clickListener.onClicked(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {


        private ImageView imgEvent;
        private ImageView imgFvrt;
        private TextView txtby;
        private TextView txtprice;
        private TextView txtName;
        private TextView txtDetail;
        private TextView txtDistance;
        private RatingBar ratingBar;
        Button btn;

        public ViewHolder(View itemView)
        {
            super(itemView);

            txtDistance=itemView.findViewById(R.id.txtDistance);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            imgEvent=itemView.findViewById(R.id.imgEvent);
            imgFvrt=itemView.findViewById(R.id.imgFavourite);
            txtprice=itemView.findViewById(R.id.txtPrice);
            txtName=itemView.findViewById(R.id.txtName);
            txtDetail=itemView.findViewById(R.id.txtDetail);
            txtby=itemView.findViewById(R.id.txtby);
            btn=itemView.findViewById(R.id.btn);

        }
    }



}
