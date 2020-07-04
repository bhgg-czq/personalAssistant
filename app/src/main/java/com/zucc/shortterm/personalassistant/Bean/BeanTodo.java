package com.zucc.shortterm.personalassistant.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BeanTodo {
    private int id;
    private String title;
    private String detail;
    private int haveDown;
    private Date date;
    private int remind;
    private int PRI;
    private String tag;

    public BeanTodo(){

    }

    public BeanTodo(int id,String title,String detail,int haveDown,String date,int remind,int PRI,String tag){
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.haveDown = haveDown;
        this.date = formatDate(date);
        this.remind = remind;
        this.PRI = PRI;
        this.tag = tag;
    }

    private Date formatDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try{
            Date d = sdf.parse(date);
            return d;
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getHaveDown() {
        return haveDown;
    }

    public void setHaveDown(int haveDown) {
        this.haveDown = haveDown;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    public int getPRI() {
        return PRI;
    }

    public void setPRI(int PRI) {
        this.PRI = PRI;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
