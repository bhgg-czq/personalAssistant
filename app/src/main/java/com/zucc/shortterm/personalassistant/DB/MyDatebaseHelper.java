package com.zucc.shortterm.personalassistant.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




public class MyDatebaseHelper extends SQLiteOpenHelper {
    private static final String DATEBASE_NAME = "PA.db";
    private static final int DATEBASE_VERSION = 1;

    public MyDatebaseHelper(Context context){
        super(context,DATEBASE_NAME,null,DATEBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建待办表
        db.execSQL("create table if not exists todoList"+
                "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "title TEXT,"+
                "detail TEXT,"+
                "haveDown INTEGER,"+ //0：未完成 1:完成
                "date TEXT,"+
                "remind INTEGER,"+//0：无 1：当天 2：提前1天 3：提前2天 4：提前3天 5：提前一周
                "PRI INTEGER,"+ //1：高优先级 2：中优先级 3：低优先级 4：无优先级
                "tag TEXT)");
        //创建
        db.execSQL("create table if not exists records"+
                "(id INTEGER  PRIMARY KEY AUTOINCREMENT,"+
                "sum REAL,"+
                "isIn INTEGER,"+
                "type INTEGER,"+
                "date TEXT,"+
                "memo TEXT)");

//        db.execSQL("create table if not exists recordType"+
//                "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
//                "name TEXT,"+
//                "icon BLOB)")+
//                "insert into recordType(name,icon)" +
//                "values"+
//                "('餐饮',"+readImage()+"";    session.beginTransaction();



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }



}
