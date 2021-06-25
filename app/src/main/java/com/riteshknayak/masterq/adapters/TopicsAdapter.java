package com.riteshknayak.masterq.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicViewHolder> {

    Context context;
    ArrayList<Topic> topics;

    public TopicsAdapter(Context context, ArrayList<Topic> models) {
        this.context = context;
        this.topics = models;
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
            Intent intent = new Intent(context, TopicActivity.class);
            SharedPreferences shared = context.getSharedPreferences("app", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("topicId", model.getTopicId());
            editor.apply();
            context.startActivity(intent);
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
}






