package com.zucc.shortterm.personalassistant.Bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BeanTodo implements Serializable{

    private String name;
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


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BeanTodo(){

    }
  public  static class Title extends BeanTodo {
        public Title(String name) {

            super.setName(name);
        }
    }


   public  static class Content extends BeanTodo implements Serializable {

           private int id;

           private String detail;
           private int haveDown;
           private Date date;
           private int remind;
           private int PRI;
           private String tag;

            public Content(int id,String name,String detail,int haveDown,Date date,int remind,int PRI,String tag) {
                this.id = id;
                super.setName(name);
                this.detail = detail;
                this.haveDown = haveDown;
                this.date = date;
                this.remind = remind;
                this.PRI = PRI;
                this.tag = tag;


                }


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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











}
