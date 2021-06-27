package com.example.digitalshop.Activities.Seller.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Model.Analytics;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.Util;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SellerHomeFragment extends Fragment
{


    TextView txtImpressions;
    TextView txtViews;
    TextView txtClicks;
    CircleImageView circleImageView;
    TextView txtUserName;
    LineChart chart;
    List<Order> orderslist=new ArrayList<>();
    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_home , container , false);
    }

    @Override
    public void onViewCreated (@NonNull @NotNull View view , @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        chart = view.findViewById(R.id.linechart);
        circleImageView=view.findViewById(R.id.circleImageView);
        txtUserName=view.findViewById(R.id.txtUserName);
        txtImpressions=view.findViewById(R.id.txtImpressions);
        txtViews=view.findViewById(R.id.txtViews);
        txtClicks=view.findViewById(R.id.txtClicks);



        readAnalytics();
        loadData();

    }


    private void readAnalytics ()
    {

        User user = SharedPref.getUser(getActivity());
        Picasso.get().load(user.getImage()).placeholder(R.drawable.loading_gif).into(circleImageView);
        txtUserName.setText("Welcome Back!\n"+user.getName());

        FireStoreDatabaseManager.readAnalytics(user.getUid() , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data)
            {

                if(error)
                {
                    Util.showSnackBar(getActivity(),Message);
                }
                else
                {
                    Analytics analytics= (Analytics) data;
                    txtClicks.setText(String.valueOf(analytics.getClicks()));
                    txtImpressions.setText(String.valueOf(analytics.getImpressions()));
                    txtViews.setText(String.valueOf(analytics.getViews()));
                }

            }
        });

    }


    private void loadData ()
    {



        FireStoreDatabaseManager.loadordersbyid(SharedPref.getUser(getContext()).getUid() , new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data) {


                orderslist.clear();
                if(error)
                {

                    Util.showSnackBarMessage(getActivity(),Message);

                }
                else
                {
                    orderslist.addAll((List<Order>)data);

                }
                loadChart();
            }
        });

    }
    public  void  loadChart()
    {
        ArrayList<Entry> listData = new ArrayList<>();

        for (int i = 0, orderslistSize = orderslist.size(); i < orderslistSize; i++)
        {
            Order order = orderslist.get(i);
            listData.add(new Entry(i,order.getTotalprice().floatValue()));
        }

        LineDataSet lineDataSet = new LineDataSet(listData, "Earning Table");
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
        LineData lineData=new LineData(lineDataSet);

        chart.getXAxis().setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue (float value)
            {
                return String.valueOf(orderslist.get(((int)value)).getFormatedDay());
            }
        });
        Description description=new Description();
        description.setText("Shows Earning of Current Month");
        chart.setDescription(description);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisLeft().setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue (float value)
            {

                String strVal=String.valueOf(value);
                if(value>=1000)
                {
                    strVal=String.format("%.1f K",value/1000);
                }
                return "Rs "+strVal;

            }
        });
        chart.setData(lineData);
        chart.animateXY(2000,2000);

        chart.invalidate();


    }
}