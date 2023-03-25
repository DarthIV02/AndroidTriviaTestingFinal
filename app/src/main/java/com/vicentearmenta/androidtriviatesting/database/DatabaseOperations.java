package com.vicentearmenta.androidtriviatesting.database;

import static java.util.Collections.shuffle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.vicentearmenta.androidtriviatesting.models.Answer;
import com.vicentearmenta.androidtriviatesting.models.Question;
import com.vicentearmenta.androidtriviatesting.models.Result;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

    private SQLiteDatabase mDatabase;

    private final DatabaseHelper mHelper;

    public DatabaseOperations(Context context){
        mHelper = new DatabaseHelper(context);
        this.open();
    }

    public void open() throws SQLException{
        mDatabase = mHelper.getWritableDatabase();
    }

    public void close(){
        if (mDatabase != null && mDatabase.isOpen()){
            mDatabase.close();
        }
    }

    public String insertUsername(String username){
        if (!mDatabase.isOpen()){
            this.open();
        } // Si la instancia de la base de datos este cerrada la abierta

        mDatabase.beginTransaction(); // Base de datos transaccionales -> No se hace el comit hasta
        // que nosotros lo terminemos

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RS_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_RS_SCORE, 0);
        long lastRowID = mDatabase.insert(DatabaseHelper.TABLE_RESULT, null, values);

        mDatabase.setTransactionSuccessful();
        // Transaction para hacer rollback
        // No se hacen commit a la base de datos hasta que setTransactionSuccesful
        // Util para cuando tienes datos importantes

        mDatabase.endTransaction();

        this.close();

        return Long.toString(lastRowID);
    }

    public int updateScore(String userID){
        if (!mDatabase.isOpen()){
            this.open();
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RS_SCORE, DatabaseHelper.COLUMN_RS_SCORE + " + 1");
        int rowsUpdated = mDatabase.update(
                DatabaseHelper.TABLE_RESULT,
                values,
                DatabaseHelper.COLUMN_RS_ID + " = ?",
                // ? Es un placeholder, y los valores van en String array con un chorro de valores
                // where donde se puede anidar las diferentes tablas como en where SQLite
                new String[]{ userID });

        // Rows updated va a regresar el # de columnas que se actualizan esta en teoria tiene que ser mayor a 1
        // Si no se actualiza la columna esta regresará 0

        this.close();

        return rowsUpdated;
    }

    public void updateScore2(String userID){
        if (!mDatabase.isOpen()){
            this.open();
        }

        ContentValues values = new ContentValues();
        //values.put(DatabaseHelper.COLUMN_RS_SCORE, DatabaseHelper.COLUMN_RS_SCORE + " + 1");
        mDatabase.execSQL("UPDATE " + DatabaseHelper.TABLE_RESULT +
                        " SET " + DatabaseHelper.COLUMN_RS_SCORE + " = " + DatabaseHelper.COLUMN_RS_SCORE + " + 1" +
                        " WHERE " + DatabaseHelper.COLUMN_RS_ID + " = ?",
                new String[]{ userID });

        // Rows updated va a regresar el # de columnas que se actualizan esta en teoria tiene que ser mayor a 1
        // Si no se actualiza la columna esta regresará 0

        this.close();

        //return rowsUpdated;
    }

    public void deleteResults(){
        if (!mDatabase.isOpen()){
            // is te regresa un booleano normalemente
            // ! para negación
            this.open();
        }

        mDatabase.delete(DatabaseHelper.TABLE_RESULT, "1", null);

        this.close();
    }

    public Question getNextQuestion(String questionsAlreadyAsked){
        if (!mDatabase.isOpen()){
            this.open();
        }

        String questionId = null;
        String questionText = null;
        String questionAnswer = null;

        // un cursor son como stored procedures
        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_QUESTION,
                new String[]{ // arreglo pq son varias columnas las que estan en la tabla
                        DatabaseHelper.COLUMN_QT_ID,
                        DatabaseHelper.COLUMN_QT_TEXT,
                        DatabaseHelper.COLUMN_QT_ANSWER
                },
                DatabaseHelper.COLUMN_QT_ID + " NOT IN ( " + questionsAlreadyAsked + " )", //preguntas que ya fueron hechas WHEre
                null, // arreglo de los datos necesarios en el placeholder, en este caso es solo unoo para el placeholder (?),  // el valor para la condicion
                null,
                null,
                "RANDOM()",
                "1"
        );

        // solo hace una iteracion
        while(cursor.moveToNext()){
            // se le ocupa decir donde esta el indice de la columna
            // getColumnIndexOrThrow devuelve donde esta esa columna que estamos buscando
            questionId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QT_ID));
            questionText = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QT_TEXT));
            questionAnswer = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QT_ANSWER));
        }

        cursor.close(); // siempre hay que cerrar los cursores

        List<Answer> options = new ArrayList<>();

        // opcion A - D traer la opcion correcta
        cursor = mDatabase.query(
                DatabaseHelper.TABLE_ANSWER,
                new String[]{
                        DatabaseHelper.COLUMN_AW_ID,
                        DatabaseHelper.COLUMN_AW_TEXT
                },
                DatabaseHelper.COLUMN_AW_ID + " = ?", // seleccion (SELECT * FROM tabla WHERE = ?) esta parte es la del WHERE
                new String[]{ questionAnswer },
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            Answer option = new Answer();
            option.setAnswerId(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AW_ID)));
            option.setAnswerText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AW_TEXT)));

            options.add(option);
        }

        cursor.close();

        // opciones restantes (incorrectas)
        cursor = mDatabase.query(
                DatabaseHelper.TABLE_ANSWER,
                new String[]{
                        DatabaseHelper.COLUMN_AW_ID,
                        DatabaseHelper.COLUMN_AW_TEXT
                },
                DatabaseHelper.COLUMN_AW_ID + " NOT IN ( ? )", // seleccion (SELECT * FROM tabla WHERE = ?) esta parte es la del WHERE
                new String[]{ questionAnswer },
                null,
                null,
                "RANDOM()",
                "3"
        );

        while(cursor.moveToNext()){
            Answer option = new Answer();
            option.setAnswerId(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AW_ID)));
            option.setAnswerText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AW_TEXT)));

            options.add(option);
        }

        cursor.close();

        shuffle(options);

        Question nextQuestion = new Question(
                questionId,
                questionText,
                questionAnswer,
                options.get(0),
                options.get(1),
                options.get(2),
                options.get(3)
        );

        return  nextQuestion;

    }

    public List<Result> getResults(String userID){
        if (!mDatabase.isOpen()){
            this.open();
        }

        List<Result> options = new ArrayList<>();

        // Current player stats
        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_RESULT,
                new String[]{
                        DatabaseHelper.COLUMN_RS_ID,
                        DatabaseHelper.COLUMN_RS_USERNAME,
                        DatabaseHelper.COLUMN_RS_SCORE },
                DatabaseHelper.COLUMN_RS_ID + " = ? ",
                new String[]{ userID },
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            Result option = new Result();
            option.setUser(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RS_USERNAME)));
            option.setScore(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RS_SCORE)));

            options.add(option);
        }

        cursor.close();

        // Top 10 answers

        cursor = mDatabase.query(
                DatabaseHelper.TABLE_RESULT,
                new String[]{
                        DatabaseHelper.COLUMN_RS_ID,
                        DatabaseHelper.COLUMN_RS_USERNAME,
                        DatabaseHelper.COLUMN_RS_SCORE},
                DatabaseHelper.COLUMN_RS_ID + " != ? ",
                new String[]{userID},
                null,
                null,
                DatabaseHelper.COLUMN_RS_SCORE + " DESC, " + DatabaseHelper.COLUMN_RS_ID + " DESC",
                "10"
        );

        while(cursor.moveToNext()){
            Result option = new Result();
            option.setUser(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RS_USERNAME)));
            option.setScore(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RS_SCORE)));

            options.add(option);
        }

        cursor.close();

        return options;
    }
}
