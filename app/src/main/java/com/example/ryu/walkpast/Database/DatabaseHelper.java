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
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    /*
    CREATE TABLE
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            db.execSQL(Constants.CREATE_TB1);
            db.execSQL(Constants.CREATE_TB2);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /*
        UPGRADE TABLE
         */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL(Constants.DROP_TB1);
            db.execSQL(Constants.DROP_TB2);
            db.execSQL(Constants.CREATE_TB1);
            db.execSQL(Constants.CREATE_TB2);

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
