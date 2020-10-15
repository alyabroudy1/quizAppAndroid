package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore"; // the data need to be passed after finishing this activity in finish method

    //for time counter
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    //to identify variables we want to save after activity restart example by rotating the screen
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;

    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;

    private TextView textViewAnswerDescription;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb; //hold the default color value of the radio buttons

    //to change the color of the time counter if it gets lower than 10 seconds
    private ColorStateList textColorDefaultCd;

    //count down timer
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;

    private int questionCounter;
    private int questionCounterTotal; //total questions in the list
    private Question currentQuestion;

    private int score;
    private boolean answered;

    //variable for back button confirmation
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        TextView textViewCategory = findViewById(R.id.text_view_category);
        TextView textViewDifficulty = findViewById(R.id.text_view_difficulty);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        textViewAnswerDescription = findViewById(R.id.text_view_answer_description);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        //save text color of time counter
        textColorDefaultCd = textViewCountDown.getTextColors();

        //retrieve difficulty value from the previous activity
        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(StartingScreenActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(StartingScreenActivity.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(StartingScreenActivity.EXTRA_DIFFICULTY);

        textViewDifficulty.setText(String.format("%s%s", getResources().getString(R.string.quiz_page_difficulty_text), difficulty));
        textViewCategory.setText(String.format("%s%s", getResources().getString(R.string.quiz_page_category_text), categoryName));

        //check if the app i restored from a destroyed activity like rotate
        if (savedInstanceState == null) {
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID, difficulty);
            questionCounterTotal = questionList.size();
            Collections.shuffle(questionList); //to get the questions in the list at a random order

            showNextQuestion();
        }else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCounterTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            //check if current answer is answered
            if (!answered){
                startCountDown(); // continue the count time as were left
            }else{
                updateCountDownText();
                showSolution();
            }

        }
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered){
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()){
                        checkAnswer();
                    }else {
                        Toast.makeText(QuizActivity.this, getResources().getString(R.string.quiz_page_select_answer_text), Toast.LENGTH_LONG).show();
                    }
                }else {
                    showNextQuestion();
                }
            }
        });

    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb); //set radioButton color to its default
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck(); //clear the previous selection

        if (questionCounter < questionCounterTotal){
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            textViewQuestionCount.setText(String.format("%s%s/%s", getResources().getString(R.string.quiz_page_question_count_text),questionCounter, questionCounterTotal));
            answered= false;
            textViewAnswerDescription.setText("");
            buttonConfirmNext.setText(getResources().getString(R.string.quiz_page_confirm_next_text));
            //buttonConfirmNext.setText("Confirm");

            //start the time counter
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int)(timeLeftInMillis / 1000) / 60;
        int seconds = (int)(timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);
        if (timeLeftInMillis < 10000){
            textViewCountDown.setTextColor(Color.RED);
        }else{
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;

        //cancel time counter if answer is picked
        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId()); //to get the checked radioButton out of our rbGroup
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1; // to get the index of the selected button and as our answer starts from one, we add one to the index
        if (answerNr == currentQuestion.getAnswerNr()){//compare user answer with the correct answer
            score++;
            textViewScore.setText(String.format("%s%s", getResources().getString(R.string.quiz_page_score_text), score));
        }
        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText(getResources().getString(R.string.quiz_page_first_answer_correct_text));
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText(getResources().getString(R.string.quiz_page_second_answer_correct_text));
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText(getResources().getString(R.string.quiz_page_third_answer_correct_text));
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestion.setText(getResources().getString(R.string.quiz_page_fourth_answer_correct_text));
                break;
        }
        textViewAnswerDescription.setText(currentQuestion.getAnswerDescription());
        if (questionCounter < questionCounterTotal){
            buttonConfirmNext.setText(getResources().getString(R.string.quiz_page_next_text));
        }else {
            buttonConfirmNext.setText(getResources().getString(R.string.quiz_page_finish_text));
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    //confirmation if the user click the back button to exit the app

    @Override
    public void onBackPressed() {
        //check if waiting time between the second click of back button is greater less than 2 seconds so we finish the app
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }else {
            Toast.makeText(this, getResources().getString(R.string.quiz_page_press_back_to_exit_text), Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cancel time counter if exited from app
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}