package com.zucc.shortterm.personalassistant.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.zucc.shortterm.personalassistant.Bean.BeanTodo;
import com.zucc.shortterm.personalassistant.MainActivity;

public class MyDatebaseManager {
    private MyDatebaseHelper helper;
    private SQLiteDatabase db;

    public MyDatebaseManager(Context context){
        helper = new MyDatebaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public void addTodo(BeanTodo todos) {
        ContentValues values=new ContentValues();
        values.put("title",todos.getTitle());

        values.put("remind",todos.getRemind());
        if (todos.getRemind()!=0){
            values.put("date",todos.getDate().toString());
        }
        values.put("haveDown",0);
        values.put("PRI",todos.getPRI());

    }


}
