package com.riteshknayak.masterq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.AnimRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.riteshknayak.masterq.adapters.TopicsAdapter;
import com.riteshknayak.masterq.databinding.ActivityTopicsBinding;
import com.riteshknayak.masterq.objects.Topic;

import java.util.ArrayList;

public class TopicsActivity extends AppCompatActivity {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    ActivityTopicsBinding binding;
    TextView catView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MasterQ);

        setContentView(R.layout.activity_topics);

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        String catId = getShared.getString("catId", "CmYfZdAGsDpA2Vupktb4");
        String catName = getShared.getString("catName", "Geography");

        final ArrayList<Topic> Topics = new ArrayList<>();
        final TopicsAdapter adapter = new TopicsAdapter(this, Topics,this);

        catView = findViewById(R.id.categoryName);
        if (catName != null) {
            catView.setText(catName);
        }

        database.collection("categories")
                .document(catId)
                .collection("Topics")
                .addSnapshotListener((value, error) -> {
                    Topics.clear();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Topic model = snapshot.toObject(Topic.class);
                        if (model.isVisibility()) {
                            model.setTopicId(snapshot.getId());
                            Topics.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });

        AnimatedRecyclerView mRecyclerView = findViewById(R.id.topicList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(TopicsActivity.this, 2));
        @AnimRes int layoutAnimation = R.anim.layout_animation_from_bottom;
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(TopicsActivity.this, layoutAnimation);
        mRecyclerView.setLayoutAnimation(animationController);
        adapter.notifyDataSetChanged();
        mRecyclerView.scheduleLayoutAnimation();
    }
    public void onBackPressed() {
        Intent intent = new Intent(TopicsActivity.this, MainActivity.class);
        startActivity(intent);
        //TODO update this show that it will show a dialog for conformation exit
    }
}
