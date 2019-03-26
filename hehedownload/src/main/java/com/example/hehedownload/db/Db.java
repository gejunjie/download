package com.example.hehedownload.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hehedownload.data.DownloadData;

import static com.example.hehedownload.data.Consts.PROGRESS;

public class Db {

    private static final String DB_NAME = "benboerboluo";

    private static final int VERSION = 2;
    //数据库表名
    private String TABLE_NAME_DOWNLOAD = "download_info";
//    volatlie防止指令重排序 可能会重排序为1->3->2
//    memory = allocate（）;    // 1.分配对象的内存空间
//    ctorInstance（memory）;    // 2.初始化对象
//    instance = memory;    // 3.设置instance指向刚才分配的内存地址
    private volatile static Db db;

    private SQLiteDatabase sqldb;

    private Db(Context context){
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context,DB_NAME,null,VERSION);
        sqldb = dbOpenHelper.getWritableDatabase();
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
        values.put("current_length",data.getCurrentLength());
        values.put("total_length",data.getTotalLength());
        values.put("percentage",data.getPercentage());
        values.put("status",data.getStatus());
        values.put("child_task_count",data.getChildTaskCount());
        values.put("date",data.getDate());
        values.put("last_modify",data.getLastModify());

        sqldb.insert(TABLE_NAME_DOWNLOAD,null,values);
    }

    public DownloadData getData(String url){

        Cursor cursor = sqldb.query(TABLE_NAME_DOWNLOAD, null, "url = ?",
                new String[]{url}, null, null, null);

        if (!cursor.moveToFirst()) {
            return null;
        }

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
        sqldb.update(TABLE_NAME_DOWNLOAD, values, "url = ?", new String[]{url});
    }

    /**
     * 删除下载信息
     */
    public void deleteData(String url) {
        sqldb.delete(TABLE_NAME_DOWNLOAD, "url = ?", new String[]{url});
    }
}
