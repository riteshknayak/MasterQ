package com.riteshknayak.masterq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.riteshknayak.masterq.databinding.ActivityQuizBinding;
import com.riteshknayak.masterq.objects.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
    ArrayList<Question> questions;
    int index = 0;
    FirebaseFirestore database;
    FirebaseAuth auth;
    Question question;
    CountDownTimer timer;
    String catId;
    String topicId;
    String UId;
    Integer score;
    CollectionReference topicReference; //TODO use this to shorten code
    DocumentReference userTopicReference; //TODO use this to shorten code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        catId = getShared.getString("catId", null);
        topicId = getShared.getString("topicId", null);

        topicReference = database.collection("categories")
                .document(catId)
                .collection(topicId);

        auth = FirebaseAuth.getInstance();
        UId = auth.getCurrentUser().getUid();

        database.collection("users")
                .document(UId)
                .collection(catId)
                .document(topicId)
                .get().addOnSuccessListener(documentSnapshot -> {
            final int[] lastQuestion = new int[1];
            if (documentSnapshot.get("LastQuestion") == null) {
                lastQuestion[0] = 0;
            } else {
                lastQuestion[0] = (int) (long) documentSnapshot.get("LastQuestion");

                database.collection("categories")
                        .document(catId)
                        .collection(topicId)
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (lastQuestion[0] == queryDocumentSnapshots.size()) {
                        lastQuestion[0] = 0;
                    }
                });
            }

            database.collection("categories")
                    .document(catId)
                    .collection(topicId)
                    .orderBy("index", Query.Direction.ASCENDING)
                    .whereGreaterThan("index", lastQuestion[0])
                    .limit(15)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Question question = snapshot.toObject(Question.class);
                    question.setUId(snapshot.getId());
                    questions.add(question);
                }
                if (queryDocumentSnapshots.size() < 15) {

                    topicReference.orderBy("index", Query.Direction.ASCENDING)
                            .whereGreaterThan("index", 0)
                            .limit(15 - queryDocumentSnapshots.size())
                            .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots1) {
                            Question question = snapshot.toObject(Question.class);
                            question.setUId(snapshot.getId());
                            questions.add(question);
                        }
                        setNextQuestion();
                    });
                } else {
                    setNextQuestion();
                }
            });
        });

        database.collection("users")
                .document(UId)
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.get("score") == null) {
                score = 0;
            } else {
                score = (int) (long) documentSnapshot.get("score");
            }
        });
    }

    //TODO collect data about highest last Question reached by a player as if it reaches end of a topic you have to add new questions fast

    void resetTimer() {
        timer = new CountDownTimer(21000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                disableClick();

                Map<String, Object> wrongAnswer = new HashMap<>();
                wrongAnswer.put(question.getUId(), false);

                database.collection("users")
                        .document(UId)
                        .collection(catId)
                        .document(topicId)
                        .update(wrongAnswer); //TODO not working

                Toast.makeText(QuizActivity.this, "Time up", Toast.LENGTH_SHORT).show();

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    if (index + 1 < questions.size()) {
                                        index++;
                                        setNextQuestion();
                                    } else {
                                        Toast.makeText(QuizActivity.this, "Quiz Finished...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },
                        1000
                );
            }
        };
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
        enableClick();
        resetTimer();
        setContentView(binding.getRoot());
        timer.start();
    }

    void checkAnswer(TextView textView) {
        auth = FirebaseAuth.getInstance();

        String selectedAnswer = textView.getText().toString();

        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.option_selected));

        Map<String, Object> rightAnswer = new HashMap<>();
        rightAnswer.put(question.getUId(), true);
        Map<String, Object> wrongAnswer = new HashMap<>();
        wrongAnswer.put(question.getUId(), false);
        Map<String, Object> setLastQuestion = new HashMap<>();
        wrongAnswer.put("LastQuestion", question.getIndex());
        Map<String, Object> setScore = new HashMap<>();
        wrongAnswer.put("score", score + 10);

        if (selectedAnswer.equals(question.getAnswer())) {
            database.collection("users")
                    .document(UId)
                    .collection(catId)
                    .document(topicId)
                    .update(rightAnswer);

            database.collection("users")
                    .document(UId)
                    .collection(catId)
                    .document(topicId)
                    .update(setLastQuestion);

            database.collection("users")
                    .document(UId)
                    .update(setScore); //TODO not working

        } else {
            database.collection("users")
                    .document(UId)
                    .collection(catId)
                    .document(topicId)
                    .update(wrongAnswer);

            database.collection("users")
                    .document(UId)
                    .collection(catId)
                    .document(topicId)
                    .update(setLastQuestion);
        }
    }

    void enableClick() {
        binding.option1.setClickable(true);
        binding.option2.setClickable(true);
        binding.option3.setClickable(true);
        binding.option4.setClickable(true);
    }

    void disableClick() {
        binding.option1.setClickable(false);
        binding.option2.setClickable(false);
        binding.option3.setClickable(false);
        binding.option4.setClickable(false);
    }

    void reset() {
        binding.option1.setBackground(ContextCompat.getDrawable(this, R.drawable.option_unselected));
        binding.option2.setBackground(ContextCompat.getDrawable(this, R.drawable.option_unselected));
        binding.option3.setBackground(ContextCompat.getDrawable(this, R.drawable.option_unselected));
        binding.option4.setBackground(ContextCompat.getDrawable(this, R.drawable.option_unselected));
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if (timer != null)
                    timer.cancel();
                disableClick();
                TextView selected = (TextView) view;
                checkAnswer(selected);
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    if (index + 1 < questions.size()) {
                                        index++;
                                        setNextQuestion();
                                    } else {
                                        Toast.makeText(QuizActivity.this, "Quiz Finished...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },
                        700
                );

                break;
            case R.id.quitBtn:
                timer.cancel();
                Intent intent2 = new Intent(QuizActivity.this, TopicsActivity.class);
                intent2.putExtra("catId", catId);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
    }
}


