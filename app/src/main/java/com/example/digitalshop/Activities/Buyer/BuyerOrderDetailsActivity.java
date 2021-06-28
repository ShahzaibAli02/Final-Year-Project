package com.example.digitalshop.Activities.Buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.digitalshop.Adapters.SliderAdapter;
import com.example.digitalshop.Enums.OrderStatus;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.Model.Product;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.google.firebase.Timestamp;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuyerOrderDetailsActivity extends AppCompatActivity
{


    Order order;
    TextView txtPrice;
    TextView txtProductName;
    TextView txtby;
    TextView txtPhone;
    com.smarteist.autoimageslider.SliderView sliderView;

    CircleImageView circleImageView1;
    CircleImageView circleImageView2;
    CircleImageView circleImageView3;
    View view1;
    View view2;
    TextView txtOrderCancelled;
    LinearLayout layoutOrderStatus;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_details);

        if(getIntent().getExtras()!=null)
        {
            order= (Order) getIntent().getExtras().getSerializable("order");
            initViews();
            setVals();
            setSlider();

        }

    }

    private void setVals ()
    {
        txtPrice.setText(String.format("PKR %s" , order.getProduct().getPrice()));
        txtProductName.setText(order.getProduct().getName());
        txtby.setText("By "+order.getProduct().getUploadername());
        txtPhone.setText("Seller Contact "+order.getProduct().getUploaderphone());




        if(order.getOrderStatus()==OrderStatus.CANCELLED)
        {
            txtOrderCancelled.setVisibility(View.VISIBLE);
            layoutOrderStatus.setVisibility(View.GONE);
        }
        else
        {
            if (order.getOrderStatus()==OrderStatus.PROCESSING)
            {
                changeProcessingColors();
            }
            else
            if (order.getOrderStatus()==OrderStatus.SHIPPED)
            {
                changeProcessingColors();
                changeShippedColors();
            }
            else
            if (order.getOrderStatus()==OrderStatus.DELIVERED)
            {
                changeProcessingColors();
                changeShippedColors();
                changeDeliveredColors();

                if(!order.getIsreviewed())
                {

                    Dialog dialog=Util.createDialog(getActivity(),R.layout.lyt_dialog_leave_review);
                    dialog.setCancelable(false);
                    RatingBar ratingBar=dialog.findViewById(R.id.ratingBar);
                    Button btnReview=dialog.findViewById(R.id.btnReview);
                    dialog.show();

                    btnReview.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick (View v)
                        {
                            dialog.dismiss();
                            Product product = order.getProduct();
                            product.setRatingcount(product.getRatingcount()+1);
                            product.setRating((product.getRating()+ratingBar.getRating())/product.getRatingcount());

                            order.setIsreviewed(true);
                            order.setProduct(product);

                            AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(getActivity());
                            progressDialog.show();
                            FireStoreDatabaseManager.updateOrderById(order , new DataBaseResult()
                            {
                                @Override
                                public void onResult (boolean error , String Message , Object data)
                                {

                                    if(!error)
                                    {
                                        FireStoreDatabaseManager.updateProductById(product , new DataBaseResult()
                                        {
                                            @Override
                                            public void onResult (boolean error , String Message , Object data) {

                                                progressDialog.dismiss();
                                                if(error)
                                                {
                                                    Util.showCustomToast(getActivity(),Message,error);
                                                }
                                                else
                                                {
                                                    Util.showCustomToast(getActivity(),"Thank You For Your Review",false);
                                                }
                                            }
                                        });

                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Util.showCustomToast(getActivity(),Message,error);
                                    }

                                }
                            });

                        }
                    });

                }
            }
        }

    }

    public  void  changeProcessingColors()
    {
        circleImageView1.setColorFilter(getResources().getColor(R.color.colorPrimary));
        view1.setBackgroundColor(getColor(R.color.colorPrimary));
    }
    public  void  changeShippedColors()
    {
        circleImageView2.setColorFilter(getResources().getColor(R.color.colorPrimary));
        view2.setBackgroundColor(getColor(R.color.colorPrimary));
    }
    public  void  changeDeliveredColors()
    {
        circleImageView3.setColorFilter(getResources().getColor(R.color.colorPrimary));
    }
    private void initViews ()
    {


        txtOrderCancelled=findViewById(R.id.txtOrderCancelled);
        layoutOrderStatus=findViewById(R.id.layoutOrderStatus);

        circleImageView1=findViewById(R.id.circleImageView1);
        circleImageView2=findViewById(R.id.circleImageView2);
        circleImageView3=findViewById(R.id.circleImageView3);

        view1=findViewById(R.id.view1);
        view2=findViewById(R.id.view2);

        txtPhone=findViewById(R.id.txtPhone);
        sliderView=findViewById(R.id.imageSlider);
        txtPrice=findViewById(R.id.txtProductPrice);
        txtProductName=findViewById(R.id.txtProductName);
        txtby=findViewById(R.id.txtby);
    }


    private void setSlider ()
    {


        List<Object> list=new ArrayList<>();
        for(String imageLink:order.getProduct().getImages())
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

    private Activity getActivity () {
        return  this;
    }


    public void onClickBack (View view) {
        finish();
    }
}