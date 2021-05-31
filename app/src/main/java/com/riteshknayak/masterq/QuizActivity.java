package com.riteshknayak.masterq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.riteshknayak.masterq.databinding.ActivityQuizBinding;
import com.riteshknayak.masterq.objects.Question;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
    ArrayList<Question> questions;
    int index = 0;
    FirebaseFirestore database;
    Question question;
    String catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        catId = getShared.getString("catId", "CmYfZdAGsDpA2Vupktb4");
        final String topicId = getIntent().getStringExtra("topicId");

        database.collection("categories")
                .document(catId)
                .collection(topicId)
                .orderBy("index", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                Question question = snapshot.toObject(Question.class);
                questions.add(question);
            }
            setNextQuestion();
        });
    }

    public void setNextQuestion() {
        reset();
        if (index < questions.size()) {
            binding.questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        }
        setContentView(binding.getRoot());
    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.option_selected));
        if (selectedAnswer.equals(question.getAnswer())) {
        } else {
        }
    }
    void reset() {
        binding.option1.setBackground(ContextCompat.getDrawable(this,R.drawable.option_unselected));
        binding.option2.setBackground(ContextCompat.getDrawable(this,R.drawable.option_unselected));
        binding.option3.setBackground(ContextCompat.getDrawable(this,R.drawable.option_unselected));
        binding.option4.setBackground(ContextCompat.getDrawable(this,R.drawable.option_unselected));
    }    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                TextView selected = (TextView) view;
                checkAnswer(selected);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    if (index <= questions.size()) {
                                        index++;
                                        setNextQuestion();
                                    } else {
//                                          Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
//                                          intent.putExtra("correct", correctAnswers);
//                                          intent.putExtra("total", questions.size());
//                                          startActivity(intent);
                                        Toast.makeText(QuizActivity.this, "Quiz Finished...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },
                        700
                );

                break;
            case R.id.quitBtn:
                Intent intent2 = new Intent(QuizActivity.this, TopicActivity.class);
                intent2.putExtra("catId", catId);
                startActivity(intent2);
                break;
        }
    }
}


