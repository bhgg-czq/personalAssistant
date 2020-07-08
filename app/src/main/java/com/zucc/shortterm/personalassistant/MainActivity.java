package com.zucc.shortterm.personalassistant;

import android.app.Dialog;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.zucc.shortterm.personalassistant.Bean.BeanTodo;
import com.zucc.shortterm.personalassistant.DB.MyDatebaseManager;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,BottomNavigationBar.OnTabSelectedListener{
    private BottomNavigationBar bottomNavigationBar;
    private Dialog dialog,dialog2;
    private View inflate,inflate2;
    private EditText nav_dialog_edit;
    private Button nav_dialog_button;
    private MyDatebaseManager myDatebaseManager;
    private TextView textRankH,textRankN,textRankM,textRankL;
    private ImageView nav_dialog_remind,nav_dialog_pri;
    private int pri=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        myDatebaseManager=new MyDatebaseManager(this);


        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContentDialog();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        intiBottomNavigationBar();

        bottomNavigationBar.setTabSelectedListener(this);


        init();


    }

    public void init(){
        dialog2 = new Dialog(this,R.style.PRIDialogStyle);
        inflate2 = LayoutInflater.from(this).inflate(R.layout.pri_dialog, null);
        textRankH=inflate2.findViewById(R.id.rankH);
        textRankM=inflate2.findViewById(R.id.rankM);
        textRankL=inflate2.findViewById(R.id.rankL);
        textRankN=inflate2.findViewById(R.id.rankN);
        dialog2.setContentView(inflate2);
        //获取当前Activity所在的窗体
        Window dialogWindow2 = dialog2.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow2.setGravity( Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow2.getAttributes();
        lp.y=500;
        lp.x=-250;
//       将属性设置给窗体
        dialogWindow2.setAttributes(lp);
    }

    public void onClickRankH(View view){
        nav_dialog_edit.setTextColor(0xffe21e29);
        pri=1;
        nav_dialog_pri.setColorFilter(0xffe21e29);
    }
    public void onClickRankM(View view){
        nav_dialog_edit.setTextColor(0xffe5b728);
        pri=2;
        nav_dialog_pri.setColorFilter(0xffe5b728);
    }
    public void onClickRankL(View view){
        nav_dialog_edit.setTextColor(0xff667ed6);
        pri=3;
        nav_dialog_pri.setColorFilter(0xff667ed6);
    }
    public void onClickRankN(View view){
        nav_dialog_edit.setTextColor(0xff8a8a8a);
        pri=4;
        nav_dialog_pri.setColorFilter(0xff8a8a8a);
    }


   public void intiBottomNavigationBar(){
       bottomNavigationBar= findViewById(R.id.nav_bottom);
       bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
       bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
       bottomNavigationBar.setBarBackgroundColor(R.color.white);
       bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.icon_todo_1, "备忘录").setActiveColorResource(R.color.colorTheme))
               .addItem(new BottomNavigationItem(R.drawable.icon_record_1, "记账").setActiveColorResource(R.color.colorTheme))
               .addItem(new BottomNavigationItem(R.drawable.icon_setting_1, "设置").setActiveColorResource(R.color.colorTheme))
               .setFirstSelectedPosition(0)
               .initialise(); //所有的设置需在调用该方法前完成
    }

    @Override
    public void onTabSelected(int position) {
        switch (position){
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;

        }

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }


    //弹窗
    private void showContentDialog() {
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.add_info_dialog, null);
        nav_dialog_edit = (EditText) inflate.findViewById(R.id.nav_dialog_edit);
        nav_dialog_remind = (ImageView) inflate.findViewById(R.id.nav_dialog_remind);
        nav_dialog_pri = (ImageView) inflate.findViewById(R.id.nav_dialog_pri);
        nav_dialog_button = (Button) inflate.findViewById(R.id.nav_dialog_button);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y=20;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框


        nav_dialog_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()==0){
                    nav_dialog_button.setEnabled(false);
                    nav_dialog_button.setTextColor(0xff8a8a8a);
                }

                else{
                    nav_dialog_button.setTextColor(0xff667ed6);
                }

            }
        });
        nav_dialog_pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("aaaaa","aaa");
//                System.out.println("fdfdf");

                dialog2.show();
            }
        });
    }



    //点击确定添加记录
    public void onClickSure(View v){
        BeanTodo t=new BeanTodo();
        if (!nav_dialog_edit.getText().toString().isEmpty())
        {
            t.setTitle(nav_dialog_edit.getText().toString());
            Log.d("a",nav_dialog_edit.getText().toString());
            t.setPRI(pri);
            t.setRemind(0);
            myDatebaseManager.addTodo(t);
            Log.d("record","添加成功");
            pri=0;
        }
        else{
            Log.d("a", "error");
        }

    }

    //侧边抽屉
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
