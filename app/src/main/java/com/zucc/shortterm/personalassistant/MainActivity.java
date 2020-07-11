package com.zucc.shortterm.personalassistant;

import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.zucc.shortterm.personalassistant.Bean.BeanTodo;
import com.zucc.shortterm.personalassistant.DB.MyDatebaseManager;

import com.zucc.shortterm.personalassistant.Bean.BeanRecord;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordGroup;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordType;
import com.zucc.shortterm.personalassistant.Tools.RecordGroupAdapter;
import com.zucc.shortterm.personalassistant.Tools.RecordItemAdapter;
import com.zucc.shortterm.personalassistant.Tools.TodoItemAdapter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.Inflater;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BottomNavigationBar.OnTabSelectedListener{
        private BottomNavigationBar bottomNavigationBar;
    private int nowLayout = 1;
    //include View
    private View content,record;
    //recordList
    private ListView recordList;
    private RecordGroupAdapter recordGroupAdapter;

    //todoList
    private List<BeanTodo> beanTodoList=new ArrayList<>();
    //tod o
    private Dialog dialogInfo,dialogPri,dialogCalendar;
    private View inflateInfo,inflatePri,inflateCalendar;
    private EditText nav_dialog_edit;
    private Button nav_dialog_button;
    private MyDatebaseManager myDatebaseManager;
    private TextView textRankH,textRankN,textRankM,textRankL;
    private ImageView nav_dialog_remind,nav_dialog_pri;

    //record
    private Dialog dialogRecordInfo;
    private View inflateRecord;
    //初始化列表
    private ArrayList<BeanRecordGroup> groupList = new ArrayList<>();
    //当前实例
    private BeanTodo currentTodo = new BeanTodo();
    private BeanRecord currentRecord = new BeanRecord();

    //数据库实例
    private MyDatebaseManager dbmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        dbmanager = new MyDatebaseManager(this);

        //登陆
        ImageView imagelogin=findViewById(R.id.imagelogin);
        TextView textlogin=findViewById(R.id.textlogin);


        RecyclerView recyclerView = findViewById(R.id.recyclelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initTodoListItem();
        TodoItemAdapter adapter=new TodoItemAdapter(beanTodoList);
        recyclerView.setAdapter(adapter);

        adapter.setOnitemClickLintener(new TodoItemAdapter.OnitemClick() {
            @Override
            public void onItemClick(BeanTodo.Content item) {
                Log.d("dsfsdf",item.getName());
                Intent intent =new Intent(MainActivity.this,TodoDetialActivity.class);
                intent.putExtra("item_info",item);
                startActivity(intent);
            }
        });

        adapter.setOnLongClickListener(new TodoItemAdapter.OnLongClick() {
            @Override
            public void onLongClick(BeanTodo.Content item) {
            }
        });

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowLayout == 0)
                    showContentDialog();
                else if(nowLayout == 1)
                    initDailogRecord();
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
        initCalendar();
        //record数据
        initRecord();
        initRecordType();
        recordList = (ListView)findViewById(R.id.recordList);
        recordGroupAdapter = new RecordGroupAdapter(this,groupList);
        recordList.setAdapter(recordGroupAdapter);
    }

    //登陆跳转
    public void toLogin(View view){
        Intent intent=new Intent();
        intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        Log.d("aaaaa","afadfasfasdf");
    }

   public void intiBottomNavigationBar(){
       bottomNavigationBar= findViewById(R.id.nav_bottom);
       bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
       bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
       bottomNavigationBar.setBarBackgroundColor(R.color.white);
       bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.icon_todo_1, "备忘录").setActiveColorResource(R.color.colorTheme))
               .addItem(new BottomNavigationItem(R.drawable.icon_record_1, "记账").setActiveColorResource(R.color.colorTheme))
               .addItem(new BottomNavigationItem(R.drawable.icon_setting_1, "设置").setActiveColorResource(R.color.colorTheme))
               .setFirstSelectedPosition(1)
               .initialise(); //所有的设置需在调用该方法前完成
       nowLayout = 1;
    }

    public void initDailogRecord(){
        dialogRecordInfo = new Dialog(this,R.style.ActionSheetDialogStyle);
        inflateRecord = LayoutInflater.from(this).inflate(R.layout.dialog_record_info,null);
        dialogRecordInfo.setContentView(inflateRecord);
        dialogRecordInfo.show();
        Window window = dialogRecordInfo.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        EditText recordsum = inflateRecord.findViewById(R.id.record_sum);
        recordsum.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        recordsum.setFocusable(true);
        recordsum.setFocusableInTouchMode(true);
        recordsum.requestFocus();
        recordsum.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) recordsum.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(recordsum,InputMethodManager.SHOW_IMPLICIT);
            }
        },100);

        HorizontalScrollView horizontalScrollView = inflateRecord.findViewById(R.id.record_type);
        LinearLayout container = inflateRecord.findViewById(R.id.record_container);

        LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams imagelayoutParams = new LinearLayout.LayoutParams(100, 100);
        imagelayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        imagelayoutParams.setMargins(5,2,15,5);
        LinearLayout.LayoutParams textlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textlayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        textlayoutParams.setMargins(0,0,15,5);

        Button in = inflateRecord.findViewById(R.id.record_in);
        Button out = inflateRecord.findViewById(R.id.record_out);
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecord.setIn(1);
                in.setTextColor(getResources().getColor(R.color.colorTheme));
                container.removeAllViews();
                out.setTextColor(getResources().getColor(R.color.gray));
                for(int i = 0;i<BeanRecordType.inlist.size();i++){
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(layoutParams);
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageBitmap(BeanRecordType.inlist.get(i).getIcon());;
                    imageView.setLayoutParams(imagelayoutParams);
                    imageView.setTag(BeanRecordType.inlist.get(i).getId());
                    if(i == 0){
                        imageView.setColorFilter(getResources().getColor(R.color.colorTheme));
                        currentRecord.setType(Integer.parseInt(String.valueOf(imageView.getTag())));
                    }

                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(BeanRecordType.inlist.get(i).getName());
                    textView.setTextColor(getResources().getColor(R.color.gray));
                    textView.setTextSize(10);
                    textView.setLayoutParams(textlayoutParams);

                    linearLayout.addView(imageView);
                    linearLayout.addView(textView);
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentRecord.setType(Integer.parseInt(String.valueOf(imageView.getTag())));
                            for(int i = 0;i<container.getChildCount();i++) {
                                if (container.getChildAt(i) == linearLayout) {
                                    imageView.setColorFilter(Color.parseColor("#9AACEC"), PorterDuff.Mode.MULTIPLY);
                                } else {
                                    LinearLayout li = (LinearLayout) container.getChildAt(i);
                                    ImageView im = (ImageView) li.getChildAt(0);
                                    im.clearColorFilter();
                                }
                            }
                        }
                    });
                    container.addView(linearLayout);
                    container.invalidate();
                }
            }
        });
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecord.setIn(0);
                out.setTextColor(getResources().getColor(R.color.app_red));
                container.removeAllViews();
                in.setTextColor(getResources().getColor(R.color.gray));
                for(int i = 0;i<BeanRecordType.outlist.size();i++){
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(layoutParams);
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageBitmap(BeanRecordType.outlist.get(i).getIcon());;
                    imageView.setLayoutParams(imagelayoutParams);
                    imageView.setTag(BeanRecordType.outlist.get(i).getId());
                    if(i == 0){
                        imageView.setColorFilter(getResources().getColor(R.color.app_red));
                        currentRecord.setType(Integer.parseInt(String.valueOf(imageView.getTag())));
                    }

                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(BeanRecordType.outlist.get(i).getName());
                    textView.setTextColor(getResources().getColor(R.color.gray));
                    textView.setTextSize(10);
                    textView.setLayoutParams(textlayoutParams);

                    linearLayout.addView(imageView);
                    linearLayout.addView(textView);
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentRecord.setType(Integer.parseInt(String.valueOf(imageView.getTag())));
                            for(int i = 0;i<container.getChildCount();i++) {
                                if (container.getChildAt(i) == linearLayout) {
                                    imageView.setColorFilter(Color.parseColor("#e75e58"), PorterDuff.Mode.MULTIPLY);
                                } else {
                                    LinearLayout li = (LinearLayout) container.getChildAt(i);
                                    ImageView im = (ImageView) li.getChildAt(0);
                                    im.clearColorFilter();
                                }
                            }
                        }
                    });
                    container.addView(linearLayout);
                    container.invalidate();
                }
            }
        });
        out.performClick();

        TextView recordMemo = inflateRecord.findViewById(R.id.record_memo);
        Dialog memo = new Dialog(this,R.style.ActionSheetDialogStyle);
        View memoview = LayoutInflater.from(this).inflate(R.layout.dialog_record_memo,null);
        memo.setContentView(memoview);
        Window window1 = memo.getWindow();
        window1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = window1.getAttributes();
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);
        EditText memoTxet = memoview.findViewById(R.id.record_memo_text);
        memoTxet.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) memoTxet.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(memoTxet,InputMethodManager.SHOW_IMPLICIT);
            }
        },100);
        memoTxet.setFocusable(true);
        memoTxet.requestFocus();
        ImageButton memoBack=  memoview.findViewById(R.id.record_memo_back);
        memoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memo.dismiss();
                dialogRecordInfo.show();
                recordsum.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) recordsum.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(recordsum,InputMethodManager.SHOW_IMPLICIT);
                    }
                },100);
            }
        });
        Button  memoSure = memoview.findViewById(R.id.record_memo_sure);
        memoSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordMemo.setText(memoTxet.getText());
                System.out.println(memoTxet.getText()+"zhelizheli");
                recordMemo.setTextColor(getResources().getColor(R.color.gray));
                if(memoTxet.getText().toString().length()== 0){
                    recordMemo.setText("添加备注");
                    recordMemo.setTextColor(getResources().getColor(R.color.colorTheme));
                }
                memo.dismiss();
                dialogRecordInfo.show();
                recordsum.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) recordsum.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(recordsum,InputMethodManager.SHOW_IMPLICIT);
                    }
                },100);
            }
        });
        recordMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memo.show();
                memoTxet.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) memoTxet.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(memoTxet,InputMethodManager.SHOW_IMPLICIT);
                    }
                },100);
                dialogRecordInfo.dismiss();

            }
        });

        TextView recordDate = inflateRecord.findViewById(R.id.record_date);
        Dialog calendar = new Dialog(this,R.style.ActionSheetDialogStyle);
        View dateview = LayoutInflater.from(this).inflate(R.layout.dialog_record_date,null);
        calendar.setContentView(dateview);
        Window window2 = calendar.getWindow();
        window2.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp2 = window2.getAttributes();
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        window2.setAttributes(lp2);
        ImageButton dateback = dateview.findViewById(R.id.record_date_back);
        MaterialCalendarView mc = dateview.findViewById(R.id.record_date_date);
        mc.setSelectedDate(new Date());
        dateback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = mc.getSelectedDate().getYear();
                int month = mc.getSelectedDate().getMonth()+1;
                int day = mc.getSelectedDate().getDay();
                currentRecord.setDate(formatStr(year+"-"+month+"-"+day));
                recordDate.setText(month+"月"+day+"日");
                recordsum.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) recordsum.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(recordsum,InputMethodManager.SHOW_IMPLICIT);
                    }
                },100);
                calendar.dismiss();
            }
        });
        Date now = new Date();
        recordDate.setText((now.getMonth()+1)+"月"+now.getDate()+"日");
        recordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.show();
            }
        });

        recordsum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                currentRecord.setSum(Integer.parseInt(recordsum.getText().toString()));
                currentRecord.setMemo(recordMemo.getText().toString());
                if(currentRecord.getDate() == null) currentRecord.setDate(new Date());
                System.out.println(currentRecord.getDate()+currentRecord.getMemo()+currentRecord.getSum()+":"+currentRecord.getType()+currentRecord.getIn());
                dialogRecordInfo.dismiss();
                dbmanager.addRecord(currentRecord.getSum(),currentRecord.getType(),currentRecord.getDate(),currentRecord.getMemo(),currentRecord.getIn());
                int i = 0;
                for(i = 0;i<groupList.size();i++){
                    if(formatDate(groupList.get(i).getDate()).equals(formatDate(currentRecord.getDate()))){
                        groupList.get(i).getList().add(currentRecord);
                        break;
                    }
                }
                if(i >= groupList.size()){
                    BeanRecordGroup beanRecordGroup = new BeanRecordGroup();
                    beanRecordGroup.setDate(currentRecord.getDate());
                    ArrayList<BeanRecord> list = new ArrayList<>();
                    list.add(currentRecord);
                    beanRecordGroup.setList(list);
                }
                recordGroupAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    public void initCalendar(){
        dialogCalendar = new Dialog(this,R.style.ActionSheetDialogStyle);
        inflateCalendar = LayoutInflater.from(this).inflate(R.layout.dialog_calendar,null);
        MaterialCalendarView todoCalendar= inflateCalendar.findViewById(R.id.todoCalendar);
        Button cancel = inflateCalendar.findViewById(R.id.cancel_calendar);
        Button sure = inflateCalendar.findViewById(R.id.sure_calendar);
        //选择时间
        LinearLayout time = inflateCalendar.findViewById(R.id.time);
        TextView timeText = inflateCalendar.findViewById(R.id.time_text);
        ImageView timeImage = inflateCalendar.findViewById(R.id.time_image);
        Dialog timePickerDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        View timeFlater = LayoutInflater.from(this).inflate(R.layout.dialog_time,null);
        timePickerDialog.setContentView(timeFlater);
        Window tWindow = timePickerDialog.getWindow();
        tWindow.setGravity(Gravity.CENTER);
        //WindowManager.LayoutParams tlp = tWindow.getAttributes();
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker timePicker = timeFlater.findViewById(R.id.time_picker);
                timePicker.setCurrentHour(new Date().getHours());
                timePicker.setCurrentMinute(new Date().getMinutes());
                Button timeSure = timeFlater.findViewById(R.id.sure_time);
                timeSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hour = timePicker.getCurrentHour() >= 10 ? String.valueOf(timePicker.getCurrentHour()):"0"+String.valueOf(timePicker.getCurrentHour());
                        String min = timePicker.getCurrentMinute() >= 10 ?String.valueOf(timePicker.getCurrentMinute()):"0"+String.valueOf(timePicker.getCurrentMinute());
                        String str = hour+":"+min;
                        timeText.setText(str);
                        int color = getResources().getColor(R.color.colorTheme);
                        timeText.setTextColor(color);
                        timeImage.setColorFilter(color);
                        timePickerDialog.dismiss();
                    }
                });
                Button timeCancel = timeFlater.findViewById(R.id.cancel_time);
                timeCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePickerDialog.dismiss();
                    }
                });
                timePickerDialog.show();
            }
        });
        // 选择提醒
        LinearLayout remind = inflateCalendar.findViewById(R.id.remind);
        TextView remindText = inflateCalendar.findViewById(R.id.remind_text);
        ImageView reminImage = inflateCalendar.findViewById(R.id.remind_image);
        Dialog remindDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        View remindFlater = LayoutInflater.from(this).inflate(R.layout.dialog_remind,null);
        remindDialog.setContentView(remindFlater);
        Window rWindow = remindDialog.getWindow();
        rWindow.setGravity(Gravity.CENTER);
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = remindFlater.findViewById(R.id.remind_box);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = remindFlater.findViewById(checkedId);
                        System.out.println(radioButton.getText());
                        currentTodo.setRemind(Integer.parseInt(String.valueOf(radioButton.getTag())));
                        remindText.setText(radioButton.getText());
                        int color = getResources().getColor(R.color.colorTheme);
                        remindText.setTextColor(color);
                        reminImage.setColorFilter(color);
                    }
                });
                remindDialog.show();
            }
        });
        //选择重复
        LinearLayout repeat = inflateCalendar.findViewById(R.id.repeat);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCalendar.dismiss();
                dialogInfo.show();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(todoCalendar.getSelectedDate() != null){
                    int year = todoCalendar.getSelectedDate().getYear();
                    int month = todoCalendar.getSelectedDate().getMonth()+1;
                    int day = todoCalendar.getSelectedDate().getDay();
                }

                dialogCalendar.dismiss();
                dialogInfo.show();
            }
        });

        dialogCalendar.setContentView(inflateCalendar);
        Window dialogWindow = dialogCalendar.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;
        lp.x = 0;
        dialogWindow.setAttributes(lp);
        dialogCalendar.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog) {
                dialogInfo.show();
            }
        });

    }
    //时间转换工具
    public Date formatStr(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = sdf.parse(str);

            return date;
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
    }
    public String formatDate(Date date){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        return sdf.format(date);
    }
    public void initPRI(){
        dialogPri = new Dialog(this,R.style.PRIDialogStyle);
        inflatePri = LayoutInflater.from(this).inflate(R.layout.dialog_pri, null);
        textRankH=inflatePri.findViewById(R.id.rankH);
        textRankM=inflatePri.findViewById(R.id.rankM);
        textRankL=inflatePri.findViewById(R.id.rankL);
        textRankN=inflatePri.findViewById(R.id.rankN);
        dialogPri.setContentView(inflatePri);
        //获取当前Activity所在的窗体
        Window dialogWindow2 = dialogPri.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow2.setGravity( Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow2.getAttributes();
        lp.y=495;
        lp.x=-250;
//       将属性设置给窗体
        dialogWindow2.setAttributes(lp);
    }

    public void onClickRankH(View view){
        nav_dialog_edit.setTextColor(0xffe21e29);
        currentTodo.setPRI(1);
        nav_dialog_pri.setColorFilter(0xffe21e29);
    }
    public void onClickRankM(View view){
        nav_dialog_edit.setTextColor(0xffe5b728);
        currentTodo.setPRI(2);
        nav_dialog_pri.setColorFilter(0xffe5b728);
    }
    public void onClickRankL(View view){
        nav_dialog_edit.setTextColor(0xff667ed6);
        currentTodo.setPRI(3);
        nav_dialog_pri.setColorFilter(0xff667ed6);
    }
    public void onClickRankN(View view){
        nav_dialog_edit.setTextColor(0xff8a8a8a);
        currentTodo.setPRI(4);
        nav_dialog_pri.setColorFilter(0xff8a8a8a);
    }
    @Override
    public void onTabSelected(int position) {
        switch (position){
            case 0:
                content.setVisibility(View.VISIBLE);
                record.setVisibility(View.GONE);
                nowLayout = 0;
                break;
            case 1:
                content.setVisibility(View.GONE);
                record.setVisibility(View.VISIBLE);
                nowLayout = 1;
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
        dialogInfo = new Dialog(this,R.style.ActionSheetDialogStyle);

        //填充对话框的布局
        inflateInfo = LayoutInflater.from(this).inflate(R.layout.dialog_info, null);
        nav_dialog_edit = (EditText) inflateInfo.findViewById(R.id.nav_dialog_edit);
        nav_dialog_remind = (ImageView) inflateInfo.findViewById(R.id.nav_dialog_remind);
        nav_dialog_pri = (ImageView) inflateInfo.findViewById(R.id.nav_dialog_pri);
        nav_dialog_button = (Button) inflateInfo.findViewById(R.id.nav_dialog_button);
        //将布局设置给Dialog
        dialogInfo.setContentView(inflateInfo);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialogInfo.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialogInfo.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                nav_dialog_edit.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) nav_dialog_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(nav_dialog_edit,InputMethodManager.SHOW_IMPLICIT);
                    }
                },100);
            }
        });
        dialogInfo.show();//显示对话框

        nav_dialog_edit.setFocusable(true);
        nav_dialog_edit.requestFocus();
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

                dialogPri.show();
            }
        });
        nav_dialog_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("calendar", "aaa");
//                System.out.println("fdfdf");

                dialogInfo.dismiss();
                dialogCalendar.show();
            }
        });
    }

    //点击确定添加记录
    public void onClickSure(View v){
        BeanTodo t=new BeanTodo();
        if (!nav_dialog_edit.getText().toString().isEmpty())
        {
            myDatebaseManager.addTodo(currentTodo);
            Log.d("record","添加成功");
        }
        else{
            Log.d("a", "error");
        }

    }


    private void initTodoListItem() {

        BeanTodo.Content item = new BeanTodo.Content(1,"好好学习1","ds",0,new Date(System.currentTimeMillis()),1,1,"fdfdfd");
        BeanTodo.Content item2 = new BeanTodo.Content(2,"好好学习2","fdf",1,new Date(System.currentTimeMillis()),1,1,"fdfdfd");
        BeanTodo.Content item3 = new BeanTodo.Content(3,"好好学习3","fdfdfdfd",1,new Date(System.currentTimeMillis()),1,1,"fdfdfd");

        List<BeanTodo>list=new ArrayList<>();
        list.add(item3);
        list.add(item);
        list.add(item2);
        BeanTodo haveNotTitle=new BeanTodo.Title("未完成");
        BeanTodo haveDoneTitle=new BeanTodo.Title("已完成");
        List<BeanTodo.Content> notHaveDone =new ArrayList<>();
        List<BeanTodo.Content> haveDone =new ArrayList<>();

        for(int i=0;i<list.size();i++){
            BeanTodo.Content a=(BeanTodo.Content)list.get(i);
            if (a.getHaveDown()==0)
                notHaveDone.add((BeanTodo.Content)list.get(i));
            else
                haveDone.add((BeanTodo.Content)list.get(i));
        }

        if (notHaveDone.size()!=0){
            beanTodoList.add(haveNotTitle);
            beanTodoList.addAll(notHaveDone);
        }

        if (haveDone.size()!=0){
            beanTodoList.add(haveDoneTitle);
            beanTodoList.addAll(haveDone);
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

    public void initRecordType(){
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
            BeanRecordType beanRecordType = new BeanRecordType(i+1,list.get(i),str[i]);
            outlist.add(beanRecordType);
        }
        List<BeanRecordType> inlist = new ArrayList<BeanRecordType>();
        for(int j= 0;j<str2.length;j++){
            inlist.add(new BeanRecordType(i+j+1,list2.get(j),str2[j]));
        }

        BeanRecordType.setList(outlist,inlist);
    }
    public void initRecord(){
        List<Date> dates = dbmanager.getGroupDate();
        for(int i = 0;i<dates.size();i++){
            BeanRecordGroup beanRecordGroup = new BeanRecordGroup();
            beanRecordGroup.setDate(dates.get(i));
            ArrayList<BeanRecord> records = dbmanager.getRecordsByDate(dates.get(i));
            if(records.size() == 0)
                continue;
            for(int j = 0;j<records.size();j++){
                System.out.println(records.get(j).getType()+"这里是type！");
            }
            beanRecordGroup.setList(records);
            groupList.add(beanRecordGroup);
        }
    }
}
