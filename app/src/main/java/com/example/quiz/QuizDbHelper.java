package com.example.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quiz.QuizContract.CategoriesTable;
import com.example.quiz.QuizContract.LevelsQuizTable;
import com.example.quiz.QuizContract.LevelsTable;
import com.example.quiz.QuizContract.QuestionsTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Quiz.db";
    private static final int DATABASE_VERSION = 1; // should be increased if changes applied to database structure or reinstall the app

    //google database questions
    DatabaseReference quizGDB = FirebaseDatabase.getInstance().getReference("questions/");
    ArrayList<Question> googleQuizList;
    //to make this class singleton which means to be created only one time in the app
    private static  QuizDbHelper instance;

    //create instance of Sqlite database object
    private SQLiteDatabase db;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //to make this class singleton which means to be created only one time in the app
    public static synchronized QuizDbHelper getInstance(Context context){
        if (instance == null){
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;


        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE "+
                CategoriesTable.TABLE_NAME + " ( "+
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CategoriesTable.COLUMN_NAME + " TEXT "+
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE "+
                QuestionsTable.TABLE_NAME + " ( "+
                QuestionsTable._ID + " TEXT PRIMARY KEY , "+
                QuestionsTable.COLUMN_QUESTION + " TEXT, "+
                QuestionsTable.COLUMN_OPTION1 + " TEXT, "+
                QuestionsTable.COLUMN_OPTION2 + " TEXT, "+
                QuestionsTable.COLUMN_OPTION3 + " TEXT, "+
                QuestionsTable.COLUMN_OPTION4 + " TEXT, "+
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, "+
                QuestionsTable.COLUMN_ANSWER_DESCRIPTION + " TEXT, "+
                QuestionsTable.COLUMN_LEVEL + " INTEGER, "+
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, "+
                "FOREIGN KEY (" + QuestionsTable.COLUMN_CATEGORY_ID + " ) REFERENCES "+
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        final String SQL_CREATE_LEVEL_TABLE = "CREATE TABLE "+
                LevelsTable.TABLE_NAME + " ( "+
                LevelsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                LevelsTable.COLUMN_NAME + " TEXT, "+
                LevelsTable.COLUMN_TIME_COUNTER + " INTEGER "+
                ")";

        final String SQL_CREATE_LEVEL_QUIZ_TABLE = "CREATE TABLE "+
                LevelsQuizTable.TABLE_NAME + " ( "+
                LevelsQuizTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                LevelsQuizTable.COLUMN_LEVEL + " INTEGER, "+
                LevelsQuizTable.COLUMN_QUESTION + " INTEGER "+
                ")";



        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        db.execSQL(SQL_CREATE_LEVEL_TABLE);
        db.execSQL(SQL_CREATE_LEVEL_QUIZ_TABLE);


        fillCategoriesTable();
        fillLevelsTable();
       fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ QuestionsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ LevelsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ LevelsQuizTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //enable category foreign key
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void saveQuestionToGoogle(Question question){
        // Write a message to the database
        //create a unique id
        String id = quizGDB.push().getKey();
        quizGDB.child(id).setValue(question);
    }


    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void addLevel(Level level) {
        ContentValues cv = new ContentValues();
        cv.put(LevelsTable.COLUMN_NAME, level.getName());
        cv.put(LevelsTable.COLUMN_TIME_COUNTER, level.getTimeCounter());
        db.insert(LevelsTable.TABLE_NAME, null, cv);
    }

    private void addLevelQuiz(LevelQuiz lq) {
        ContentValues cv = new ContentValues();
        cv.put(LevelsQuizTable.COLUMN_LEVEL, lq.getLevel());
        cv.put(LevelsQuizTable.COLUMN_QUESTION, lq.getQuestion());
        db.insert(LevelsQuizTable.TABLE_NAME, null, cv);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable._ID, question.getId());
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_ANSWER_DESCRIPTION, question.getAnswerDescription());
        cv.put(QuestionsTable.COLUMN_LEVEL, question.getLevel());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category(Category.ISLAMIC_CATEGORY_NAME);
        addCategory(c1);
        Category c2 = new Category(Category.GEOGRAPHY_CATEGORY_NAME);
        addCategory(c2);
        Category c3 = new Category(Category.MATH_CATEGORY_NAME);
        addCategory(c3);
    }

    private void fillLevelsTable() {
        int timeCounter= 30;
       for (int i = 1; i <= 10; i++){
           Level level = new Level(""+i, 30);
           addLevel(level);
           timeCounter = timeCounter -2;
       }
    }

    private boolean isExists(Question question){
        db = getReadableDatabase();

        String selection = QuestionsTable._ID + " = ? ";// +
                //" AND " + QuestionsTable.COLUMN_LEVEL + " = ? ";

        String[] selectionArgs = new String[]{ String.valueOf(question.getId()) };

        Cursor c = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (c.moveToFirst()){
           return true;
        }
        c.close();
        return false;
    }

    /**
     * get questions from google fire, then save it to local database
     */
    private void fillQuestionsTable() {
         googleQuizList = new ArrayList<>();

        // get quiz from google
        quizGDB.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot quiz : dataSnapshot.getChildren()) {
                    //  Question question = new Question();
                    // question = quiz.getValue(Question.class);
                    // Log.i("levels:", "question "+question.getQuestion())
                    Question question = new Question();
                    question.setId(quiz.child("id").getValue(String.class));



                    question.setQuestion(quiz.child("question").getValue(String.class));


                    if (quiz.child("answerNr").getValue() instanceof String){
                        question.setAnswerNr(Integer.parseInt( quiz.child("answerNr").getValue(String.class)));
                    }else {
                        question.setAnswerNr(quiz.child("answerNr").getValue(Long.class).intValue());
                    }

                    if (quiz.child("categoryID").getValue() instanceof String){
                        question.setCategoryID(Integer.parseInt( quiz.child("categoryID").getValue(String.class)));
                    }else {
                        question.setCategoryID(quiz.child("categoryID").getValue(Long.class).intValue());
                    }

                    if (quiz.child("level").getValue() instanceof String){
                        question.setLevel(Integer.parseInt( quiz.child("level").getValue(String.class)));
                    }else {
                        question.setLevel(quiz.child("level").getValue(Long.class).intValue());
                    }

                    question.setOption1(quiz.child("option1").getValue(String.class));
                    question.setOption2(quiz.child("option2").getValue(String.class));
                    question.setOption3(quiz.child("option3").getValue(String.class));
                    question.setOption4(quiz.child("option4").getValue(String.class));
                    question.setAnswerDescription(quiz.child("answerDescription").getValue(String.class));
                  //  googleQuizList.add(question);
                    //addQuestion(question);


                    if (googleQuizList.contains(question)) {
                    } else {
                        addQuestion(question);
                        googleQuizList.add(question);
                    }
                    Log.i("questions:", "question "+question.getQuestion()+" loaded from google");
                }
                ///////////
                Log.i("method:", "FillQuestions. returns "+googleQuizList.size()+" questions count");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("loading from googleFire failed: " + error.getMessage());
            }
        });
    }

    public ArrayList<Question> getQuestions(int categoryID, int level){

        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_LEVEL + " = ? ";

        String[] selectionArgs = new String[]{ String.valueOf(categoryID),  String.valueOf(level) };

        Cursor c = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(""+c.getColumnIndex(QuestionsTable._ID)); //get question id from database
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setAnswerDescription(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_DESCRIPTION)));
                question.setLevel(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_LEVEL)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();

        Log.i("method:", "getQuestions. returns "+questionList.size()+" questions count");
        return questionList;
        //Log.i("method:", "getQuestions. returns "+googleQuizList.size()+" questions count");
        //return googleQuizList;
    }

    public List<Category> getAllCategories(){
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ CategoriesTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            }while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }

    public List<Level> getAllLevels(){
        List<Level> levelList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ LevelsTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do {
                Level level = new Level();
                level.setId(c.getInt(c.getColumnIndex(LevelsTable._ID)));
                level.setName(c.getString(c.getColumnIndex(LevelsTable.COLUMN_NAME)));
                levelList.add(level);
            }while (c.moveToNext());
        }
        c.close();
        return levelList;
    }
    
    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(""+c.getColumnIndex(QuestionsTable._ID)); //get question id from database
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setAnswerDescription(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_DESCRIPTION)));
                question.setLevel(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_LEVEL)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
    
    public ArrayList<Question> getQuestionsByCategory(int categoryID){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? "; //+
               // " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";

        String[] selectionArgs = new String[]{ String.valueOf(categoryID) };

        Cursor c = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
                );
        if (c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(""+c.getColumnIndex(QuestionsTable._ID)); //get question id from database
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setAnswerDescription(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_DESCRIPTION)));
                question.setLevel(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_LEVEL)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    } 
    
    public void fillLevelQuiz(){
        List<Category> categoriesList = getAllCategories();
        List<Level> levels = getAllLevels();
        List<Question> questionList = new ArrayList<>();

        Random rand = new Random(); //instance of random class
        db = getReadableDatabase();

        for (Category cat : categoriesList)
        {
            questionList = getQuestionsByCategory(cat.getId());
            int levelQuizCount = questionList.size() / levels.size();
            for (Level level:levels) {
                for (int i = 0; i <= levelQuizCount;i++) {
                    int quizId = rand.nextInt(questionList.size());
                    LevelQuiz lq= new LevelQuiz(level.getId(), quizId);
                    addLevelQuiz(lq);
                    questionList.remove(quizId);
                }
            }
        }
    }
    
    private ArrayList<Integer> getLevelsQuizId(Level level){
        ArrayList<Integer> levelQuizList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = LevelsQuizTable.COLUMN_LEVEL + " = ? "; //+
                //" AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";

        //String[] selectionArgs = new String[]{ String.valueOf(categoryID), difficulty };
        String[] selectionArgs = new String[]{ String.valueOf(level.getId()) };

        Cursor c = db.query(
                LevelsQuizTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
                );
        if (c.moveToFirst()) {
            do {
                int questionId = c.getInt(c.getColumnIndex(LevelsQuizTable.COLUMN_QUESTION));
                levelQuizList.add(questionId);
            } while (c.moveToNext());
        }        
        c.close();
            return levelQuizList;
    
    }


}
