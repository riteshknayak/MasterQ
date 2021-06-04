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
import com.riteshknayak.masterq.objects.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<Result> Results = new ArrayList<>();
    int index = 0;
    FirebaseFirestore database;
    FirebaseAuth auth;
    Question question;
    CountDownTimer timer;
    String catId;
    String topicId;
    String UId;
    Integer mScore;
    CollectionReference topicReference;
    DocumentReference userTopicReference;
    int highestTopicQuestion; //TODO remove this in your modified app for developer
    TextView selectedTextView;


    //TODO ADD COMMENTS SHOW THAT OTHER DEVELOPERS CAN READ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        catId = getShared.getString("catId", null);
        topicId = getShared.getString("topicId", null);

        auth = FirebaseAuth.getInstance();
        UId = auth.getCurrentUser().getUid();

        setScore();
        setHighestTopicQuestion();

        topicReference = database.collection("categories")
                .document(catId)
                .collection(topicId);

        userTopicReference = database.collection("users")
                .document(UId)
                .collection(catId)
                .document(topicId);

        userTopicReference.get().addOnSuccessListener(documentSnapshot -> {
            final int[] lastQuestion = new int[1];
            if (documentSnapshot.get("LastQuestion") == null) {
                lastQuestion[0] = 0;
            } else {
                lastQuestion[0] = (int) (long) documentSnapshot.get("LastQuestion");

                topicReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (lastQuestion[0] == queryDocumentSnapshots.size()) {
                        lastQuestion[0] = 0;
                    }
                });
            }

            topicReference
                    .orderBy("index", Query.Direction.ASCENDING)
                    .whereGreaterThan("index", lastQuestion[0])
                    .limit(10)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Question question = snapshot.toObject(Question.class);
                    question.setUId(snapshot.getId());
                    questions.add(question);
                }
                if (queryDocumentSnapshots.size() < 10) {

                    topicReference
                            .orderBy("index", Query.Direction.ASCENDING)
                            .whereGreaterThan("index", 0)
                            .limit(10 - queryDocumentSnapshots.size())
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

        binding.NextBtn.setOnClickListener(v -> {
            if (timer != null)
                timer.cancel();
            if (selectedTextView != null) {
                checkAnswer(selectedTextView);
            }else{
                Results.add(new Result(question.getQuestion(),index+1,false,question.getUId()));
            }
            index++;
            setNextQuestion();
        });
    }

    void setHighestTopicQuestion() {
        database.collection("categories")
                .document(catId)
                .collection("Topics")
                .document(topicId)
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.get(topicId.concat("HighestIndex")) != null) {
                highestTopicQuestion = (int) (long) documentSnapshot.get(topicId.concat("HighestIndex"));
            } else {
                highestTopicQuestion = 0;
            }
        });
        //TODO collect data about highest last Question reached by a player as if it reaches end of a topic you have to add new questions fast
    }


    void setScore() {
        database.collection("users")
                .document(UId)
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.get("score") == null) {
                mScore = 0;
            } else {
                mScore = (int) (long) documentSnapshot.get("score");
            }
        });
    }

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
                userTopicReference.update(wrongAnswer);

                Toast.makeText(QuizActivity.this, "Time up", Toast.LENGTH_SHORT).show();

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    Results.add(new Result(question.getQuestion(), index + 1, false,question.getUId()));
                                    index++;
                                    setNextQuestion();
                                });
                            }
                        },
                        1000
                );
            }
        };
    }

    public void setNextQuestion() {
        if (timer != null){
            timer.cancel();
        }
        if (index < questions.size()) {
            resetBackground();
            binding.questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
            enableClick();
            resetTimer();
            setContentView(binding.getRoot());
            timer.start();
            if (index + 1 >= questions.size()) {
                binding.NextBtn.setText("See Result");
                binding.NextBtn.setOnClickListener(v -> {
                    if (selectedTextView != null) {
                        checkAnswer(selectedTextView);
                    }else {
                        Results.add(new Result(question.getQuestion(), index + 1, false,question.getUId()));
                    }
                    if (timer != null){
                        timer.cancel();
                    }
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("Results",Results);
                    startActivity(intent);
                });
            }
        }else{
            if (timer != null){
                timer.cancel();
            }
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("Results",Results);
            startActivity(intent);
        }
    }

    void checkAnswer(TextView textView) {
        setScore();
        auth = FirebaseAuth.getInstance();
        String selectedAnswer = textView.getText().toString();

        Map<String, Object> rightAnswer = new HashMap<>();
        rightAnswer.put(question.getUId(), true);
        Map<String, Object> wrongAnswer = new HashMap<>();
        wrongAnswer.put(question.getUId(), false);
        Map<String, Object> setLastQuestion = new HashMap<>();
        setLastQuestion.put("LastQuestion", question.getIndex());

        if (selectedAnswer.equals(question.getAnswer())) {
            userTopicReference.update(rightAnswer);

            Map<String, Object> score = new HashMap<>();
            score.put("score", mScore + 10);

            database.collection("users")
                    .document(UId)
                    .update(score);
            setScore();

            Results.add(new Result(question.getQuestion(), index+1,true,question.getUId()));

        } else {
            userTopicReference.update(wrongAnswer);

            Results.add(new Result(question.getQuestion(),index+1,false,question.getUId()));
        }

        userTopicReference.update(setLastQuestion);

        selectedTextView = null;

//        if (question.getIndex() > highestTopicQuestion){
//            database.collection("categories")
//                    .document(catId)
//                    .collection("Topics")
//                    .document(topicId)
//                    .get().addOnSuccessListener(documentSnapshot -> {
//                if (documentSnapshot.get(topicId.concat("HighestIndex")) != null) {
//                    highestTopicQuestion = (int) (long) documentSnapshot.get(topicId.concat("HighestIndex"));
//                } else {
//                    highestTopicQuestion = 0;
//                }
//                Map<String, Object> highestIndex = new HashMap<>();
//                highestIndex.put(topicId.concat("HighestIndex"), question.getIndex());
//
//                database.collection("categories")
//                        .document(catId)
//                        .collection("Topics")
//                        .document(topicId)
//                        .update(highestIndex);
//            });
//        }
//        setHighestTopicQuestion();
//        if (question.getIndex() > highestTopicQuestion) {
//            Map<String, Object> data = new HashMap<>();
//            data.put(topicId.concat("HighestIndex"), question.getIndex());
//
//            database.collection("MasterQ")
//                    .document("Statistics")
//                    .collection("categories")
//                    .document(topicId)
//                    .update(data);
//        }
    }

    //TODO *topic score. add this in future update as you don't have enough user
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

    void resetBackground() {
        binding.option1.setBackground(ContextCompat.getDrawable(this, R.drawable.option_unselected));
        binding.option2.setBackground(ContextCompat.getDrawable(this, R.drawable.option_unselected));
        binding.option3.setBackground(ContextCompat.getDrawable(this, R.drawable.option_unselected));
        binding.option4.setBackground(ContextCompat.getDrawable(this, R.drawable.option_unselected));
    }

    void setSelectedBackground(TextView selectedTextView) {
        resetBackground();
        selectedTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.option_selected));
        this.selectedTextView = selectedTextView;
    }

    //TODO use OnclickListener Instead of switch statement
    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                TextView selected = (TextView) view;
                setSelectedBackground(selected);
                break;
            case R.id.quitBtn:
                timer.cancel();
                Intent intent = new Intent(QuizActivity.this, TopicsActivity.class);
                intent.putExtra("catId", catId);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        timer.cancel();
        //TODO update this show that it will show a dialog for conformation exit
    }

    //TODO "*" Means require High number of users
    //TODO specify back button for every other activity
    //TODO show dialog to the user if there is and update in app

    //TODO *add a feature show that any one can contribute question to the topic which will be verified before adding into MasterQ
    //TODO *add a shout section showing users with highest score in this week, day, ,month, year, all time


    //TODO Reward free topics to users who shares the app with whatsapp-3 Topics, Facebook- 10 Topics, Twitter- 5 topics. Verify weather the user is really shareing or not

    //TODO *Make its on social media profiles like Twitter, Facebook, Instagram.  shutout in social media to highest contributing users of each month
    //TODO *Make this app monthly subscription based
    //TODO add share button in your app
    //TODO use multithreading to make your app fast check the app performance in profiler
    //TODO *affiliate marketing in android app
    //TODO add splash screen
}


