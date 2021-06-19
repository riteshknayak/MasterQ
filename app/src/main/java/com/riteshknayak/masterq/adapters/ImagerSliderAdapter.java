package com.riteshknayak.masterq.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.riteshknayak.masterq.R;
import com.riteshknayak.masterq.TopicActivity;
import com.riteshknayak.masterq.TopicsActivity;
import com.riteshknayak.masterq.objects.SliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImagerSliderAdapter extends
        SliderViewAdapter<ImagerSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public ImagerSliderAdapter(Context context) {
        this.context = context;
    }

    //you can Completely customize you can add buttons text etc to the imageSlider

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    public void clearAllSlides (){
        mSliderItems.clear();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImageUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        if (sliderItem.getOnClickLocation().equals("category")){
            viewHolder.itemView.setOnClickListener(v -> {
                SharedPreferences shared = context.getSharedPreferences("app", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("catId", sliderItem.getCatLocation());
                editor.putString("catName", sliderItem.getCatLocation());
                editor.apply();
                Intent intent = new Intent(context, TopicsActivity.class);
                context.startActivity(intent);
            });
        }else if (sliderItem.getOnClickLocation().equals("topic")){
            viewHolder.itemView.setOnClickListener(v -> {
                SharedPreferences shared = context.getSharedPreferences("app", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("catId", sliderItem.getCatLocation());
                editor.putString("catName", sliderItem.getCatLocation());
                editor.putString("topicId", sliderItem.getTopicLocation());
                editor.apply();
                Intent intent = new Intent(context, TopicActivity.class);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.slider_image);
            this.itemView = itemView;
        }
    }
}

