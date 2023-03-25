package com.vicentearmenta.androidtriviatesting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "android_trivia.db";

    public static final String TABLE_QUESTION = "question_pool";

    public static final String COLUMN_QT_ID = "_id";

    public static final String COLUMN_QT_TEXT = "QTText";

    public static final String COLUMN_QT_ANSWER = "QTAnswer";

    public static final String TABLE_ANSWER = "answer_pool";

    public static final String COLUMN_AW_ID = "_id";

    public static final String COLUMN_AW_TEXT = "AWText";

    public static final String TABLE_RESULT = "result";

    public static final String COLUMN_RS_ID = "_id";

    public static final String COLUMN_RS_USERNAME = "RSUserName";

    public static final String COLUMN_RS_SCORE = "RSScore";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuestionTable = " CREATE TABLE " + TABLE_QUESTION + " ( " +
            COLUMN_QT_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            COLUMN_QT_TEXT + " TEXT NOT NULL, " +
            COLUMN_QT_ANSWER + " INTEGER NOT NULL );";

        String createAnswerTable = " CREATE TABLE " + TABLE_ANSWER + " ( " +
                COLUMN_AW_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                COLUMN_AW_TEXT + " TEXT NOT NULL );";

        String createResultTable = "CREATE TABLE " + TABLE_RESULT + " ( " +
                COLUMN_RS_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                COLUMN_RS_USERNAME + " TEXT NOT NULL, " +
                COLUMN_RS_SCORE + " INTEGER NOT NULL );";

        String seedQuestionTable = " INSERT INTO " + TABLE_QUESTION + " (" + COLUMN_QT_ID + ", "
                + COLUMN_QT_TEXT + ", " + COLUMN_QT_ANSWER + ") VALUES " +
                "(1, 'What was the first Pixar film to be made?',1)," +
                "(2, 'Brave is set in which country',2)," +
                "(3, 'In Monsters Inc., what city do Mike and Sulley live in?',3)," +
                "(4, 'What is the name of the talking dog in Up?',4)," +
                "(5, 'In the Incredibles what is Frozone´s actual first name?',5)," +
                "(6, 'In Cars, Lighting McQueen finds himself stuck in what town?',6)," +
                "(7, 'In Coco, what does Miguel´s family make for a living?',7)," +
                "(8, 'What is the address that Marlin and Dory are trying to reach in Finding Nemo?',8)," +
                "(9, 'Who is the leader of the food stealing grasshopper in gang in A Bug´s Life?',9)," +
                "(10, 'What sport does Riley play in Inside Out?',10);";

        String seedAnswerTable = " INSERT INTO " + TABLE_ANSWER + " (" + COLUMN_AW_ID + ", " +
                COLUMN_AW_TEXT + " ) VALUES " +
                "(1, 'Toy Story')," +
                "(2, 'Scotland')," +
                "(3, 'Monstropolis')," +
                "(4, 'Dug')," +
                "(5, 'Lucius')," +
                "(6, 'Radiator Springs')," +
                "(7, 'Shoes')," +
                "(8, '42 Wallaby Way')," +
                "(9, 'Hopper')," +
                "(10, 'Hockey')," +
                "(11, 'Remy')," +
                "(12, 'Ian and Barley Lightfoot')," +
                "(13, 'The Good Dinosaur')," +
                "(14, 'Emperor Zurg')," +
                "(15, 'Waste Allocation Load Lifter- Earth');";

        db.execSQL(createQuestionTable);
        db.execSQL(createAnswerTable);
        db.execSQL(createResultTable);

        db.execSQL(seedQuestionTable);
        db.execSQL(seedAnswerTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String deleteQuestionTable = "DROP TABLE IF EXISTS " + TABLE_QUESTION;
        String deleteAnswerTable = "DROP TABLE IF EXISTS " + TABLE_ANSWER;

        db.execSQL(deleteQuestionTable);
        db.execSQL(deleteAnswerTable);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
