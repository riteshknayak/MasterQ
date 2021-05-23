package com.riteshknayak.masterq;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.databinding.ActivityTopicBinding;
import com.riteshknayak.masterq.topics.TopicAdapter;
import com.riteshknayak.masterq.topics.Topic;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {

    ActivityTopicBinding binding;
    RecyclerView recyclerView;
    FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        String catId = getIntent().getStringExtra("catId");

        recyclerView = findViewById(R.id.topicList);
        final ArrayList<Topic> Topics = new ArrayList<>();
        final TopicAdapter adapter = new TopicAdapter(this, Topics);

        database.collection("categories")
        .document(catId)
        .collection("Topics")
        .addSnapshotListener((value, error) -> {
            Topics.clear();
            for (DocumentSnapshot snapshot : value.getDocuments()) {
                Topic model = snapshot.toObject(Topic.class);
                if(model.isVisible()) {
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
