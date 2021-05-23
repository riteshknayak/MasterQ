package com.riteshknayak.masterq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    Context context;
    ArrayList<TopicModel> topicModels;

    public TopicAdapter(Context context, ArrayList<TopicModel> models) {
        this.context = context;
        this.topicModels = models;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        final TopicModel model = topicModels.get(position);

        holder.textView.setText(model.getTopicName());
        Glide.with(context)
                .load(model.getTopicImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return topicModels.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.topic_image);
            textView = itemView.findViewById(R.id.topic_name);
        }
    }
}






