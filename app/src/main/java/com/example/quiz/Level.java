package com.example.quiz;

public class Level {

    private int id;

    private int timeCounter;

    private String name;
    public Level(){
    }

    public Level(String name, int timeCounter) {
        this.name = name;
        this.timeCounter = timeCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(int timeCounter) {
        this.timeCounter = timeCounter;
    }

    @Override
    public String toString() {
        return getName();
    }
}
