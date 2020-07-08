package com.zucc.shortterm.personalassistant.Bean;

import java.util.ArrayList;
import java.util.Date;

public class BeanRecordGroup {
    private Date date;
    private ArrayList<BeanRecord> list;

    public BeanRecordGroup(Date date,ArrayList<BeanRecord> list){
        this.date = date;
        this.list = list;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<BeanRecord> getList() {
        return list;
    }

    public void setList(ArrayList<BeanRecord> list) {
        this.list = list;
    }
}
