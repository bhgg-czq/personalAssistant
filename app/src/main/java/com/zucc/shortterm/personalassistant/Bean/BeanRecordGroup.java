package com.zucc.shortterm.personalassistant.Bean;

import java.util.ArrayList;
import java.util.Date;

public class BeanRecordGroup {
    private Date date;
    private int inSum;
    private int outSum;
    private ArrayList<BeanRecord> list;

    public BeanRecordGroup(){
        this.inSum = 0;
        this.outSum = 0;
    }
    public BeanRecordGroup(Date date,ArrayList<BeanRecord> list){
        this.date = date;
        this.list = list;

    }

    public void addRecords(BeanRecord beanRecord){
        this.list.add(beanRecord);
        if(beanRecord.getIn() == 0){
            this.outSum += beanRecord.getSum();
        }else{
            this.inSum += beanRecord.getSum();
        }
    }

    public void deleteRecords(int position){
        if(list.get(position).getIn() == 0){
            this.outSum -= list.get(position).getSum();
        }else{
            this.inSum -= list.get(position).getSum();
        }
        this.list.remove(position);
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
        for(int i = 0;i<this.list.size();i++){
            if(this.list.get(i).getIn() == 0){
                this.outSum += this.list.get(i).getSum();
            }else{
                this.inSum += this.list.get(i).getSum();
            }
        }
    }

    public int getInSum() {
        return inSum;
    }

    public void setInSum(int inSum) {
        this.inSum = inSum;
    }

    public int getOutSum() {
        return outSum;
    }

    public void setOutSum(int outSum) {
        this.outSum = outSum;
    }
}
