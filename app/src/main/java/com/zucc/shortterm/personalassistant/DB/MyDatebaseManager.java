package com.zucc.shortterm.personalassistant.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MyDatebaseManager {
    private MyDatebaseHelper helper;
    private SQLiteDatabase db;

    public MyDatebaseManager(Context context){
        helper = new MyDatebaseHelper(context);
        db = helper.getWritableDatabase();
    }


}
