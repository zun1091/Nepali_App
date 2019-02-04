package com.nepali.nepali_app.nepali_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    private static final String Table_name = "login_info";
    private static final String COL1 = "ID";
    private static final String COL2 = "NAME";
    private static final String COL3 = "MANAGER";

    public DataBaseHelper(Context context) {
        super(context,Table_name,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = "create table IF NOT EXISTS "+Table_name+ "(ID TEXT PRIMARY KEY, NAME TEXT, MANAGER INTEGER)";
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addData(String id,String name, int manager){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,id);
        contentValues.put(COL2,name);
        contentValues.put(COL3,manager);
        db.delete(Table_name, null,null); //테이블의 모든 데이터를 지울 경우
        //db.delete("tablename","id=? and name=?",new String[]{"1","jack"}); 보통은 이런식으로 쓴다!!
        long result = db.insert(Table_name,null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from "+Table_name;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void delete_login(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Table_name, null,null);
    }



}
