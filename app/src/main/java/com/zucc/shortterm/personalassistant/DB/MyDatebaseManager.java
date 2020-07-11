package com.zucc.shortterm.personalassistant.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zucc.shortterm.personalassistant.Bean.BeanRecord;
import com.zucc.shortterm.personalassistant.Bean.BeanTodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyDatebaseManager {
    private MyDatebaseHelper helper;
    private SQLiteDatabase db;

    public MyDatebaseManager(Context context){
        helper = new MyDatebaseHelper(context);
        db = helper.getWritableDatabase();
    }
    public void addTodo(BeanTodo todos) {
//        ContentValues values=new ContentValues();
//        values.put("title",todos.getTitle());
//
//        values.put("remind",todos.getRemind());
//        if (todos.getRemind()!=0){
//            values.put("date",todos.getDate().toString());
//        }
//        values.put("haveDown",0);
//        values.put("PRI",todos.getPRI());

    }

    public void addRecord(double sum, int type, Date date, String memo){
        ContentValues values = new ContentValues();
        values.put("sum",sum);
        values.put("type",type);
        values.put("date",formatDate(date));
        values.put("memo",memo);
        db.insert("records",null,values);
    }

    public ArrayList<BeanRecord> getAllRecords(){
        ArrayList<BeanRecord> dates =new ArrayList<>();
        Cursor c = db.query("records",null,null,null,null,null,null);
        while(c.moveToNext()){
            BeanRecord beanRecord = new BeanRecord(c.getInt(c.getColumnIndex("id")),
                    c.getDouble(c.getColumnIndex("sum")),
                    c.getInt(c.getColumnIndex("type")),
                    formatString(c.getString(c.getColumnIndex("date"))),
                    c.getString(c.getColumnIndex("memo")));
            dates.add(beanRecord);
        }
        c.close();
        return dates;
    }

    public String formatDate(Date date){
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd" );
        return sdf.format(date);
    }
    public Date formatString(String str){
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd" );
        try{
            Date date = sdf.parse(str);
            return date;
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
    }

}
