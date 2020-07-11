package com.zucc.shortterm.personalassistant.Bean;

import java.util.Date;

public class BeanRecord {

    private int id;
    private double sum;
    private int type;
    private Date date;
    private int in;
    private String memo;

    public BeanRecord(){};

    public BeanRecord(int id,double sum,int type,int inOut,Date date,String memo){
        this.id = id;
        this.sum = sum;
        this.type = type;
        this.date = date;
        this.memo = memo;
        this.in = inOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int inOut) {
        this.in = inOut;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
