package com.example.digitalshop.Activities.Buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.digitalshop.Adapters.SliderAdapter;
import com.example.digitalshop.Enums.OrderStatus;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.Model.Product;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.Util;
import com.google.firebase.Timestamp;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import okio.Utf8;

public class BuyerProductDetailActivity extends AppCompatActivity
{


    Product product;
    TextView txtPrice;
    TextView txtProductName;
    TextView txtby;
    TextView txtDetail;
    TextView txtQuantity;
    TextView txtPhone;
    com.smarteist.autoimageslider.SliderView sliderView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_product_detail);

        if(getIntent().getExtras()!=null)
        {
            product= (Product) getIntent().getExtras().getSerializable("product");
            initViews();
            setVals();
            setSlider();
            FireStoreDatabaseManager.updateAnalyticVal(product.getUid(),"clicks",1L);
        }

    }

    private void setVals ()
    {
        txtPrice.setText(String.format("PKR %s" , product.getPrice()));
        txtProductName.setText(product.getName());
        txtby.setText("By "+product.getUploadername());
        txtQuantity.setText("1");
        txtDetail.setText(product.getDetail());
        txtPhone.setText("Seller Contact "+product.getUploaderphone());

    }
    private void initViews ()
    {
        txtPhone=findViewById(R.id.txtPhone);
        sliderView=findViewById(R.id.imageSlider);
        txtPrice=findViewById(R.id.txtProductPrice);
        txtProductName=findViewById(R.id.txtProductName);
        txtby=findViewById(R.id.txtby);
        txtDetail=findViewById(R.id.txtDetail);
        txtQuantity=findViewById(R.id.txtQuantity);
    }


    private void setSlider ()
    {


        List<Object> list=new ArrayList<>();
        for(String imageLink:product.getImages())
        {
            if(!imageLink.isEmpty())
            {
                list.add(imageLink);
            }
        }
        SliderAdapter adapter = new SliderAdapter(this,list,true);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(getColor(R.color.colorPrimary));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(1); //set scroll delay in seconds :
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

    }
    public void onClickAddToCart (View view)
    {
        if(Util.needsLogIn(this))
            return;


        Order order=new Order();
        order.setCreatedat(Timestamp.now().getSeconds());
        order.setUpdatedat(Timestamp.now().getSeconds());
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setQuantity(Integer.parseInt(txtQuantity.getText().toString()));
        order.setUser(SharedPref.getUser(getActivity()));
        order.setProduct(product);
        order.setTotalprice(order.getProduct().getPrice()*order.getQuantity());
        order.setSellerid(order.getProduct().getUid());
        order.setUserid(SharedPref.getUser(getActivity()).getUid());
        SharedPref.addInCart(getActivity(),order);
        Util.showSnackBar(this,"Added In Cart");
    }

    private Activity getActivity () {
        return  this;
    }

    public void onClickMinus (View view)
    {
        if (product != null)
        {

            int quantity=Integer.parseInt(txtQuantity.getText().toString());
            if(quantity>1)
            {
                txtQuantity.setText(String.valueOf(quantity-1));
            }

        }
    }

    public void onClickPlus (View view)
    {
        if (product != null)
        {

            int quantity=Integer.parseInt(txtQuantity.getText().toString());
            if(quantity < 5)
            {
                txtQuantity.setText(String.valueOf(quantity+1));
            }
            else
            {
                Util.showSnackBar(this,"Cannot add more then 5");
            }

        }
    }

    public void onClickBack (View view) {
        finish();
    }
}