package com.example.personalquizcontest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbConnection extends SQLiteOpenHelper {

    private SQLiteDatabase db = this.getWritableDatabase();
    private ContentValues values = new ContentValues();
    private ContentValues values1 = new ContentValues();

    private static final String dbName = "Quiz.db";
    private static final String USER_TABLE = "create table user (id integer primary key AUTOINCREMENT, fname text, lname text, nname text, age integer);";
    private static final String SCORE = "create table score(id integer primary key AUTOINCREMENT, score integer, userid integer, time text);";
    private static final String USER = "user";
    public static final String FNAME = "fname";
    public static final String LNAME = "lname";
    public static final String NNAME = "nname";
    public static final String AGE = "age";
    public static final String USERID = "userid";
    public static final String TIME = "time";
    public static final String SCORE1 = "score";

    SQLiteDatabase sqLiteDatabase;

    // Getting DB Context
    public DbConnection(Context context) {
        super(context, dbName, null, 1);
        sqLiteDatabase = getWritableDatabase();
    }

    // Creating DB
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(SCORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    // Saving user data
    public void saveData(User user){
        values.put(USERID, user.getId());
        values.put(FNAME, user.getFirstname());
        values.put(LNAME, user.getLastname());
        values.put(NNAME, user.getNickname());
        values.put(AGE, user.getAge());
    }

    // Loading user data
    public ArrayList<User> loadData(){
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        final Integer NUM = 1;
        final String LOAD = "SELECT * FROM user WHERE id = "+ NUM  +" ORDER BY id DESC LIMIT " + NUM;
        final String LOAD1 = "SELECT * FROM score WHERE userid = "+ NUM  +" ORDER BY userid DESC LIMIT " + NUM;
        Cursor cursor = db.rawQuery(LOAD, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setFirstname(cursor.getString(1));
                user.setLastname(cursor.getString(2));
                user.setNickname(cursor.getString(3));
                user.setAge(cursor.getInt(4));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    // Saving user score to DB
    public void saveToDB(int score, int userid, String date) {
        values.put(SCORE1, score);
        values.put(USERID, userid);
        values.put(TIME, date);
        db.insert(SCORE, null, values);
    }

    // Saving user info into DB
    public int insertUser(User user) {
        values.put(FNAME, user.getFirstname());
        values.put(LNAME, user.getLastname());
        values.put(NNAME, user.getNickname());
        values.put(AGE, user.getAge());
        int userId = (int) db.insert(USER, null, values);
        return userId;
    }
}