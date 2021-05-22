package com.riteshknayak.masterq;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    FirebaseFirestore database = FirebaseFirestore.getInstance();


    Context context;
    ArrayList<CategoryModel> categoryModels;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final CategoryModel model = categoryModels.get(position);

        holder.textView.setText(model.getCategoryName());
        Glide.with(context)
                .load(model.getCategoryImage())
                .into(holder.imageView);


        //TODO  below onClickListener for opening quizActivity
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, TopicActivity.class);
//                intent.putExtra("catId", model.getCategoryId());
                context.startActivity(intent);
//            Map<String, Boolean> data = new HashMap<>();
//            data.put("unlocked", true);
//            database.collection("users")
//                    .document(FirebaseAuth.getInstance().getUid())
//                    .collection(model.getCategoryId())
//                    .document("1")
//                    .set(data);
//            database.collection("users")
//                    .document(FirebaseAuth.getInstance().getUid())
//                    .collection(model.getCategoryId())
//                    .document("2")
//                    .set(data);
//            database.collection("users")
//                    .document(FirebaseAuth.getInstance().getUid())
//                    .collection(model.getCategoryId())
//                    .document("3")
//                    .set(data);
//            database.collection("users")
//                    .document(FirebaseAuth.getInstance().getUid())
//                    .collection(model.getCategoryId())
//                    .document("4")
//                    .set(data);
//            database.collection("users")
//                    .document(FirebaseAuth.getInstance().getUid())
//                    .collection(model.getCategoryId())
//                    .document("5")
//                    .set(data);
//            database.collection("users")
//                    .document(FirebaseAuth.getInstance().getUid())
//                    .collection(model.getCategoryId())
//                    .document("6")
//                    .set(data);

            //below code to check if  category is unlocked
//            final boolean[] val = new boolean[1];
//            database.collection("users")
//                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
//                    .collection("userData")
//                    .document("categories")
//                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    val[0] = documentSnapshot.getBoolean(model.getCategoryId());
//                }
//            });
//            if(val[0]){
//                Intent intent = new Intent(context, QuizActivity.class);
//                intent.putExtra("catId", model.getCategoryId());
//                context.startActivity(intent);
//                Map<String, Boolean> data = new HashMap<>();
//                data.put(model.getCategoryId(), true);
//            }else{
//                Toast toast = Toast.makeText(context, "Access Denied", Toast.LENGTH_LONG);
//                toast.show();
//            }
//            below code to add category data
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.category);
        }
    }
}
