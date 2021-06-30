package com.riteshknayak.masterq.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.R;
import com.riteshknayak.masterq.TopicActivity;
import com.riteshknayak.masterq.TopicsActivity;
import com.riteshknayak.masterq.objects.SliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class ImagerSliderAdapter extends SliderViewAdapter<ImagerSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();
    Activity activity;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();


    public ImagerSliderAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    Boolean i = Boolean.TRUE;

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
                if (checkIfOnline()){
                    SharedPreferences shared = context.getSharedPreferences("app", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("catId", sliderItem.getCatLocation());
                    editor.putString("catName", sliderItem.getCatLocation());
                    editor.putString("topicId", sliderItem.getTopicLocation());
                    editor.apply();
                    Intent intent = new Intent(context, TopicActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        database.collection("users")
                .document(auth.getUid())
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getBoolean("SliderShowPrompt") != null){
                Boolean showPrompt = documentSnapshot.getBoolean("SliderShowPrompt");
                if (showPrompt){
            new MaterialTapTargetPrompt.Builder(activity)
                    .setTarget(viewHolder.uselessView)
                    .setBackgroundColour(0xFFCA1395)
                    .setPrimaryText("Check out what's new here!")
                    .setPromptStateChangeListener((prompt, state) -> {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                        {
                            // User has pressed the prompt target
                        }
                        CountDownTimer timer = new CountDownTimer(3000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {}

                            @Override
                            public void onFinish() {
                                prompt.dismiss();
                            }
                        };
                        timer.start();
                    })
                    .show();

                    Map<String, Object> update = new HashMap<>();
                    update.put("SliderShowPrompt", false );
                    database.collection("users")
                            .document(auth.getUid())
                            .update(update);

                }
            }
        });

//        if (i){
//            i = Boolean.FALSE;
//        }
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        View uselessView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.slider_image);
            uselessView = itemView.findViewById(R.id.uselessView);
            this.itemView = itemView;

        }
    }

    Boolean checkIfOnline() {
        if (!isConnected()) {
            new AestheticDialog.Builder(activity, DialogStyle.CONNECTIFY, DialogType.ERROR)
                    .setTitle("NO CONNECTION FOUND!")
                    .setMessage("Check your Internet connection")
                    .setCancelable(true)
                    .setDarkMode(true)
                    .setGravity(Gravity.TOP)
                    .setAnimation(DialogAnimation.SWIPE_RIGHT)
                    .show();
            return false;
        }
        return true;
    }

    public boolean isConnected() {
        boolean connected;
        try {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return false;
    }

}

