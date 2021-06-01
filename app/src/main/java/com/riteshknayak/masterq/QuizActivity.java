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

import com.google.firebase.auth.FirebaseAuth;
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
    String catId;
    String topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        catId = getShared.getString("catId", "CmYfZdAGsDpA2Vupktb4");
        topicId = getShared.getString("topicId", "CmYfZdAGsDpA2Vupktb4");

//        String userId = auth.getCurrentUser().getUid();

//        final Integer[] r = new Integer[1];
//        database.collection("users")
//                .document("0mu8LcLm8aREn14Qa13LvxTJv9D3")//TODO user hardcoded
//                .collection(catId)
//                .document(topicId)
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
////                r[0] = Integer.parseInt(documentSnapshot.getString("LastQuestion"));
//                r[0]  = 1;
//                int s = r[0] +1;
//            }
//        });
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
////                result[0] = Integer.parseInt(documentSnapshot.getString("LastQuestion"));
//            }
//        });


//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()) {
//                    result[0] = Integer.parseInt(task.getResult().getString("LastQuestion"));
//                }
////                if(task.getResult().exists()){
////                    result[0] = 0;
////                }else{
////                    result[0] = (Integer)task.getResult().get("LastQuestion");
////                }
//            }
//        });

//        Integer s = r[0];
        database.collection("categories")
                .document(catId)
                .collection(topicId)
                .orderBy("index", Query.Direction.ASCENDING)
//                .whereGreaterThan("index", s)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                Question question = snapshot.toObject(Question.class);
                question.setUId(snapshot.getId());
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
        enableClick();
        setContentView(binding.getRoot());

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
        wrongAnswer.put("LastQuestion", question.getIndex().toString());

        if (selectedAnswer.equals(question.getAnswer())) {
            database.collection("users")
                    .document(auth.getUid())
                    .collection(catId)
                    .document(topicId)
                    .update(rightAnswer);
        } else {
            database.collection("users")
                    .document(auth.getUid())
                    .collection(catId)
                    .document(topicId)
                    .update(wrongAnswer);
        }

        database.collection("users")
                .document(auth.getUid())
                .collection(catId)
                .document(topicId)
                .update(setLastQuestion);

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
                Intent intent2 = new Intent(QuizActivity.this, TopicsActivity.class);
                intent2.putExtra("catId", catId);
                startActivity(intent2);
                break;
        }
    }
}


