package com.riteshknayak.masterq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.adapters.TopicsAdapter;
import com.riteshknayak.masterq.databinding.ActivityTopicsBinding;
import com.riteshknayak.masterq.objects.Topic;

import java.util.ArrayList;

public class TopicsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    ActivityTopicsBinding binding;
    TextView catView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        String catId = getShared.getString("catId", "CmYfZdAGsDpA2Vupktb4");
        String catName = getShared.getString("catName", "Geography");

        recyclerView = findViewById(R.id.topicList);
        final ArrayList<Topic> Topics = new ArrayList<>();
        final TopicsAdapter adapter = new TopicsAdapter(this, Topics);

        catView = findViewById(R.id.categoryName);
        if (catName != null){
            catView.setText(catName);
        }

        database.collection("categories")
        .document(catId)
        .collection("Topics")
        .addSnapshotListener((value, error) -> {
            Topics.clear();
            for (DocumentSnapshot snapshot : value.getDocuments()) {
                Topic model = snapshot.toObject(Topic.class);
                if(model.isVisibility()) {
                    model.setTopicId(snapshot.getId());
                    Topics.add(model);
                }
            }
            adapter.notifyDataSetChanged();
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
    }
    public void onBackPressed() {
    Intent intent = new Intent(TopicsActivity.this, MainActivity.class);
    startActivity(intent);
    //TODO update this show that it will show a dialog for conformation exit
    }
}
