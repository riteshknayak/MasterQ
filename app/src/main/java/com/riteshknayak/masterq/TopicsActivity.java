package com.riteshknayak.masterq;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.adapters.TopicsAdapter;
import com.riteshknayak.masterq.objects.Topic;

import java.util.ArrayList;

public class TopicsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        String catId = getShared.getString("catId", "CmYfZdAGsDpA2Vupktb4");

        recyclerView = findViewById(R.id.topicList);
        final ArrayList<Topic> Topics = new ArrayList<>();
        final TopicsAdapter adapter = new TopicsAdapter(this, Topics);

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
}
