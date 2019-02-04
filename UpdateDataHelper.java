package com.nepali.nepali_app.nepali_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UpdateDataHelper extends SQLiteOpenHelper {

    private static final String TAG = "UpdateDataHelper";
    private static final String Table_name = "words";
    private static final String COL1 = "ID";
    private static final String COL2 = "origin";
    private static final String COL3 = "M";
    private static final String COL4 = "S";
    private static final String COL5 = "role";
    private static final String COL6 = "checking";
    private static final String COL7 = "counts";

    public UpdateDataHelper(Context context) {
        super(context,Table_name,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = "create table IF NOT EXISTS "+Table_name+ "(ID INTEGER PRIMARY KEY, origin TEXT,M TEXT,S TEXT,role TEXT,counts INTEGER DEFAULT 0 ,checking INTEGER DEFAULT 0)";
        db.execSQL(CreateTable);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addWord(int id, String origin,String M,String S,String role,int counts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id);
        contentValues.put(COL2, origin);
        contentValues.put(COL3, M);
        contentValues.put(COL4, S);
        contentValues.put(COL5, role);
        contentValues.put(COL7, counts);
        long result = db.insert(Table_name, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from "+Table_name;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getQuizWord(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select ID,origin,M,S,role from "+Table_name+" where checking = 0 order by counts desc LIMIT 1 ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getQuizAnswers(int id, String role){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select M from "+Table_name+" where role ='"+role+"' AND id != "+id+" Order BY RANDOM() LIMIT 3";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public long getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db,Table_name);
    }

}
