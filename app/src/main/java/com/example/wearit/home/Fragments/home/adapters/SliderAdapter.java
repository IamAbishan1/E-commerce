package com.example.wearit.home.Fragments.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wearit.R;
import com.example.wearit.api.response.Slider;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {
    List<Slider> sliders;
    LayoutInflater inflater;
    Context context;
    OnSliderClickLister onSliderClickLister;
    Boolean isFitted;

    public SliderAdapter(List<Slider> sliders, Context context, Boolean isFitted) {
        this.sliders = sliders;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.isFitted = isFitted;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        return new SliderViewHolder(inflater.inflate(R.layout.item_slider, parent, false));
    }

    public void setClickLister(OnSliderClickLister onSliderClickLister) {
        this.onSliderClickLister = onSliderClickLister;

    }


    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        Picasso.get().load(sliders.get(position).getImage()).into(viewHolder.imageViewBackground);
        if (isFitted)
            viewHolder.imageViewBackground.setScaleType(ImageView.ScaleType.FIT_XY);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSliderClickLister.onSliderClick(position, sliders.get(position));
            }
        });

    }

    @Override
    public int getCount() {
        return sliders.size();
    }

    public class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;


        public SliderViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            this.itemView = itemView;
        }
    }

    public interface OnSliderClickLister {
        public void onSliderClick(int position, Slider slider);
    }
}
