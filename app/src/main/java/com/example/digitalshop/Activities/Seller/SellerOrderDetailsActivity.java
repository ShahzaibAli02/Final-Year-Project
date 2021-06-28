package com.example.digitalshop.Activities.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.digitalshop.Enums.OrderStatus;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

public class SellerOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener
{


    ImageView imgproduct;
    TextView txtProductName;
    TextView txtAddress;
    TextView txtProductPrice;
    TextView txtDate;
    TextView txtTime;
    TextView txtBuyerName;
    TextView txtBuyerPhone;
    TextView txtBuyerAddress;
    TextView txtProductQuantity;
    TextView txtProductTotalPrice;
    TextView txtProductStatus;
    TextView txtProductDesc;
    TextView txtChangeStatus;



    Order order;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_details);
        initViews();
        if(getIntent().getExtras()!=null)
        {
            order= (Order) getIntent().getExtras().getSerializable("order");
            setVals();
            txtChangeStatus.setOnClickListener(this);
        }
    }

    private void setVals ()
    {
        Picasso.get().load(order.getProduct().getImages().get(0)).placeholder(R.drawable.logo).into(imgproduct);

        txtProductName.setText(order.getProduct().getName());
        txtAddress.setText(order.getProduct().getAddress());
        txtProductPrice.setText(String.format("PKR %s" , order.getProduct().getPrice()));
        txtDate.setText(order.getFormatedDate());
        txtTime.setText(order.getFormatedTime());
        txtBuyerName.setText(order.getUser().getName());
        txtBuyerPhone.setText(order.getUser().getPhone());
        txtBuyerAddress.setText(order.getUser().getAddress());
        txtProductQuantity.setText(String.valueOf(order.getQuantity()));
        txtProductTotalPrice.setText(String.format("PKR %s" , order.getTotalprice()));

        txtProductStatus.setText(String.valueOf(order.getOrderStatus()));
        txtProductDesc.setText(order.getProduct().getDetail());

    }

    private void initViews ()
    {

        imgproduct=findViewById(R.id.imgproduct);
        txtProductName=findViewById(R.id.txtProductName);
        txtAddress=findViewById(R.id.txtAddress);
        txtProductPrice=findViewById(R.id.txtProductPrice);
        txtDate=findViewById(R.id.txtDate);
        txtTime=findViewById(R.id.txtTime);
        txtBuyerName=findViewById(R.id.txtBuyerName);
        txtBuyerPhone=findViewById(R.id.txtBuyerPhone);
        txtBuyerAddress=findViewById(R.id.txtBuyerAddress);
        txtProductQuantity=findViewById(R.id.txtProductQuantity);
        txtProductTotalPrice=findViewById(R.id.txtProductTotalPrice);
        txtProductStatus=findViewById(R.id.txtProductStatus);
        txtProductDesc=findViewById(R.id.txtProductDesc);
        txtChangeStatus=findViewById(R.id.txtChangeStatus);

    }

    public void onClickBack (View view) {
        finish();
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick (View v)
    {
        if(v==txtChangeStatus)
        {

            if(order.getOrderStatus()==OrderStatus.DELIVERED || order.getOrderStatus()==OrderStatus.CANCELLED)
            {
                Util.showCustomToast(this,"You cannot change already delivered or Cancelled order state",true);
                return;
            }
            Dialog dialog = Util.createDialog(this , R.layout.lyt_dialog_change_status);
            Spinner spinnerStatus=dialog.findViewById(R.id.spinnerStatus);
            Button btnUpdate=dialog.findViewById(R.id.btnUpdate);
            btnUpdate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View v)
                {
                    dialog.dismiss();
                    OrderStatus orderstatus = OrderStatus.valueOf(spinnerStatus.getSelectedItem().toString());

                    if(spinnerStatus.getSelectedItem().toString().equalsIgnoreCase("DELIVERED"))
                        FireStoreDatabaseManager.updateAnalyticVal(SharedPref.getUser(SellerOrderDetailsActivity.this).getUid(),"earning",order.getTotalprice().longValue());
                    order.setUpdatedat(Timestamp.now().getSeconds());
                    order.setOrderStatus(orderstatus);
                    updateOrder();
                }
            });
            dialog.show();
        }
    }

    private void updateOrder ()
    {
        AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(this);
        progressDialog.show();
        FireStoreDatabaseManager.updateOrderById(order , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data) {
                progressDialog.dismiss();
                Util.showCustomToast(SellerOrderDetailsActivity.this,Message,error);

                if(!error)
                    setVals();
            }
        });
    }
}