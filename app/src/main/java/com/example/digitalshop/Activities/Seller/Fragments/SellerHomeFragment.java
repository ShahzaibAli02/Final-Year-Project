package com.example.digitalshop.Activities.Seller.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.digitalshop.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SellerHomeFragment extends Fragment
{


    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_home , container , false);
    }

    @Override
    public void onViewCreated (@NonNull @NotNull View view , @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        LineChart chart = view.findViewById(R.id.linechart);
        ArrayList<Entry> listData = new ArrayList<>();
        listData.add(new Entry(0f, 20f));
        listData.add(new Entry(1f, 10f));
        listData.add(new Entry(2f, 35f));
        listData.add(new Entry(3f, 13f));
        listData.add(new Entry(4f, 32f));
        listData.add(new Entry(5f, 12f));
        listData.add(new Entry(6f, 35f));
        listData.add(new Entry(7f, 13f));
        listData.add(new Entry(8f, 32f));
        listData.add(new Entry(9f, 12f));

        LineDataSet lineDataSet = new LineDataSet(listData, "Chart Table");
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
        LineData lineData=new LineData(lineDataSet);

        chart.setData(lineData);
        chart.animateXY(2000,2000);


        chart.invalidate();
    }
}