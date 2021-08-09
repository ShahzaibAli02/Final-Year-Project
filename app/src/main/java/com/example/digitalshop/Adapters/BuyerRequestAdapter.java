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
import com.example.digitalshop.Model.BuyerRequest;
import com.example.digitalshop.Model.Product;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BuyerRequestAdapter extends RecyclerView.Adapter<BuyerRequestAdapter.ViewHolder> {
    private List<BuyerRequest>listData;
    Context context;
    public BuyerRequestAdapter (List<BuyerRequest> listData, Context context) {
        this.listData = listData;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lyt_buyer_request,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int k)
    {
        BuyerRequest buyerRequest=listData.get(holder.getAdapterPosition());
        holder.txtTitle.setText(buyerRequest.getTitle());
        holder.txtby.setText("By "+buyerRequest.getUser_name());
        holder.txtDetail.setText(buyerRequest.getRequest_discription());
        holder.txtPhone.setText(buyerRequest.getUser_phone());
        holder.btnCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v) {
                Util.openDialer(context,buyerRequest.getUser_phone());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {



        private TextView txtTitle;
        private TextView txtby;
        private TextView txtDetail;
        private TextView txtPhone;
        Button btnCall;

        public ViewHolder(View itemView)
        {
            super(itemView);

            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtDetail=itemView.findViewById(R.id.txtDetail);
            txtby=itemView.findViewById(R.id.txtby);
            txtPhone=itemView.findViewById(R.id.txtPhone);
            btnCall=itemView.findViewById(R.id.btnCall);

        }
    }



}
