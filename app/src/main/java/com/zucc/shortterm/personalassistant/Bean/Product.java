package com.zucc.shortterm.personalassistant.Bean;

public class Product {
    private int id;
    private String companyName;
    private String rate;
    private int term;
    private String des;
    private int startPoint;
    private String risk;

    public Product(){

    }
    public Product(int id,String companyName,String rate,int term,String des,int startPoint,String risk){
        this.id = id;
        this.companyName = companyName;
        this.rate = rate;
        this.term = term;
        this.des = des;
        this.startPoint = startPoint;
        this.risk = risk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}
