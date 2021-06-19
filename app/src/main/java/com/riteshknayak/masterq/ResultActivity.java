package com.riteshknayak.masterq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.AnimRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.riteshknayak.masterq.adapters.ResultAdapter;
import com.riteshknayak.masterq.objects.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ResultActivity extends AppCompatActivity {

    FirebaseFirestore database;
    FirebaseAuth auth;
    String UId, catId, topicId;
    Integer score;
    int correctAnswer;
    TextView scoreView, resultView;
    ScrollView scrollView;
    private Boolean showedAnim = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        scrollView = findViewById(R.id.result_scroll);

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        catId = getShared.getString("catId", null);
        topicId = getShared.getString("topicId", null);

        ArrayList<Result> Results = new ArrayList<>();
        Results = (ArrayList<Result>) getIntent().getSerializableExtra("Results");
        ResultAdapter adapter = new ResultAdapter(this, Results);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        AnimatedRecyclerView mRecyclerView = findViewById(R.id.result_list);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        @AnimRes int layoutAnimation = R.anim.layout_animation_from_bottom;
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(ResultActivity.this, layoutAnimation);

        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            Rect scrollBounds = new Rect();
            scrollView.getHitRect(scrollBounds);
            if (mRecyclerView.getLocalVisibleRect(scrollBounds)) {
                if (!showedAnim){
                    //mRecyclerView is Visible
                    mRecyclerView.setLayoutAnimation(animationController);
                    adapter.notifyDataSetChanged();
                    mRecyclerView.scheduleLayoutAnimation();
                    showedAnim = Boolean.TRUE;
                }
            } else {
                // NONE of the mRecyclerView is within the visible window
            }
        });

        Calendar rightNow = Calendar.getInstance();
        Date strDate = rightNow.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd MMM yyyy z", Locale.ENGLISH);
        String time = formatter.format(strDate);

        auth = FirebaseAuth.getInstance();
        UId = auth.getCurrentUser().getUid();
        database = FirebaseFirestore.getInstance();

        for (int index = 0; index < Results.size(); index++) {
            Result result = Results.get(index);
            database.collection("users")
                    .document(UId)
                    .collection(catId)
                    .document(topicId)
                    .collection(time)
                    .document(String.valueOf(result.getQuestionIndex()).concat("- ").concat(result.getQuestion()))
                    .set(result);
        }

        score = getIntent().getIntExtra("score", 0);
        scoreView = findViewById(R.id.score_r);
        scoreView.setText(score.toString());

        correctAnswer = getIntent().getIntExtra("correctAnswer", 0);
        resultView = findViewById(R.id.result_r);
        resultView.setText(String.format("%d/10", correctAnswer));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultActivity.this, TopicActivity.class);
        startActivity(intent);
    }
}