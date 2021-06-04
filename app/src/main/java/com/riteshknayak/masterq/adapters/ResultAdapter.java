package com.riteshknayak.masterq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riteshknayak.masterq.R;
import com.riteshknayak.masterq.objects.Result;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder>{

    Context context;
    ArrayList<Result> results;

    public ResultAdapter(Context context, ArrayList<Result> results){
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
        if(result.getQuestionResult()){
            holder.answerValue.setText(R.string.Right);
        }else{
            holder.answerValue.setText(R.string.Wrong);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView questionIndex;
        TextView question;
        TextView answerValue;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            questionIndex = itemView.findViewById(R.id.question_index);
            question = itemView.findViewById(R.id.result_question);
            answerValue = itemView.findViewById(R.id.user_answer);
        }
    }
}
