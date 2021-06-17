package com.riteshknayak.masterq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.riteshknayak.masterq.adapters.ResultAdapter;
import com.riteshknayak.masterq.databinding.ActivityResultBinding;
import com.riteshknayak.masterq.objects.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    RecyclerView recyclerView;
    FirebaseFirestore database;
    FirebaseAuth auth;
    String UId, catId, topicId;
    Integer score;
    int correctAnswer;
    TextView scoreView, resultView;
    ConstraintLayout parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        catId = getShared.getString("catId", null);
        topicId = getShared.getString("topicId", null);

        recyclerView = findViewById(R.id.result_list);
        ArrayList<Result> Results = new ArrayList<>();
        Results = (ArrayList<Result>) getIntent().getSerializableExtra("Results");
        ResultAdapter adapter = new ResultAdapter(this, Results);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

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

        score = getIntent().getIntExtra("score",0);
        scoreView = findViewById(R.id.score_r);
        scoreView.setText(score.toString());

        correctAnswer = getIntent().getIntExtra("correctAnswer",0);
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