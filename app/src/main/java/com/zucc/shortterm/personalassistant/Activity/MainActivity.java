package com.zucc.shortterm.personalassistant.Activity;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.app.Dialog;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.zucc.shortterm.personalassistant.Bean.BeanTodo;
import com.zucc.shortterm.personalassistant.DB.MyDatebaseManager;

import com.zucc.shortterm.personalassistant.Bean.BeanRecord;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordGroup;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordType;
import com.zucc.shortterm.personalassistant.Tools.AutoReceiver;
import com.zucc.shortterm.personalassistant.R;
import com.zucc.shortterm.personalassistant.Tools.RecordGroupAdapter;
import com.zucc.shortterm.personalassistant.Tools.TodoItemAdapter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.util.List;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BottomNavigationBar.OnTabSelectedListener{
        private BottomNavigationBar bottomNavigationBar;
    private int nowLayout = 1;
    //include View
    private View content,record;
    private final String CHANNEL_ID="001";
    private final String CHANNEL_NAME="chanel";
    private final String CHANNEL_DESCRIPTION="no";
    //recordList
    private ListView recordList;
    private RecordGroupAdapter recordGroupAdapter;
    private TodoItemAdapter adapter;

    //todoList
    private ArrayList<BeanTodo> beanTodoList=new ArrayList<>();
    //tod o
    //当前点击的位置
    private int pos;
    private  AlertDialog.Builder dialogdelete;
    private Dialog dialogInfo,dialogPri,dialogCalendar;
    private View inflateInfo,inflatePri,inflateCalendar,inflateLogin;
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
    private BeanTodo.Content currentTodo = new BeanTodo.Content(1,"",null,0,new Date(),0,0,0);
    private BeanRecord currentRecord = new BeanRecord();

    //数据库实例
    private MyDatebaseManager dbmanager;

    private Switch aSwitch;
    private SharedPreferences sharedPreferences;
    List<BeanTodo.Content> notHaveDone =new ArrayList<>();
    List<BeanTodo.Content> haveDone =new ArrayList<>();

    //发送通知
    private List<Date> messages=new ArrayList<>();

    //长按获得的todoitem
    private BeanTodo.Content todoitem;

    //是否登陆

    private Boolean isLogin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        dbmanager = new MyDatebaseManager(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getInt("night",0)==0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        isLogin = sharedPreferences.getBoolean("isLogin",false);

        Log.d("wowowowwo",isLogin.toString());
        dbmanager = new MyDatebaseManager(this);
        inflateLogin = LayoutInflater.from(this).inflate(R.layout.nav_header_main,null);

        //登陆
        ImageView imagelogin=inflateLogin.findViewById(R.id.imagelogin);
        TextView textlogin=inflateLogin.findViewById(R.id.textlogin);

        if (isLogin){
            System.out.println("loginnnnnnnnnnnnn");
            imagelogin.setImageResource(R.drawable.icon_head_2);
            textlogin.setVisibility(View.GONE);
        }

        //发送通知
        createChannel();


        dialogdelete = new AlertDialog.Builder(this);

        //todolist
        RecyclerView recyclerView = findViewById(R.id.recyclelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initTodoListItem();
        adapter=new TodoItemAdapter(beanTodoList);
        recyclerView.setAdapter(adapter);

        adapter.setOnitemClickLintener(new TodoItemAdapter.OnitemClick() {
            @Override
            public void onItemClick(BeanTodo.Content item,int position) {
                Log.d("dsfsdf",String.valueOf(position));

                pos=position;
                Intent intent =new Intent(MainActivity.this,TodoDetialActivity.class);
                intent.putExtra("item_info",item);
                Log.d("abababa", String.valueOf(item.getRemind()));
                startActivityForResult(intent, 1);
            }
        });


        //长按
        adapter.setOnLongClickListener(new TodoItemAdapter.OnLongClick() {
            @Override
            public void onLongClick(BeanTodo.Content item) {
                todoitem=item;
                Log.d("得到item", todoitem.getName());
                dialogdelete.show();
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
        if(isLogin){
            initRecord();
            initRecordType();
        }
        recordList = (ListView)findViewById(R.id.recordList);
        recordGroupAdapter = new RecordGroupAdapter(this,groupList);
        recordList.setAdapter(recordGroupAdapter);

        TextView in = findViewById(R.id.income);
        TextView out = findViewById(R.id.cost);
        int sum = 0;
        int cost = 0;
        for(int i = 0;i<groupList.size();i++){
            sum += groupList.get(i).getInSum();
            cost += groupList.get(i).getOutSum();
        }
        in.setText("总支出："+cost);
        out.setText("总收入："+sum);

        //删除待办
        initdialogdelete();
    }

    //发送通知
    private void sendMessage(Date date){
        Intent intent = new Intent(this, AutoReceiver.class);
        intent.setAction("VIDEO_TIMER");
        // PendingIntent这个类用于处理即将发生的事情 
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        // AlarmManager.ELAPSED_REALTIME_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟使用相对时间
        // SystemClock.elapsedRealtime()表示手机开始到现在经过的时间
//        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime(), 10 * 1000, sender);
        am.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
    }
    //创建
    private void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private void initdialogdelete(){
        dialogdelete
//                .setTitle("删除")
                .setMessage("确定要删除该待办吗")
//                .setIcon(R.mipmap.ic_launcher)
                .create();
        dialogdelete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            // what if we click the alert;
            public void onClick(DialogInterface dialog, int which) {

                beanTodoList.remove(todoitem);
                adapter.notifyDataSetChanged();
                Log.d("删除",todoitem.getName());

            }
        });
// Add a NegativeButton;
        dialogdelete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
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

        final EditText recordsum = inflateRecord.findViewById(R.id.record_sum);
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
        final LinearLayout container = inflateRecord.findViewById(R.id.record_container);

        final LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams imagelayoutParams = new LinearLayout.LayoutParams(100, 100);
        imagelayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        imagelayoutParams.setMargins(5,2,15,5);
        final LinearLayout.LayoutParams textlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textlayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        textlayoutParams.setMargins(0,0,15,5);

        final Button in = inflateRecord.findViewById(R.id.record_in);
        final Button out = inflateRecord.findViewById(R.id.record_out);
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRecord.setIn(1);
                in.setTextColor(getResources().getColor(R.color.colorTheme));
                container.removeAllViews();
                out.setTextColor(getResources().getColor(R.color.gray));
                for(int i = 0;i<BeanRecordType.inlist.size();i++){
                    final LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(layoutParams);
                    final ImageView imageView = new ImageView(getApplicationContext());
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
                    final LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(layoutParams);
                    final ImageView imageView = new ImageView(getApplicationContext());
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

        final TextView recordMemo = inflateRecord.findViewById(R.id.record_memo);
        final Dialog memo = new Dialog(this,R.style.ActionSheetDialogStyle);
        View memoview = LayoutInflater.from(this).inflate(R.layout.dialog_record_memo,null);
        memo.setContentView(memoview);
        Window window1 = memo.getWindow();
        window1.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp1 = window1.getAttributes();
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window1.setAttributes(lp1);
        final EditText memoTxet = memoview.findViewById(R.id.record_memo_text);
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

        final TextView recordDate = inflateRecord.findViewById(R.id.record_date);
        final Dialog calendar = new Dialog(this,R.style.ActionSheetDialogStyle);
        View dateview = LayoutInflater.from(this).inflate(R.layout.dialog_record_date,null);
        calendar.setContentView(dateview);
        Window window2 = calendar.getWindow();
        window2.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp2 = window2.getAttributes();
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        window2.setAttributes(lp2);
        ImageButton dateback = dateview.findViewById(R.id.record_date_back);
        final MaterialCalendarView mc = dateview.findViewById(R.id.record_date_date);
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
                BeanRecord beanRecord = new BeanRecord(currentRecord.getId(),currentRecord.getSum(),currentRecord.getType(),currentRecord.getIn(),currentRecord.getDate(),currentRecord.getMemo());
                int i = 0;
                for(i = 0;i<groupList.size();i++){
                    if(formatDate(groupList.get(i).getDate()).equals(formatDate(currentRecord.getDate()))){
                        groupList.get(i).getList().add(beanRecord);
                        break;
                    }
                }
                if(i >= groupList.size()){
                    BeanRecordGroup beanRecordGroup = new BeanRecordGroup();
                    beanRecordGroup.setDate(currentRecord.getDate());
                    ArrayList<BeanRecord> list = new ArrayList<>();
                    beanRecordGroup.setList(list);
                    beanRecordGroup.addRecords(beanRecord);
                    groupList.add(beanRecordGroup);
                }
                recordGroupAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    public void initCalendar(){
        dialogCalendar = new Dialog(this,R.style.ActionSheetDialogStyle);
        inflateCalendar = LayoutInflater.from(this).inflate(R.layout.dialog_calendar,null);
        final MaterialCalendarView todoCalendar= inflateCalendar.findViewById(R.id.todoCalendar);
        Button cancel = inflateCalendar.findViewById(R.id.cancel_calendar);
        Button sure = inflateCalendar.findViewById(R.id.sure_calendar);
        //选择时间
        LinearLayout time = inflateCalendar.findViewById(R.id.time);
        final TextView timeText = inflateCalendar.findViewById(R.id.time_text);
        final ImageView timeImage = inflateCalendar.findViewById(R.id.time_image);
        final Dialog timePickerDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        final View timeFlater = LayoutInflater.from(this).inflate(R.layout.dialog_time,null);
        timePickerDialog.setContentView(timeFlater);
        Window tWindow = timePickerDialog.getWindow();
        tWindow.setGravity(Gravity.CENTER);
        //WindowManager.LayoutParams tlp = tWindow.getAttributes();
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePicker timePicker = timeFlater.findViewById(R.id.time_picker);
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
        final TextView remindText = inflateCalendar.findViewById(R.id.remind_text);
        final ImageView reminImage = inflateCalendar.findViewById(R.id.remind_image);
        final Dialog remindDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        final View remindFlater = LayoutInflater.from(this).inflate(R.layout.dialog_remind,null);
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
        final TextView repeatText = inflateCalendar.findViewById(R.id.repeat_text);
        final ImageView repeatImage = inflateCalendar.findViewById(R.id.repeat_image);
        final Dialog repeatDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        final View repeatFlater = LayoutInflater.from(this).inflate(R.layout.dialog_repeat,null);
        repeatDialog.setContentView(repeatFlater);
        Window repeatWindow = repeatDialog.getWindow();
        repeatWindow.setGravity(Gravity.CENTER);
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = repeatFlater.findViewById(R.id.repeat_box);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = repeatFlater.findViewById(checkedId);
                        System.out.println(radioButton.getText());
                        currentTodo.setRepeat(Integer.parseInt(String.valueOf(radioButton.getTag())));
                        repeatText.setText(radioButton.getText());
                        int color = getResources().getColor(R.color.colorTheme);
                        repeatText.setTextColor(color);
                        repeatImage.setColorFilter(color);
                    }
                });
                repeatDialog.show();
            }
        });

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
                    String date=year+"-"+month+"-"+day+" "+timeText.getText();
                    Log.d("fdsfds", date);
                    Date d=formatStrSecond(date);
                    currentTodo.setDate(d);
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

    public void repeat() {
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < beanTodoList.size(); i++) {
            BeanTodo.Content item = (BeanTodo.Content) beanTodoList.get(i);
            if (item.getRepeat() == 1) {

            } else if (item.getRepeat() == 2) {
                if (!((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))) {
                    item.setDate(new Date());
                    beanTodoList.add(1,item);
                    adapter.notifyDataSetChanged();
                }

            } else if (item.getRepeat() == 3) {
                if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                    item.setDate(new Date());
                    beanTodoList.add(1,item);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    //时间转换工具
    public Date formatStrSecond(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try{
            Date date = sdf.parse(str);

            return date;
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
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
    public String formatDateSecond(Date date){
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        return sdf.format(date);
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
                TextView in = findViewById(R.id.income);
                TextView out = findViewById(R.id.cost);
                int sum = 0;
                int cost = 0;
                for(int i = 0;i<groupList.size();i++){
                    sum += groupList.get(i).getInSum();
                    cost += groupList.get(i).getOutSum();
                }
                in.setText("总支出："+cost);
                out.setText("总收入："+sum);
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
            currentTodo.setName(nav_dialog_edit.getText().toString());

            Log.d("record",String.valueOf(currentTodo.getPRI()));
            Log.d("record",currentTodo.getDate().toString());
            Log.d("record",String.valueOf(currentTodo.getRemind()));
            dbmanager.addTodo(currentTodo);
            Log.d("record","添加成功");
            BeanTodo.Content a=new BeanTodo.Content();
            a.setName(currentTodo.getName());
            a.setDate(currentTodo.getDate());
            a.setDetail(currentTodo.getDetail());
            a.setRemind(currentTodo.getRemind());
            a.setPRI(currentTodo.getPRI());
            a.setHaveDown(currentTodo.getHaveDown());
            a.setRepeat(currentTodo.getRepeat());

            beanTodoList.add(1,a);
            adapter.notifyDataSetChanged();
            Log.d("aaaaaa",currentTodo.getDate().toString());

            if (currentTodo.getRemind()!=0) {
                Date date = new Date();
                switch (currentTodo.getRemind()) {
                    case 1:
                        date = getSpecifiedDayBefore(currentTodo.getDate(), 0);
                    case 2:
                        date = getSpecifiedDayBefore(currentTodo.getDate(), 1);
                    case 3:
                        date = getSpecifiedDayBefore(currentTodo.getDate(), 2);
                    case 4:
                        date = getSpecifiedDayBefore(currentTodo.getDate(), 3);
                    case 5:
                        date = getSpecifiedDayBefore(currentTodo.getDate(), 7);
                }
                sendMessage(date);
                Log.d("aaaaaa", date.toString());
            }
        }else{
            Log.d("a", "error");
        }

    }


    private void initTodoListItem(){

            List<BeanTodo.Content>list=new ArrayList<>();
            if (isLogin)
                list= dbmanager.getTodoList();
            BeanTodo haveNotTitle=new BeanTodo.Title("未完成");
            BeanTodo haveDoneTitle=new BeanTodo.Title("已完成");

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
        aSwitch = findViewById(R.id.nav_night);
        aSwitch.setChecked(sharedPreferences.getInt("night",0)==1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    System.out.println("点击了？");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("night",1);
                    editor.commit();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("night",0);
                    editor.commit();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chart) {
            Intent intent = new Intent(MainActivity.this,CountActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_product) {
            Intent intent = new Intent(MainActivity.this,FinancingActivity.class);
            startActivity(intent);
        }  else if (id == R.id.nav_share) {

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
        List<Date> dates = dbmanager.getRecordDates();
        for(int i = 0;i<dates.size();i++){
            BeanRecordGroup beanRecordGroup = new BeanRecordGroup();
            beanRecordGroup.setDate(dates.get(i));
            ArrayList<BeanRecord> records = dbmanager.getRecordsByDate(dates.get(i));
            if(records.size() == 0)
                continue;
            beanRecordGroup.setList(records);
            groupList.add(beanRecordGroup);
        }
    }
    //获得提前几天的日期
    public static Date getSpecifiedDayBefore(Date specifiedDay,int d){
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-d);
        return c.getTime();
    }
}
