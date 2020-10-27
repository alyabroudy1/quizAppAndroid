package com.example.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StartingScreenActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<Question> quizGList = new ArrayList<>();

    // result to be token to other activity
    private static final int REQUEST_CODE_QUIZ = 1; //hold the activity number to used in method startActivityForResult
    public static final String EXTRA_LEVEL = "extraLevel";
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGH_SCORE = "keyHighScore";

    private TextView textViewHighScore;
    private Spinner spinnerCategory;
    private Spinner spinnerLevel;
    private int highScore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        // layout_view variables
        textViewHighScore = findViewById(R.id.text_view_highscore);
        //for difficulty levels spinner
        spinnerLevel = findViewById(R.id.spinner_level);
        spinnerCategory = findViewById(R.id.spinner_category);

        // 1-load a previous result is there was any. and load pre defined Categories and levels
        loadPreviousIfActivityNotNew();

        //create the start quiz button
        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        // 2-onclick event start the Quiz activity using the startQuiz method
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });
    }

    /**
     * load previous result if the there is.
     */
    private void loadPreviousIfActivityNotNew(){
        loadCategories();
        //loadLevels();
        loadHighScore();
    }

    private void startQuiz() {
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();
        Intent intent = new Intent(StartingScreenActivity.this, QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID); // to send the categoryID value to quiz activity
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName); // to send the categoryName value to quiz activity

        //retrieve level level from the spinner
        String level = spinnerLevel.getSelectedItem().toString();

        intent.putExtra(EXTRA_LEVEL, Integer.parseInt(level)); // to send the level value to quiz activity
        //start the created activity
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_QUIZ){
            if (resultCode == RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highScore){
                    updateHighScore(score);
                }
            }
        }

    }

    /**
     * load categories from local dataBase
     */
    private void loadCategories() {
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);

        List<Category> categories = dbHelper.getAllCategories();

        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }

    /**
     * load levels from local dataBase
     */
    private void loadLevels() {
        //fill spinner with intended values
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Level> levels = dbHelper.getAllLevels();

        ArrayAdapter<Level> adapterLevel = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, levels);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapterLevel);

    }

    private void loadHighScore(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScore = prefs.getInt(KEY_HIGH_SCORE, 0);
        //TODO: get the current level from the previous activity to show in the main screen
        textViewHighScore.setText(String.format("%s%s", getResources().getString(R.string.home_page_high_score_text), highScore));
    }

    private void updateHighScore(int highScoreNew) {
        highScore = highScoreNew;
        //TODO: update the current level also
        textViewHighScore.setText(String.format("%s%s", getResources().getString(R.string.home_page_high_score_text), highScore));
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGH_SCORE, highScore);
        editor.apply();
    }
}