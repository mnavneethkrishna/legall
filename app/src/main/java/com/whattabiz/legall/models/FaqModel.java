package com.whattabiz.legall.models;

/**
 * Created by User on 8/3/2016.
 */

public class FaqModel {

        private String question, answer;


        public FaqModel() {
        }

        public FaqModel(String question, String answer) {
            this.question = question;
            this.answer = answer;


        }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {

    }
}

