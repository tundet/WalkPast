package com.example.ryu.walkpast.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ryu.walkpast.Model.Choice;
import com.example.ryu.walkpast.Model.Page;

import java.util.ArrayList;

/**
 * HANDLES THE CONTENT OF DATABASE
 * saves and gets pages and choices.
 * Created by RYU on 10/3/2017.
 */

public class DatabaseAdapter {

    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper helper;
    private ArrayList<Choice> choices;
    private ArrayList<Page> pages;

    /*
     1. INITIALIZE DB HELPER AND PASS IT A CONTEXT
     */
    public DatabaseAdapter(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
        choices = retrieveChoices();
        pages = retrievePages();
    }

    /*
     SAVE PAGE TO DB
     */
    public boolean savePage(Page page) {
        try {
            db = helper.getWritableDatabase();
            ContentValues pagecv = new ContentValues();
            pagecv.put(Constants.STORY, page.getStory());
            pagecv.put(Constants.CHOICE1, page.getChoice1());
            pagecv.put(Constants.CHOICE2, page.getChoice2());
            pagecv.put(Constants.BACKGROUND, page.getBackground());
            pagecv.put(Constants.STEPS, page.getSteps());
            long result = db.insert(Constants.TB1_NAME, Constants.ROW_ID, pagecv);
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return false;
    }

    /*
     SAVE CHOICE TO DB
     */
    public boolean saveChoice(Choice choice) {
        try {
            db = helper.getWritableDatabase();
            ContentValues choicecv = new ContentValues();
            choicecv.put(Constants.TEXT, choice.getText());
            choicecv.put(Constants.PAGE, choice.getNextPage());
            long result = db.insert(Constants.TB2_NAME, Constants.ROW_ID, choicecv);
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return false;
    }

    /*
     1. RETRIEVE PAGES FROM DB AND POPULATE ARRAYLIST
     2. RETURN THE LIST
     */
    private ArrayList<Page> retrievePages() {
        ArrayList<Page> pages = new ArrayList<>();
        try {
            db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.TB1_NAME, null);
            Page page;
            pages.clear();
            while (cursor.moveToNext()) {
                String pstory = cursor.getString(1);
                int pchoice1 = cursor.getInt(2);
                int pchoice2 = cursor.getInt(3);
                String pbackground = cursor.getString(4);
                int psteps = cursor.getInt(5);
                page = new Page(pstory, pchoice1, pchoice2, pbackground, psteps);
                pages.add(page);
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return pages;
    }

    /*
     1. RETRIEVE CHOICES FROM DB AND POPULATE ARRAYLIST
     2. RETURN THE LIST
     */
    private ArrayList<Choice> retrieveChoices() {
        ArrayList<Choice> choices = new ArrayList<>();
        try {
            db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.TB2_NAME, null);
            Choice choice;
            choices.clear();
            while (cursor.moveToNext()) {
                String ctext = cursor.getString(1);
                int cpage = cursor.getInt(2);
                choice = new Choice(ctext, cpage);
                choices.add(choice);
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return choices;
    }

    public Choice getChoice(int id) {
        return choices.get(id);
    }

    public Page getPage(int id) {
        return pages.get(id);
    }
}