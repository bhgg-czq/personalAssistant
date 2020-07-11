package com.zucc.shortterm.personalassistant.Bean;

import java.util.ArrayList;
import java.util.List;

public class BeanTodoHeader {
    private String title;
    private List<BeanTodo> mlist;

    public BeanTodoHeader(String title){
        this.title=title;
        mlist=new ArrayList<>();
    }
    public void additem(BeanTodo items){
        mlist.add(items);
    }

    public Object getitem(int position){
        if (position==0){
            return title;
        }
        else
            return mlist.get(position-1);
    }
    public int size(){
        return mlist.size()+1;
    }

}
