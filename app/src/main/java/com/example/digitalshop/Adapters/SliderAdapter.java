package com.example.digitalshop.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.digitalshop.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH>
{

    private Context context;
    private List<Object> mSliderItems = new ArrayList<>();
    private  boolean isStringLink;
    public SliderAdapter(Context context,List<Object> SliderItems,boolean isStringLink) {
        this.context = context;
        this.mSliderItems=SliderItems;
        this.isStringLink=isStringLink;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder (ViewGroup parent) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate((isStringLink?R.layout.image_slider_layout_item:R.layout.image_slider_layout_item_cardview), null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {


        //Glide.with( viewHolder.imageViewBackground).load(mSliderItems.get(position));
        if(isStringLink)
        {
            String link= (String) mSliderItems.get(position);
            Picasso.get().load(link).placeholder(R.drawable.logo).into( viewHolder.imageViewBackground);
        }
        else
        {
            viewHolder.imageViewBackground.setImageResource((Integer) mSliderItems.get(position));
        }



    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
        }
    }

}