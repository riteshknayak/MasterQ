package com.riteshknayak.masterq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.objects.Topic;

public class TopicActivity extends AppCompatActivity {

    TextView topicName, playersNumber, rating, questionsNumber, topicDescription;
    String topicId, catId;
    FirebaseFirestore database;
    Topic topic;
    ImageView topicImage;
    Button backButton, startQuiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MasterQ);

        setContentView(R.layout.activity_topic);

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        topicId = getShared.getString("topicId", null);
        catId = getShared.getString("catId", null);

        topicName = findViewById(R.id.topic_name);
        topicImage = findViewById(R.id.topic_image);
        playersNumber = findViewById(R.id.players);
        rating = findViewById(R.id.rating);
        questionsNumber = findViewById(R.id.questions_number);
        backButton = findViewById(R.id.back_button);
        startQuiz = findViewById(R.id.start_quiz);
        topicDescription = findViewById(R.id.topic_description);

        topicName.setText(topicId);

        database = FirebaseFirestore.getInstance();
        database.collection("categories")
                .document(catId)
                .collection("Topics")
                .document(topicId)
                .get().addOnSuccessListener(documentSnapshot -> {
            topic = documentSnapshot.toObject(Topic.class);

            Glide.with(TopicActivity.this)
                    .load(topic.getTopicMainImage())
                    .centerCrop()
                    .into(topicImage);

            database.collection("categories")
                    .document(catId)
                    .collection(topicId)
                    .get().addOnSuccessListener(queryDocumentSnapshots ->
                    questionsNumber.setText(String.valueOf(queryDocumentSnapshots.size()).concat(" Questions")));

            rating.setText(String.valueOf(topic.getReviewNumber()).concat(" Reviews"));
            playersNumber.setText(String.valueOf(topic.getPlayers()).concat(" Players"));
            topicDescription.setText(topic.getTopicDescription());
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TopicActivity.this, TopicsActivity.class);
            startActivity(intent);
        });

        startQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(TopicActivity.this, QuizActivity.class);
            startActivity(intent);
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(TopicActivity.this, TopicsActivity.class);
        startActivity(intent);
    }
}
