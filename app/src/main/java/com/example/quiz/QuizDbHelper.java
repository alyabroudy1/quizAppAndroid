package com.example.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.quiz.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Quiz.db";
    private static final int DATABASE_VERSION = 1; // should be increased if changes applied to database structure or reinstall the app

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
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                QuestionsTable.COLUMN_QUESTION + " TEXT, "+
                QuestionsTable.COLUMN_OPTION1 + " TEXT, "+
                QuestionsTable.COLUMN_OPTION2 + " TEXT, "+
                QuestionsTable.COLUMN_OPTION3 + " TEXT, "+
                QuestionsTable.COLUMN_OPTION4 + " TEXT, "+
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, "+
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, "+
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, "+
                "FOREIGN KEY (" + QuestionsTable.COLUMN_CATEGORY_ID + " ) REFERENCES "+
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //enable category foreign key
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category(Category.ISLAMIC_CATEGORY_NAME);
        addCategory(c1);
        Category c2 = new Category(Category.GEOGRAPHY_CATEGORY_NAME);
        addCategory(c2);
        Category c3 = new Category(Category.MATH_CATEGORY_NAME);
        addCategory(c3);
    }

    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionsTable() {

        Question q1 = new Question(
                "أكبر شبه جزيرة في العالم",
                "شبه الجزيرة العربية",
                "شبه الجزيرة الأمريكية",
                "شبه الجزيرة الكورية",
                "شبه الجزيرة الكورية2",
                1,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        addQuestion(q1);

        Question q2 = new Question(
                "أطول نهر في العالم",
                "نهر الامازون",
                "نهر النيل",
                "نهر الفرات ",
                "نهر الفرات2 ",
                2,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        addQuestion(q2);

        Question q3 = new Question(
                "يلتقي عندها البحر المتوسط بالبحر الأحمر",
                "رأس الرجاء الصالح",
                "رأس الرجاء الصالح2",
                "قناة السبيل",
                "قناة السويس",
                3,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        addQuestion(q3);

        Question q4 = new Question(
                "يحد الجهورية العربية السورية من الغرب",
                "تركيا",
                "البحر المتوسط",
                "العراق",
                "العراق2",
                2,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        addQuestion(q4);

        Question q5 = new Question(
                "صحابي صلى النبي على آله فقال اللهم صلى على آل ابي فلان من هو",
                "أبو بكر ",
                "أبو بكر 1",
                "ابن أبي أوفى",
                "عمر الخطاب ",
                2,
                Question.DIFFICULTY_EASY, Category.ISLAMIC);
        addQuestion(q5);

        Question q6 = new Question(
                "صحابي فداه النبي بأبيه وأمه يوم أحد فقال ارم فداك أبي وأمي",
                "علي بن أبي طالب ",
                "حمزة بن عبدالمطلب ",
                "حمزة بن عبدالمطلب 2",
                "سعد بن مالك",
                3,
                Question.DIFFICULTY_EASY, Category.ISLAMIC);
        addQuestion(q6);

        Question q7 = new Question(
                "بماذا كانت تسمى المدينة النبوية قبل الهجرة",
                "يثرب",
                "مكة2",
                "مكة",
                "الطائف",
                1,
                Question.DIFFICULTY_EASY, Category.ISLAMIC);
        addQuestion(q7);

        Question q8 = new Question(
                "من هو أبو الأنبياء",
                "إسماعيل ",
                "نوح2",
                "نوح",
                "إبراهيم",
                3,
                Question.DIFFICULTY_EASY, Category.ISLAMIC);
        addQuestion(q8);

        Question q9 = new Question(
                "ما هي أعظم آية في القرآن الكريم",
                "آية الدين",
                "آية الكرسي ",
                "آية الربا",
                "آية الربا2",
                2,
                Question.DIFFICULTY_EASY, Category.ISLAMIC);
        addQuestion(q9);

        Question q10 = new Question(
                "ما هي أعظم سورة في القرآن الكريم",
                "الفاتحة",
                "آل عمران",
                "آل عمران2",
                "البقرة",
                1,
                Question.DIFFICULTY_EASY, Category.ISLAMIC);
        addQuestion(q10);

        Question q11 = new Question(
                "سيف سله الله على الكافرين",
                "علي بن أبي طالب2",
                "علي بن أبي طالب",
                "خالد بن الوليد",
                "عمر بن الخطاب",
                2,
                Question.DIFFICULTY_EASY, Category.ISLAMIC);
        addQuestion(q11);


    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
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

    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getColumnIndex(QuestionsTable._ID)); //get question id from database
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";

        String[] selectionArgs = new String[]{ String.valueOf(categoryID), difficulty };

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
                question.setId(c.getColumnIndex(QuestionsTable._ID)); //get question id from database
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
