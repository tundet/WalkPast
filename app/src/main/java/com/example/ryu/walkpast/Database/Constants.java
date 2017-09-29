package com.example.ryu.walkpast.Database;

/**
 * Created by RYU on 9/29/2017.
 */

public class Constants {

    //columns
    static final String ROW_ID = "ID";
    static final String STORY = "STORY";
    static final String CHOICE1 = "CHOICE1";
    static final String CHOICE2 = "CHOICE2";

    //db properties
    public static final String DATABASE_NAME = "Stories.db";
    public static final String TABLE_NAME = "student_table";
    static final int DB_VERSION=1;

    //table creation statement
    static final String CREATE_TB="CREATE TABLE lv_TB(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT NOT NULL,category TEXT NOT NULL);";

    //table deletion statement
    static final String DROP_TB="DROP TABLE IF EXISTS "+TABLE_NAME;
}