package com.riteshknayak.masterq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.riteshknayak.masterq.R;
import com.riteshknayak.masterq.objects.Result;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    Context context;
    ArrayList<Result> results;

    public ResultAdapter(Context context, ArrayList<Result> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        final Result result = results.get(position);

        holder.questionIndex.setText(String.valueOf(result.getQuestionIndex()));
        holder.question.setText(result.getQuestion());
        holder.parentView.setOnClickListener(v -> {
            if (holder.expandable.getVisibility() == View.GONE) {
                expand(holder.expandable);
            } else if (holder.expandable.getVisibility() == View.VISIBLE) {
                collapse(holder.expandable);
            }
        });
        holder.option1.setText(result.getOption1());
        holder.option2.setText(result.getOption2());
        holder.option3.setText(result.getOption3());
        holder.option4.setText(result.getOption4());
        holder.selectedOption.setText(result.getGivenAnswer());

        if (result.getQuestionResult()) {
            holder.parentView.setBackground(ContextCompat.getDrawable(context, R.drawable.option_right));
        }
        else {
            holder.parentView.setBackground(ContextCompat.getDrawable(context, R.drawable.option_wrong));
            if (result.getGivenAnswer().equals("no")){}else  {
                if (result.getGivenAnswer().equals(result.getOption1())) {
                    holder.option1view.setBackground(ContextCompat.getDrawable(context, R.drawable.option_wrong));
                }else if (result.getGivenAnswer().equals(result.getOption2())) {
                    holder.option2view.setBackground(ContextCompat.getDrawable(context, R.drawable.option_wrong));
                }else if (result.getGivenAnswer().equals(result.getOption3())) {
                    holder.option3view.setBackground(ContextCompat.getDrawable(context, R.drawable.option_wrong));
                }else if (result.getGivenAnswer().equals(result.getOption4())) {
                    holder.option4view.setBackground(ContextCompat.getDrawable(context, R.drawable.option_wrong));
                }
            }
        }


        if (result.getTrueOption().equals(result.getOption1())) {
            holder.option1view.setBackground(ContextCompat.getDrawable(context, R.drawable.option_right));
        }else if (result.getTrueOption().equals(result.getOption2())) {
            holder.option2view.setBackground(ContextCompat.getDrawable(context, R.drawable.option_right));
        }else if (result.getTrueOption().equals(result.getOption3())) {
            holder.option3view.setBackground(ContextCompat.getDrawable(context, R.drawable.option_right));
        }else if (result.getTrueOption().equals(result.getOption4())) {
            holder.option4view.setBackground(ContextCompat.getDrawable(context, R.drawable.option_right));
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView questionIndex, selectedOption, question;
        ConstraintLayout expandable, parentView;
        TextView option1, option2, option3,option4;
        LinearLayout option1view, option2view, option3view, option4view;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            questionIndex = itemView.findViewById(R.id.question_index);
            question = itemView.findViewById(R.id.question);
            expandable = itemView.findViewById(R.id.expandable);
            parentView = itemView.findViewById(R.id.parent_view);
            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);
            option4 = itemView.findViewById(R.id.option4);
            selectedOption = itemView.findViewById(R.id.selected_option);
            option1view = itemView.findViewById(R.id.option_1_view);
            option2view = itemView.findViewById(R.id.option_2_view);
            option3view = itemView.findViewById(R.id.option_3_view);
            option4view = itemView.findViewById(R.id.option_4_view);
        }
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
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

}
