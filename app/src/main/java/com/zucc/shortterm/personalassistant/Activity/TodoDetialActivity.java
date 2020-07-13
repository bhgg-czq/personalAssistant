package com.zucc.shortterm.personalassistant.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.zucc.shortterm.personalassistant.Bean.BeanTodo;
import com.zucc.shortterm.personalassistant.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TodoDetialActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail);


        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        BeanTodo.Content item=(BeanTodo.Content)getIntent().getSerializableExtra("item_info");
        AppCompatCheckBox checkBox=findViewById(R.id.checkbox);
        TextView time=findViewById(R.id.time);
        ImageView pri=findViewById(R.id.pri);
        EditText title=findViewById(R.id.title);
        EditText detail=findViewById(R.id.detail);

        checkBox.setChecked((item.getHaveDown()==1)? true:false);
        time.setText(format.format(item.getDate()));
        title.setText(item.getName());
        switch (item.getPRI()){
            case 1:
                pri.setColorFilter(R.color.red);
                break;
            case 2:
                pri.setColorFilter(R.color.yellow);
                break;
            case 3:
                pri.setColorFilter(R.color.colorTheme);
                break;
            case 0:
                pri.setColorFilter(R.color.gray);
                break;
        }

        if (item.getDetail()!=null)
            detail.setText(item.getDetail());
    }
}
