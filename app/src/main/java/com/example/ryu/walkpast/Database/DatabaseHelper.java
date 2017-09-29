package com.example.ryu.walkpast.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RYU on 9/29/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DB_VERSION);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }

    //create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try
        {
            sqLiteDatabase.execSQL(Constants.CREATE_TB);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //upgrade table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL(Constants.DROP_TB);
            sqLiteDatabase.execSQL(Constants.CREATE_TB);

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
