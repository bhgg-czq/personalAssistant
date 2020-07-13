package com.zucc.shortterm.personalassistant.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.zucc.shortterm.personalassistant.R;

public class LoginActivity extends AppCompatActivity {

    private ImageButton back;

    private SharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.login_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final EditText number=findViewById(R.id.number);
        final EditText pwd=findViewById(R.id.login_pwd);
        Button button=findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().toString().equals("13588376250"))
                    if (pwd.getText().toString().equals("1234567")){
                        Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("isLogin",true);


                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        //步骤2-2：将获取过来的值放入文件
                        editor.putBoolean("isLogin",true);
                        //步骤3：提交
                        editor.commit();


                        startActivity(intent);
                    }
            }
        });

    }
}
