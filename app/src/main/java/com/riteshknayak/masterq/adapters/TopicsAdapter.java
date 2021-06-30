package com.riteshknayak.masterq.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riteshknayak.masterq.R;
import com.riteshknayak.masterq.TopicActivity;
import com.riteshknayak.masterq.objects.Topic;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicViewHolder> {

    Context context;
    ArrayList<Topic> topics;
    Activity activity;

    public TopicsAdapter(Context context, ArrayList<Topic> models, Activity activity) {
        this.context = context;
        this.topics = models;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        final Topic model = topics.get(position);

        Glide.with(context)
                .load(model.getTopicImage())
                .into(holder.imageView);

        holder.tv.setText(model.getTopicName());

        holder.itemView.setOnClickListener(v -> {
            if (checkIfOnline()){
                Intent intent = new Intent(context, TopicActivity.class);
                SharedPreferences shared = context.getSharedPreferences("app", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("topicId", model.getTopicId());
                editor.apply();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.topic_image);
            tv = itemView.findViewById(R.id.namet);
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






