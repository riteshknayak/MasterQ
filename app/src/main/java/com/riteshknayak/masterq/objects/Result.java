package com.riteshknayak.masterq.objects;

import java.io.Serializable;

public class Result implements Serializable {
    private String question, questionUid, option1, option2, option3, option4, trueOption, givenAnswer;
    private int questionIndex;
    private Boolean questionResult;

    public Result() {}

    public Result( Question question, String givenAnswer, Boolean questionResult) {
        this.question = question.getQuestion();
        this.option1 = question.getOption1();
        this.option2 = question.getOption2();
        this.option3 = question.getOption3();
        this.option4 = question.getOption4();
        this.trueOption = question.getAnswer();
        this.questionUid = question.getUId();
        this.givenAnswer = givenAnswer;
        this.questionIndex = question.getIndex();
        this.questionResult = questionResult;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getTrueOption() {
        return trueOption;
    }

    public void setTrueOption(String trueOption) {
        this.trueOption = trueOption;
    }

    public String getGivenAnswer() {
        return givenAnswer;
    }

    public void setGivenAnswer(String givenAnswer) {
        this.givenAnswer = givenAnswer;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public Boolean getQuestionResult() {
        return questionResult;
    }

    public void setQuestionResult(Boolean questionResult) {
        this.questionResult = questionResult;
    }

    public String getQuestionUid() {
        return questionUid;
    }

    public void setQuestionUid(String questionUid) {
        this.questionUid = questionUid;
    }
}
