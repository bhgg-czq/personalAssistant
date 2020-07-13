package com.zucc.shortterm.personalassistant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.zucc.shortterm.personalassistant.Bean.BeanTodo;
import com.zucc.shortterm.personalassistant.DB.MyDatebaseManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TodoDetialActivity extends AppCompatActivity {
    //数据库实例
    private MyDatebaseManager dbmanager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail);


        dbmanager = new MyDatebaseManager(this);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //得到的item
        final BeanTodo.Content item=(BeanTodo.Content)getIntent().getSerializableExtra("item_info");


        final AppCompatCheckBox checkBox=findViewById(R.id.checkbox);
        TextView time=findViewById(R.id.time);
        ImageView pri=findViewById(R.id.pri);
        final EditText title=findViewById(R.id.title);
        final EditText detail=findViewById(R.id.detail);
        ImageButton back=findViewById(R.id.goback);

        checkBox.setChecked((item.getHaveDown()==1)? true:false);
        time.setText(format.format(item.getDate()));
        title.setText(item.getName());
        switch (item.getPRI()){
            case 1:
                pri.setColorFilter(getResources().getColor(R.color.red));
                break;
            case 2:
                pri.setColorFilter(getResources().getColor(R.color.yellow));
                break;
            case 3:
                pri.setColorFilter(getResources().getColor(R.color.colorTheme));
                break;
            case 0:
                pri.setColorFilter(getResources().getColor(R.color.gray));
                break;
        }

        if (item.getDetail()!=null)
            detail.setText(item.getDetail());



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setDetail(detail.getText().toString());
                item.setHaveDown((checkBox.isChecked()==true)? 1:0);
                item.setName(title.getText().toString());
                dbmanager.editTodo(item);

                Intent data=new Intent();
                data.putExtra("item_info",item);
                Log.d("fdfd",String.valueOf(item.getRemind()));
                setResult(1,data);
                finish();
            }
        });
    }
}
