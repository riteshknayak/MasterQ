package com.riteshknayak.masterq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.AnimRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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
    ConstraintLayout resultmain;
    private Boolean showedAnim = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        scrollView = findViewById(R.id.result_scroll);

        SharedPreferences getShared = getSharedPreferences("app", MODE_PRIVATE);
        catId = getShared.getString("catId", null);
        topicId = getShared.getString("topicId", null);

        ArrayList<Result> results;
        results = (ArrayList<Result>) getIntent().getSerializableExtra("Results");

        resultmain = findViewById(R.id.resultmain);
        ConstraintLayout resultsView = findViewById(R.id.results_view);
        TextView resultTextView = findViewById(R.id.resultview);

        resultSetText(findViewById(R.id.question_index1), findViewById(R.id.selected_option1), findViewById(R.id.question1), findViewById(R.id.option1_q1), findViewById(R.id.option2_q1), findViewById(R.id.option3_q1), findViewById(R.id.option4_q1), results.get(0), "1");
        setBackground(results.get(0), findViewById(R.id.parent_view1), findViewById(R.id.selected_option1), findViewById(R.id.option_1_view_q1), findViewById(R.id.option_2_view_q1), findViewById(R.id.option_3_view_q1), findViewById(R.id.option_4_view_q1));
        setClickListener(findViewById(R.id.parent_view1), findViewById(R.id.expandable1));

        resultSetText(findViewById(R.id.question_index2), findViewById(R.id.selected_option2), findViewById(R.id.question2), findViewById(R.id.option1_q2), findViewById(R.id.option2_q2), findViewById(R.id.option3_q2), findViewById(R.id.option4_q2), results.get(1), "2");
        setBackground(results.get(1), findViewById(R.id.parent_view2), findViewById(R.id.selected_option2), findViewById(R.id.option_1_view_q2), findViewById(R.id.option_2_view_q2), findViewById(R.id.option_3_view_q2), findViewById(R.id.option_4_view_q2));
        setClickListener(findViewById(R.id.parent_view2), findViewById(R.id.expandable2));

        resultSetText(findViewById(R.id.question_index3), findViewById(R.id.selected_option3), findViewById(R.id.question3), findViewById(R.id.option1_q3), findViewById(R.id.option2_q3), findViewById(R.id.option3_q3), findViewById(R.id.option4_q3), results.get(2), "3");
        setBackground(results.get(2), findViewById(R.id.parent_view3), findViewById(R.id.selected_option3), findViewById(R.id.option_1_view_q3), findViewById(R.id.option_2_view_q3), findViewById(R.id.option_3_view_q3), findViewById(R.id.option_4_view_q3));
        setClickListener(findViewById(R.id.parent_view3), findViewById(R.id.expandable3));

        resultSetText(findViewById(R.id.question_index4), findViewById(R.id.selected_option4), findViewById(R.id.question4), findViewById(R.id.option1_q4), findViewById(R.id.option2_q4), findViewById(R.id.option3_q4), findViewById(R.id.option4_q4), results.get(3), "4");
        setBackground(results.get(3), findViewById(R.id.parent_view4), findViewById(R.id.selected_option4), findViewById(R.id.option_1_view_q4), findViewById(R.id.option_2_view_q4), findViewById(R.id.option_3_view_q4), findViewById(R.id.option_4_view_q4));
        setClickListener( findViewById(R.id.parent_view4), findViewById(R.id.expandable4));

        resultSetText(findViewById(R.id.question_index5), findViewById(R.id.selected_option5), findViewById(R.id.question5), findViewById(R.id.option1_q5), findViewById(R.id.option2_q5), findViewById(R.id.option3_q5), findViewById(R.id.option4_q5), results.get(4), "5");
        setBackground(results.get(4), findViewById(R.id.parent_view5), findViewById(R.id.selected_option5), findViewById(R.id.option_1_view_q5), findViewById(R.id.option_2_view_q5), findViewById(R.id.option_3_view_q5), findViewById(R.id.option_4_view_q5));
        setClickListener( findViewById(R.id.parent_view5), findViewById(R.id.expandable5));
        
        resultSetText(findViewById(R.id.question_index6), findViewById(R.id.selected_option6), findViewById(R.id.question6), findViewById(R.id.option1_q6), findViewById(R.id.option2_q6), findViewById(R.id.option3_q6), findViewById(R.id.option4_q6), results.get(5), "6");
        setBackground(results.get(5), findViewById(R.id.parent_view6), findViewById(R.id.selected_option6), findViewById(R.id.option_1_view_q6), findViewById(R.id.option_2_view_q6), findViewById(R.id.option_3_view_q6), findViewById(R.id.option_4_view_q6));
        setClickListener( findViewById(R.id.parent_view6), findViewById(R.id.expandable6));
        
        resultSetText(findViewById(R.id.question_index7), findViewById(R.id.selected_option7), findViewById(R.id.question7), findViewById(R.id.option1_q7), findViewById(R.id.option2_q7), findViewById(R.id.option3_q7), findViewById(R.id.option4_q7), results.get(6), "7");
        setBackground(results.get(6), findViewById(R.id.parent_view7), findViewById(R.id.selected_option7), findViewById(R.id.option_1_view_q7), findViewById(R.id.option_2_view_q7), findViewById(R.id.option_3_view_q7), findViewById(R.id.option_4_view_q7));
        setClickListener( findViewById(R.id.parent_view7), findViewById(R.id.expandable7));

        resultSetText(findViewById(R.id.question_index8), findViewById(R.id.selected_option8), findViewById(R.id.question8), findViewById(R.id.option1_q8), findViewById(R.id.option2_q8), findViewById(R.id.option3_q8), findViewById(R.id.option4_q8), results.get(7), "8");
        setBackground(results.get(7), findViewById(R.id.parent_view8), findViewById(R.id.selected_option8), findViewById(R.id.option_1_view_q8), findViewById(R.id.option_2_view_q8), findViewById(R.id.option_3_view_q8), findViewById(R.id.option_4_view_q8));
        setClickListener( findViewById(R.id.parent_view8), findViewById(R.id.expandable8));

        resultSetText(findViewById(R.id.question_index9), findViewById(R.id.selected_option9), findViewById(R.id.question9), findViewById(R.id.option1_q9), findViewById(R.id.option2_q9), findViewById(R.id.option3_q9), findViewById(R.id.option4_q9), results.get(8), "9");
        setBackground(results.get(8), findViewById(R.id.parent_view9), findViewById(R.id.selected_option9), findViewById(R.id.option_1_view_q9), findViewById(R.id.option_2_view_q9), findViewById(R.id.option_3_view_q9), findViewById(R.id.option_4_view_q9));
        setClickListener( findViewById(R.id.parent_view9), findViewById(R.id.expandable9));

        resultSetText(findViewById(R.id.question_index10), findViewById(R.id.selected_option10), findViewById(R.id.question10), findViewById(R.id.option1_q10), findViewById(R.id.option2_q10), findViewById(R.id.option3_q10), findViewById(R.id.option4_q10), results.get(9), "10");
        setBackground(results.get(9), findViewById(R.id.parent_view10), findViewById(R.id.selected_option10), findViewById(R.id.option_1_view_q10), findViewById(R.id.option_2_view_q10), findViewById(R.id.option_3_view_q10), findViewById(R.id.option_4_view_q10));
        setClickListener( findViewById(R.id.parent_view10), findViewById(R.id.expandable10));

        Calendar rightNow = Calendar.getInstance();
        Date strDate = rightNow.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd MMM yyyy z", Locale.ENGLISH);
        String time = formatter.format(strDate);

        @AnimRes int layoutAnimation = R.anim.layout_animation_from_bottom;
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(ResultActivity.this, layoutAnimation);
        resultsView.setLayoutAnimation(animationController);

        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            Rect scrollBounds = new Rect();
            scrollView.getHitRect(scrollBounds);
            if (resultTextView.getLocalVisibleRect(scrollBounds)) {
                if (!showedAnim){
                    //mRecyclerView is Visible
                    resultsView.scheduleLayoutAnimation();
                    resultsView.startLayoutAnimation();
                    showedAnim = Boolean.TRUE;
                }
            } else {
                // NONE of the mRecyclerView is within the visible window
            }
        });


        auth = FirebaseAuth.getInstance();
        UId = auth.getCurrentUser().getUid();
        database = FirebaseFirestore.getInstance();

        for (int index = 0; index < results.size(); index++) {
            Result result = results.get(index);
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

        resultmain.setLayoutAnimation(animationController);
        resultmain.scheduleLayoutAnimation();
        resultmain.animate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultActivity.this, TopicActivity.class);
        startActivity(intent);
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
        v.setVisibility(View.VISIBLE);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    void resultSetText(TextView questionIndex,
                       TextView selectedOption,
                       TextView question,
                       TextView option1,
                       TextView option2,
                       TextView option3,
                       TextView option4,
                       Result result,
                       String index
    ) {
        questionIndex.setText(index);
        selectedOption.setText(result.getGivenAnswer());
        question.setText(result.getQuestion());
        option1.setText(result.getOption1());
        option2.setText(result.getOption2());
        option3.setText(result.getOption3());
        option4.setText(result.getOption4());
    }

    void setBackground(Result result,
                       ConstraintLayout parentView,
                       TextView selectedOption,
                       LinearLayout option1view,
                       LinearLayout option2view,
                       LinearLayout option3view,
                       LinearLayout option4view) {
        if (result.getQuestionResult()) {
            parentView.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_right_parent));
        } else {
            parentView.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_wrong_parent));
            if (result.getGivenAnswer().equals("no")) {
                selectedOption.setText(R.string.No_option_selected);
                selectedOption.setTextColor(0xFFFF0000);
                //Never add "no" as a option!
            } else {
                if (result.getGivenAnswer().equals(result.getOption1())) {
                    option1view.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_wrong));
                } else if (result.getGivenAnswer().equals(result.getOption2())) {
                    option2view.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_wrong));
                } else if (result.getGivenAnswer().equals(result.getOption3())) {
                    option3view.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_wrong));
                } else if (result.getGivenAnswer().equals(result.getOption4())) {
                    option4view.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_wrong));
                }
            }
        }


        if (result.getTrueOption().equals(result.getOption1())) {
            option1view.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_right));
        } else if (result.getTrueOption().equals(result.getOption2())) {
            option2view.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_right));
        } else if (result.getTrueOption().equals(result.getOption3())) {
            option3view.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_right));
        } else if (result.getTrueOption().equals(result.getOption4())) {
            option4view.setBackground(ContextCompat.getDrawable(ResultActivity.this, R.drawable.option_right));
        }

    }

    void setClickListener(ConstraintLayout parentView, ConstraintLayout expandable) {
        parentView.setOnClickListener(v -> {
            if (expandable.getVisibility() == View.GONE) {
                expand(expandable);
            } else if (expandable.getVisibility() == View.VISIBLE) {
                collapse(expandable);
            }

        });
    }
}