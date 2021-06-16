package com.riteshknayak.masterq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }
    public void onBackPressed() {
        Intent intent = new Intent(TopicActivity.this, TopicsActivity.class );
        startActivity(intent);
    }
}
