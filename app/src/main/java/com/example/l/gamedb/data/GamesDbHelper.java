package com.example.l.gamedb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.l.gamedb.data.GameContract.*;


public class GamesDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "gamesDb.db";
    private static final int VERSION = 1;
    public GamesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE "  + GameEntry.TABLE_NAME + " (" +
                GameEntry.COLUMN_ID + " INTEGER PRIMARY KEY);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
