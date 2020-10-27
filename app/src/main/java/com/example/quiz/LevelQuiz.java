package com.example.quiz;

public class LevelQuiz {

    private int id;

    private int level;
    private int question;

    public LevelQuiz(int levelId, int questionId){
        this.level = levelId;
        this.question = questionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

}
