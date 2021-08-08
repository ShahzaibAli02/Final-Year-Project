package com.example.digitalshop.Activities.Buyer.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.digitalshop.Activities.Buyer.BuyerDashBoardActivity;
import com.example.digitalshop.Activities.Buyer.BuyerProductDetailActivity;
import com.example.digitalshop.Adapters.BuyerHomeAdapter;
import com.example.digitalshop.Adapters.ProductAdapter;
import com.example.digitalshop.Adapters.SliderAdapter;
import com.example.digitalshop.Enums.PriceFilter;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.ClickListener;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Interfaces.LocationListener;
import com.example.digitalshop.Model.Product;
import com.example.digitalshop.R;
import com.example.digitalshop.Utils.LocationProvider;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.slider.Slider;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BuyerHomeFragment extends Fragment implements ClickListener
{



    List<Product> orderslist=new ArrayList<>();
    BuyerHomeAdapter buyerHomeAdapter;
    RecyclerView recyclerView;
    SpinKitView spin_kit;
    EditText edit_query;
    Object originalData;
    com.smarteist.autoimageslider.SliderView sliderView;
    ImageView imgCart;
    CardView cardFilter;
    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buyer_home , container , false);
    }

    @Override
    public void onViewCreated (@NonNull @NotNull View view , @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        initViews(view);
        loadData();
        setSlider();

        imgCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v) {

                ((BuyerDashBoardActivity)getActivity()).changeSelection(R.id.cart,true);
                ((BuyerDashBoardActivity)getActivity()).navigateTo(new BuyerCartFragment());
            }
        });
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
                        if(event.getCategory().toLowerCase().contains(query.toLowerCase()) || event.getName().toLowerCase().contains(query.toLowerCase()))
                        {
                            filteredList.add(event);
                        }
                    }
                    orderslist.clear();
                    orderslist.addAll(filteredList);
                }

                buyerHomeAdapter.notifyDataSetChanged();

            }
        });


        cardFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {

                Dialog dialog=Util.getDialog(getActivity(),R.layout.lyt_dialog_filter);

                TextView txtkms=dialog.findViewById(R.id.txtKms);
                Slider sliderKms=dialog.findViewById(R.id.slider);
                Spinner spinnerPrice=dialog.findViewById(R.id.spinnerSortPrice);
                Button btnApply=dialog.findViewById(R.id.btnApply);

                sliderKms.addOnChangeListener(new Slider.OnChangeListener()
                {
                    @Override
                    public void onValueChange (@NonNull Slider slider , float value , boolean fromUser) {
                        txtkms.setText(String.format("( %s Km )" , (int)value));
                    }
                });


                btnApply.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick (View v) {
                        dialog.dismiss();
                        applyFilter(getPriceFilterType(spinnerPrice.getSelectedItemPosition()),sliderKms.getValue());
                    }
                });

                dialog.show();

            }
        });
    }

    private PriceFilter getPriceFilterType (int selectedItemPosition)
    {

        if(selectedItemPosition==0)
                return  PriceFilter.NO_FILTER;

        if(selectedItemPosition==1)
            return  PriceFilter.HIGH;

        if(selectedItemPosition==2)
            return  PriceFilter.LOW;

        return PriceFilter.NO_FILTER;
    }



    private void applyFilter (PriceFilter priceFilter , float filterkms)
    {

        AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(getActivity());
        progressDialog.show();
        Util.loadLocation((AppCompatActivity) getActivity() , new LocationListener()
        {
            @Override
            public void onLocationLoad (LatLng mylatLng)
            {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                Location mylocation = new Location("mylocation");
                mylocation.setLatitude(mylatLng.latitude);
                mylocation.setLongitude(mylatLng.longitude);


                List <Product> filteredList = new ArrayList <>();
                for (Product event : orderslist)
                {
                    Location locationShop = new Location("ShopLocation");
                    locationShop.setLatitude(event.getLat());
                    locationShop.setLongitude(event.getLng());


                    if (getDistance(locationShop , mylocation) <= filterkms)
                    {
                        filteredList.add(event);
                    }


                }

                if(!filteredList.isEmpty() && priceFilter!=PriceFilter.NO_FILTER)
                {

                    filteredList.sort(new Comparator <Product>()
                    {
                        @Override
                        public int compare (Product o1 , Product o2) {

                            if (priceFilter == PriceFilter.LOW)
                            {
                                return o1.getPrice().compareTo(o2.getPrice());
                            }
                            else
                                return o2.getPrice().compareTo(o1.getPrice());

                        }
                    });


                }
                orderslist.clear();
                orderslist.addAll(filteredList);
                buyerHomeAdapter.notifyDataSetChanged();
            }
        });


    }

    private float getDistance (Location locationShop , Location mylocation)
    {
        return  locationShop.distanceTo(mylocation)/1000;
    }

    private void loadData ()
    {




        FireStoreDatabaseManager.loadAllProducts( new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data) {

                //  progressDialog.dismiss();
                orderslist.clear();
                if(error)
                {

                    Util.showSnackBarMessage(getActivity(),Message);

                }
                else
                {
                    originalData=data;
                    orderslist.addAll((List<Product>)data);
                    for(Product product:orderslist)
                        FireStoreDatabaseManager.updateAnalyticVal(product.getUid(),"impressions",1L);
                }
                buyerHomeAdapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
                spin_kit.setVisibility(View.GONE);
                recyclerView.setVisibility(orderslist.isEmpty()?View.GONE:View.VISIBLE);





            }
        });


    }

    private void initViews (View view)
    {

        imgCart=view.findViewById(R.id.imgCart);
        sliderView=view.findViewById(R.id.imageSlider);
        spin_kit=view.findViewById(R.id.spin_kit);
        recyclerView=view.findViewById(R.id.recyclerView);
        edit_query=view.findViewById(R.id.edit_query);
        cardFilter=view.findViewById(R.id.cardFilter);

        buyerHomeAdapter =new BuyerHomeAdapter(orderslist,getActivity(),this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(buyerHomeAdapter);
    }


    @Override
    public void onClicked (int position)
    {
        startActivity(new Intent(getActivity(), BuyerProductDetailActivity.class).putExtra("product",orderslist.get(position)));
    }

    private void setSlider ()
    {


        List<Object> list=new ArrayList<>();

        list.add(R.drawable.slider1);
        list.add(R.drawable.slider2);
        list.add(R.drawable.slider3);
        list.add(R.drawable.slider4);
        list.add(R.drawable.slider5);
        SliderAdapter adapter = new SliderAdapter(getActivity(),list,false);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(getActivity().getColor(R.color.white));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(1); //set scroll delay in seconds :
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

    }



}