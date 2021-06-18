package com.riteshknayak.masterq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
    String catId, topicId, UId;
    Integer mScore;
    Integer addedScore = 0;
    CollectionReference topicReference;
    DocumentReference userTopicReference;
    int highestTopicQuestion; //TODO remove this in your modified app for developer
    TextView selectedTextView, time;
    int correctAnswer = 0;


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
                        setCurrentQuestion(index);
                    });
                } else {
                    setNextQuestion();
                    setCurrentQuestion(index);
                }
            });
        });

        binding.NextBtn.setOnClickListener(v -> {
            if (timer != null)
                timer.cancel();
            if (selectedTextView != null) {
                setAttempted(index);
                checkAnswer(selectedTextView);
            } else {
                resetCurrentQuestion(index);
                Results.add(new Result(question,"no" , false));
            }
            index++;
            setNextQuestion();
            setCurrentQuestion(index);
        });

        binding.quitBtn.setOnClickListener(v -> {
            timer.cancel();
            Intent intent = new Intent(QuizActivity.this, TopicsActivity.class);
            startActivity(intent);
        });
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
        timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long percent = millisUntilFinished/1000*5;

                binding.timeView.setFgColorStart(0xFFCA1395);
                binding.timeView.setFgColorEnd(0xFF630CAE);
                binding.timeView.setPercent(percent);

                time = findViewById(R.id.time);
                time.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                disableClick();

                resetCurrentQuestion(index);
                Map<String, Object> wrongAnswer = new HashMap<>();
                wrongAnswer.put(question.getUId(), false);
                userTopicReference.update(wrongAnswer);

                Toast.makeText(QuizActivity.this, "Time up", Toast.LENGTH_SHORT).show();

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    Results.add(new Result(question, "no", false));
                                    index++;
                                    setCurrentQuestion(index);
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
        if (timer != null) {
            timer.cancel();
        }
        if (index == 9){
        }
        if (index < questions.size()) {
            resetBackground();
            binding.questionCounter.setText(String.format("Question %d", (index + 1)));
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
                    } else {
                        Results.add(new Result(question, "no", false));
                    }
                    if (timer != null) {
                        timer.cancel();
                    }
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("Results", Results);
                    intent.putExtra("score", addedScore);
                    intent.putExtra("correctAnswer", correctAnswer);
                    startActivity(intent);
                });
            }
        } else {
            if (timer != null) {
                timer.cancel();
            }
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("Results", Results);
            intent.putExtra("score", addedScore);
            intent.putExtra("correctAnswer", correctAnswer);
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
            addedScore = addedScore+10;

            Map<String, Object> score = new HashMap<>();
            score.put("score", mScore + 10);

            database.collection("users")
                    .document(UId)
                    .update(score);
            setScore();

            Results.add(new Result(question, selectedAnswer, true));

            correctAnswer++;

        } else {
            userTopicReference.update(wrongAnswer);

            Results.add(new Result(question, selectedAnswer, false));
        }

        userTopicReference.update(setLastQuestion);

        selectedTextView = null;

        database.collection("categories")
                .document(catId)
                .collection("Topics")
                .document(topicId)
                .get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.get(topicId.concat("HighestIndex")) != null) {
                Long h = (long)documentSnapshot.get(topicId.concat("HighestIndex"));
                highestTopicQuestion = h.intValue();
            } else {
                highestTopicQuestion = 0;
            }
            if (highestTopicQuestion < question.getIndex())
                database.collection("categories")
                        .document(catId)
                        .collection("Topics")
                        .document(topicId)
                        .get().addOnSuccessListener(mDocumentSnapshot -> {

                    Map<String, Object> highestIndex = new HashMap<>();
                    highestIndex.put(topicId.concat("HighestIndex"), question.getIndex());

                    database.collection("categories")
                            .document(catId)
                            .collection("Topics")
                            .document(topicId)
                            .update(highestIndex);
                });
        });
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
        binding.option1.setBackground(ContextCompat.getDrawable(this, R.drawable.parent_view_background));
        binding.option2.setBackground(ContextCompat.getDrawable(this, R.drawable.parent_view_background));
        binding.option3.setBackground(ContextCompat.getDrawable(this, R.drawable.parent_view_background));
        binding.option4.setBackground(ContextCompat.getDrawable(this, R.drawable.parent_view_background));
    }

    void setSelectedBackground(TextView selectedTextView) {
        resetBackground();
        selectedTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.option_selected));
        this.selectedTextView = selectedTextView;
    }

    void setAttempted(int index) {
        index+=1;
        switch (index){
            case 1:
                binding.top1.setVisibility(View.GONE);
                binding.tick1.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.top2.setVisibility(View.GONE);
                binding.tick2.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.top3.setVisibility(View.GONE);
                binding.tick3.setVisibility(View.VISIBLE);
                break;
            case 4:
                binding.top4.setVisibility(View.GONE);
                binding.tick4.setVisibility(View.VISIBLE);
                break;
            case 5:
                binding.top5.setVisibility(View.GONE);
                binding.tick5.setVisibility(View.VISIBLE);
                break;
            case 6:
                binding.top6.setVisibility(View.GONE);
                binding.tick6.setVisibility(View.VISIBLE);
                break;
            case 7:
                binding.top7.setVisibility(View.GONE);
                binding.tick7.setVisibility(View.VISIBLE);
                break;
            case 8:
                binding.top8.setVisibility(View.GONE);
                binding.tick8.setVisibility(View.VISIBLE);
                break;
            case 9:
                binding.top9.setVisibility(View.GONE);
                binding.tick9.setVisibility(View.VISIBLE);
                break;
            case 10:
                binding.top10.setVisibility(View.GONE);
                binding.tick10.setVisibility(View.VISIBLE);
                break;
        }
    }

    void setCurrentQuestion(int index) {
        index+=1;
        switch (index){
            case 1:
                binding.top1.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
            case 2:
                binding.top2.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
            case 3:
                binding.top3.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
            case 4:
                binding.top4.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
            case 5:
                binding.top5.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
            case 6:
                binding.top6.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
            case 7:
                binding.top7.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
            case 8:
                binding.top8.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
            case 9:
                binding.top9.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
            case 10:
                binding.top10.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view_pink));
                break;
        }
    }

    void resetCurrentQuestion(int index) {
        index+=1;
        switch (index){
            case 1:
                binding.top1.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
            case 2:
                binding.top2.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
            case 3:
                binding.top3.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
            case 4:
                binding.top4.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
            case 5:
                binding.top5.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
            case 6:
                binding.top6.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
            case 7:
                binding.top7.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
            case 8:
                binding.top8.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
            case 9:
                binding.top9.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
            case 10:
                binding.top10.setBackground(ContextCompat.getDrawable(this, R.drawable.circular_view));
                break;
        }
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
        }
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        Intent intent = new Intent(QuizActivity.this, TopicsActivity.class);
        startActivity(intent);
        //TODO update this show that it will show a dialog for conformation exit
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


