package com.riteshknayak.masterq.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.R;
import com.riteshknayak.masterq.TopicsActivity;
import com.riteshknayak.masterq.objects.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<Category> categoryModels;
    Activity activity;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public CategoryAdapter(Context context, ArrayList<Category> categoryModels, Activity activity) {
        this.context = context;
        this.categoryModels = categoryModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final Category model = categoryModels.get(position);

        Glide.with(context)
                .load(model.getCategoryImage())
                .into(holder.imageView);

        holder.tv.setText(model.getCategoryName());
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, TopicsActivity.class);
            context.startActivity(intent);

            SharedPreferences shared = context.getSharedPreferences("app", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("catId", model.getCategoryId());
            editor.putString("catName", model.getCategoryName());
            editor.apply();

        });
        database.collection("users")
                .document(auth.getUid())
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getBoolean("CatShowPrompt") != null) {
                Boolean showPrompt = documentSnapshot.getBoolean("CatShowPrompt");
                if (showPrompt) {
                    new MaterialTapTargetPrompt.Builder(activity)
                            .setTarget(holder.itemView)
                            .setBackgroundColour(0xFFCA1395)
                            .setPrimaryText("List of categories")
                            .setSecondaryText("New categories will be added soon")
                            .setPromptStateChangeListener((prompt, state) -> {
                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                    // User has pressed the prompt target
                                }
                                CountDownTimer timer = new CountDownTimer(4000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onFinish() {
                                        prompt.dismiss();
                                        Map<String, Object> update = new HashMap<>();
                                        update.put("CatShowPrompt", false);
                                        update.put("SliderShowPrompt", true);
                                        database.collection("users")
                                                .document(auth.getUid())
                                                .update(update);
                                    }
                                };
                                timer.start();
                            })
                            .show();

                    Map<String, Object> update = new HashMap<>();
                    update.put("CatShowPrompt", false);
                    update.put("SliderShowPrompt", true);
                    database.collection("users")
                            .document(auth.getUid())
                            .update(update);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            tv = itemView.findViewById(R.id.namec);
        }
    }
}
