package com.zucc.shortterm.personalassistant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.app.Dialog;
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
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.zucc.shortterm.personalassistant.Bean.BeanTodo;
import com.zucc.shortterm.personalassistant.DB.MyDatebaseManager;

import com.zucc.shortterm.personalassistant.Bean.BeanRecord;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordGroup;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordType;
import com.zucc.shortterm.personalassistant.Tools.RecordGroupAdapter;
import com.zucc.shortterm.personalassistant.Tools.RecordItemAdapter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BottomNavigationBar.OnTabSelectedListener{
        private BottomNavigationBar bottomNavigationBar;
    //include View
    private View content,record;
    //recordList
    private ListView recordList;
    //czq
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

        //include View
        content = findViewById(R.id.layout_content);
        record = findViewById(R.id.layout_record);

        //底部导航
        intiBottomNavigationBar();
        bottomNavigationBar.setTabSelectedListener(this);

        //czq
        initPRI();
        //record数据
        initRecord();
        recordList = (ListView)findViewById(R.id.recordList);
        ArrayList<BeanRecordGroup> groupList = new ArrayList<>();
        for(int i = 0;i<2;i++){
            Date date = new Date();
            final ArrayList<BeanRecord> itemList = new ArrayList<>();
            for(int j = 0;j<5;j++){
                BeanRecord beanRecord = new BeanRecord(i+1,100+i,BeanRecordType.outlist.get(i).getId(),new Date(),"test"+i);
                itemList.add(beanRecord);
            }
            BeanRecordGroup beanRecordGroup = new BeanRecordGroup(date,itemList);
            groupList.add(beanRecordGroup);
        }
        final RecordGroupAdapter recordGroupAdapter = new RecordGroupAdapter(this,groupList);
        recordList.setAdapter(recordGroupAdapter);
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

    public void initPRI(){
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
    @Override
    public void onTabSelected(int position) {
        switch (position){
            case 0:
                content.setVisibility(View.VISIBLE);
                record.setVisibility(View.GONE);
                break;
            case 1:
                content.setVisibility(View.GONE);
                record.setVisibility(View.VISIBLE);
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

    public void initRecord(){
        String[] str = {"餐饮","住房缴费","医疗","宠物","娱乐","旅游","酒店","运动","学习","人情"};
        String[] str2 = {"生意","退款","工资","红包","奖金"};
        List<Bitmap> list = new ArrayList<>();
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.food));
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.shenghuofuwu));
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.yiliao));
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.chongwu));
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.yule));
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.lvyou));
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.jiudian));
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.yundong ));
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.xuexi));
        list.add(BitmapFactory.decodeResource(getResources(),R.drawable.renqinglaiwang));
        List<Bitmap> list2 = new ArrayList<>();
        list2.add(BitmapFactory.decodeResource(getResources(),R.drawable.dianpu1));
        list2.add(BitmapFactory.decodeResource(getResources(),R.drawable.tuikuan));
        list2.add(BitmapFactory.decodeResource(getResources(),R.drawable.gongzi));
        list2.add(BitmapFactory.decodeResource(getResources(),R.drawable.hongbao));
        list2.add(BitmapFactory.decodeResource(getResources(),R.drawable.money));

        List<BeanRecordType> outlist = new ArrayList<BeanRecordType>();
        int i = 0;
        for(i = 0;i<str.length;i++){
            outlist.add(new BeanRecordType(i+1,list.get(i),str[i]));
        }
        List<BeanRecordType> inlist = new ArrayList<BeanRecordType>();
        for(int j = 0;i<str2.length;i++){
            inlist.add(new BeanRecordType(i+j+1,list2.get(j),str2[j]));
        }

        BeanRecordType.setList(outlist,inlist);
    }
}
