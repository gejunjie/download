package com.example.hehedownload.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String CTEATE_DOWNLOAD_INFO = "create table download_info ("
            +"id integer primary key autoincrement,"
            +"url text,"
            +"path text,"
            +"name text, "
            +"child_task_count integer,"
            +"current_length integer,"
            +"total_length integer,"
            +"percentage real,"
            +"status integer,"
            +"last_modify text,"
            +"date text )";


    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CTEATE_DOWNLOAD_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1 :
                db.execSQL("alter table download_info add column status integer");

            default:
        }
    }

}
