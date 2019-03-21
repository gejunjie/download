package com.example.hehedownload.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hehedownload.data.DownloadData;

import static com.example.hehedownload.data.Consts.PROGRESS;

public class Db {

    private static final String DB_NAME = "db_okdown";

    private static final int VERSION = 2;

    private String TABLE_NAME_OKDOWN = "okdown_info";

    private static Db db;

    private SQLiteDatabase database;

    private Db(Context context){
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context,DB_NAME,null,VERSION);
        database = dbOpenHelper.getWritableDatabase();
    }

    public static Db getInstance(Context context){
        if (db == null){
            synchronized (Db.class){
                if (db == null){
                    db = new Db(context);
                }
            }
        }
        return db;
    }

    /**
     * 保存下载信息
     */
    public void insertData(DownloadData data){
        ContentValues values = new ContentValues();
        values.put("url",data.getUrl());
        values.put("path",data.getPath());
        values.put("name",data.getName());
        values.put("currentLength",data.getCurrentLength());
        values.put("totalLength",data.getTotalLength());
        values.put("percentage",data.getPercentage());
        values.put("status",data.getStatus());
        values.put("childTaskCount",data.getChildTaskCount());
        values.put("date",data.getDate());
        values.put("lastModify",data.getLastModify());

        database.insert(DB_NAME,null,values);
    }

    public DownloadData getData(String url){
        Cursor cursor = database.query(DB_NAME, null, "url = ?", new String[]{url},
                null, null, null);
        DownloadData downloadData = new DownloadData();
        downloadData.setUrl(cursor.getString(cursor.getColumnIndex("url")));
        downloadData.setPath(cursor.getString(cursor.getColumnIndex("path")));
        downloadData.setName(cursor.getString(cursor.getColumnIndex("name")));
        downloadData.setCurrentLength(cursor.getInt(cursor.getColumnIndex("currentLength")));
        downloadData.setTotalLength(cursor.getInt(cursor.getColumnIndex("totalLength")));
        downloadData.setPercentage(cursor.getFloat(cursor.getColumnIndex("percentage")));
        downloadData.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        downloadData.setChildTaskCount(cursor.getInt(cursor.getColumnIndex("childTaskCount")));
        downloadData.setDate(cursor.getLong(cursor.getColumnIndex("date")));
        downloadData.setLastModify(cursor.getString(cursor.getColumnIndex("lastModify")));
        cursor.close();
        return downloadData;
    }

    public void updateProgress(int currentSize, float percentage, int status, String url){
        ContentValues values = new ContentValues();
        if (status != PROGRESS){
            values.put("current_length", currentSize);
            values.put("percentage", percentage);
        }
        values.put("status", status);
        database.update(DB_NAME, values, "url = ?", new String[]{url});
    }
}
