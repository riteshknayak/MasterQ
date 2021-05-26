package com.riteshknayak.masterq;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.riteshknayak.masterq.databinding.ActivityQuizBinding;
import com.riteshknayak.masterq.objects.Question;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;

    ArrayList<Question> questions;
    int index = 0;
    FirebaseFirestore database;
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        String catId = getShared.getString("catId","CmYfZdAGsDpA2Vupktb4");
        final String topicId = getIntent().getStringExtra("topicId");

        database.collection("categories")
                .document(catId)
                .collection(topicId)
                .orderBy("index")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Question question = snapshot.toObject(Question.class);
                    questions.add(question);
                }
            }
        });
        setNextQuestion();
    }

    void setNextQuestion() {
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        }
    }


