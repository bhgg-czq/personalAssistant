package com.zucc.shortterm.personalassistant.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zucc.shortterm.personalassistant.Bean.BeanRecord;
import com.zucc.shortterm.personalassistant.Bean.BeanTodo;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyDatebaseManager {
    private MyDatebaseHelper helper;
    private SQLiteDatabase db;

    public MyDatebaseManager(Context context){
        helper = new MyDatebaseHelper(context);
        db = helper.getWritableDatabase();
    }
    public void addTodo(BeanTodo.Content todos) {
        ContentValues values=new ContentValues();
        values.put("title",todos.getName());
        values.put("remind",todos.getRemind());
        values.put("date",formatDateSecond(todos.getDate()));

        values.put("haveDown",0);
        values.put("PRI",todos.getPRI());
        values.put("repeat",todos.getRepeat() );

        Log.d("date", formatDateSecond(todos.getDate()));
        db.insert("todoList",null,values);
    }
    public void editTodo(BeanTodo.Content todos) {
        Log.d("fdsf",todos.getDetail());
        Log.d("fdsf",String.valueOf(todos.getPRI()));
        Log.d("fdsf",String.valueOf(todos.getHaveDown()));
       String sql="UPDATE todolist SET detail = '"+todos.getDetail()+"', pri = "+todos.getPRI()+", havedown = "+todos.getHaveDown()+" ,title = '"+todos.getName() +" ' WHERE id = "+todos.getId();

       db.execSQL(sql);
    }

    public ArrayList<BeanTodo.Content> getTodoList(){
        ArrayList<BeanTodo.Content> records = new ArrayList<>();
        String sql = "select * from todoList";
        Cursor c = db.rawQuery(sql,null);
        while(c.moveToNext()){
            Log.d("lookatdate", String.valueOf(c.getColumnIndex("date")));
            BeanTodo.Content beanRecord = new BeanTodo.Content(c.getInt(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("detail")),
                    c.getInt(c.getColumnIndex("haveDown")),
                    formatStringSecond(c.getString(c.getColumnIndex("date"))),
                    c.getInt(c.getColumnIndex("remind")),
                    c.getInt(c.getColumnIndex("PRI")),
                    c.getInt(c.getColumnIndex("repeat")));
            records.add(beanRecord);
        }
        return records;
    }



    public void addRecord(double sum, int type, Date date, String memo,int isIn){
        ContentValues values = new ContentValues();
        values.put("sum",sum);
        values.put("type",type);
        values.put("date",formatDate(date));
        values.put("memo",memo);
        values.put("isIn",isIn);
        db.insert("records",null,values);
    }

    public List<Date> getRecordDates(){
        List<Date> dates = new ArrayList<>();
        String sql = "select distinct date from records order by date DESC";
        Cursor c = db.rawQuery(sql,null);
        while(c.moveToNext()){
            Date date = formatString(c.getString(c.getColumnIndex("date")));
            dates.add(date);
        }
        return dates;
    }

    public ArrayList<BeanRecord> getRecordsByDate(Date date){
        ArrayList<BeanRecord> records = new ArrayList<>();
        String d = formatDate(date);
        String sql = "select * from records where date = '"+d+"'";
        Cursor c = db.rawQuery(sql,null);
        while(c.moveToNext()){
            Log.d("getdate", c.getString(c.getColumnIndex("date")));
            BeanRecord beanRecord = new BeanRecord(c.getInt(c.getColumnIndex("id")),
                    c.getDouble(c.getColumnIndex("sum")),
                    c.getInt(c.getColumnIndex("type")),
                    c.getInt(c.getColumnIndex("isIn")),
                    formatString(c.getString(c.getColumnIndex("date"))),
                    c.getString(c.getColumnIndex("memo")));
            records.add(beanRecord);
        }
        return records;
    }

    public ArrayList<BeanRecord> getRecordsByMonth(Date date){
        ArrayList<BeanRecord> records = new ArrayList<>();
        String d,d2;
        if(date.getMonth() < 9){
            d = (date.getYear()+1900)+"-0"+(date.getMonth()+1)+"-01";
            d2 = (date.getYear()+1900)+"-0"+(date.getMonth()+1)+"-31";
        }else{
            d = (date.getYear()+1900)+"-"+(date.getMonth()+1)+"-01";
            d2 = (date.getYear()+1900)+"-"+(date.getMonth()+1)+"-31";
        }

        String sql = "select * from records "+
                "WHERE date BETWEEN '"+d+"' AND '"+d2+"'";
        System.out.println(sql+"sql");
        Cursor c = db.rawQuery(sql,null);
        while(c.moveToNext()){
            BeanRecord beanRecord = new BeanRecord(c.getInt(c.getColumnIndex("id")),
                    c.getDouble(c.getColumnIndex("sum")),
                    c.getInt(c.getColumnIndex("type")),
                    c.getInt(c.getColumnIndex("isIn")),
                    formatString(c.getString(c.getColumnIndex("date"))),
                    c.getString(c.getColumnIndex("memo")));
            records.add(beanRecord);
        }
        return records;
    }

    public ArrayList<BeanRecord> getRecordsByType(int typeId){
        ArrayList<BeanRecord> list = new ArrayList<>();
        String sql = "select * from records where tyep = "+typeId;
        Cursor c = db.rawQuery(sql,null);
        while(c.moveToNext()){
            BeanRecord beanRecord = new BeanRecord(c.getInt(c.getColumnIndex("id")),
                    c.getDouble(c.getColumnIndex("sum")),
                    c.getInt(c.getColumnIndex("type")),
                    c.getInt(c.getColumnIndex("isIn")),
                    formatString(c.getString(c.getColumnIndex("date"))),
                    c.getString(c.getColumnIndex("memo")));
            list.add(beanRecord);
        }
        return list;
    }

    public Boolean deleteRecord(int id){
        String sql = "delete from records where id = "+id;
        db.execSQL(sql);
        return true;
    }

    public ArrayList<BeanRecord> getAllRecords(){
        ArrayList<BeanRecord> records =new ArrayList<>();
        Cursor c = db.query("records",null,null,null,null,null,null);
        while(c.moveToNext()){
            BeanRecord beanRecord = new BeanRecord(c.getInt(c.getColumnIndex("id")),
                    c.getDouble(c.getColumnIndex("sum")),
                    c.getInt(c.getColumnIndex("isIn")),
                    c.getInt(c.getColumnIndex("type")),
                    formatString(c.getString(c.getColumnIndex("date"))),
                    c.getString(c.getColumnIndex("memo")));
            records.add(beanRecord);
        }
        c.close();
        return records;
    }

    public String formatDate(Date date){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        return sdf.format(date);
    }
    public String formatDateSecond(Date date){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        return sdf.format(date);
    }
    public Date formatStringSecond(String str){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        try{
            Date date = sdf.parse(str);
            return date;
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public Date formatString(String str){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        try{
            Date date = sdf.parse(str);
            return date;
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
    }

}
