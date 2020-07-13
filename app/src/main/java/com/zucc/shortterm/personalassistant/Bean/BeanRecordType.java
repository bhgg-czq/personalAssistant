package com.zucc.shortterm.personalassistant.Bean;

import android.graphics.Bitmap;

import java.sql.Blob;
import java.util.List;

public class BeanRecordType {
    private int id;
    private Bitmap icon;
    private String name;
    private int color;
    private double sum;
    public static List<BeanRecordType> outlist;
    public static List<BeanRecordType> inlist;

    public static void setList(List<BeanRecordType> outlist,List<BeanRecordType> inlist){
        BeanRecordType.outlist = outlist;
        BeanRecordType.inlist = inlist;
    }

    public BeanRecordType(){
        this.sum = 0;
    };

    public BeanRecordType(int i,Bitmap icon,String name){
        this.id = i;
        this.icon = icon;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
