package com.example.quiz;

public class Category {
    public static final int ISLAMIC = 1;
    public static final String ISLAMIC_CATEGORY_NAME ="Islamic";
    public static final int GEOGRAPHY = 2;
    public static final String GEOGRAPHY_CATEGORY_NAME ="Geography";
    public static final int MATH = 3;
    public static final String MATH_CATEGORY_NAME ="Math";

    private int id;
    private String name;

    public Category(){
    }

    public Category(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return getName();
    }
}
