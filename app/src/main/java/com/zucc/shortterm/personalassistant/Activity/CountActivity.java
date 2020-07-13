package com.zucc.shortterm.personalassistant.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordGroup;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordType;
import com.zucc.shortterm.personalassistant.DB.MyDatebaseManager;
import com.zucc.shortterm.personalassistant.R;
import com.zucc.shortterm.personalassistant.Tools.CountItemAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CountActivity extends AppCompatActivity{

    private ImageButton imageButton;
    private MyDatebaseManager db;
    private List<PieEntry> outlist;//图表
    private ArrayList<Integer> outColors;
    private List<PieEntry> inlist;
    private ArrayList<Integer> inColors;
    private PieChart pieChart;
    private List<BeanRecordType> outl;//list数据
    private List<BeanRecordType> inl;
    private double out = 0,in = 0;

    private ListView listView;
    private CountItemAdapter countItemAdapter;

    private PieData pieDataout ;
    private PieData pieDatain ;
    private PieDataSet pieDataSetout;
    private PieDataSet pieDataSetin;
    private Button switchChart;
    private Button monthpickButton;
    private int isOut = 1;

    private int mYear,mMonth;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        db = new MyDatebaseManager(this);

        imageButton = findViewById(R.id.count_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        monthpickButton = findViewById(R.id.count_month);
        monthpickButton.setText((new Date().getYear()+1900)+"年"+(new Date().getMonth()+1)+"月");
        final Dialog monthDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        View monthView = LayoutInflater.from(this).inflate(R.layout.dialog_monthpicker,null);
        monthDialog.setContentView(monthView);
        Window window = monthDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        final NumberPicker yearpick = monthDialog.findViewById(R.id.pick_year);
        final NumberPicker monthpick = monthDialog.findViewById(R.id.pick_month);
        yearpick.setMaxValue(new Date().getYear()+1900);
        yearpick.setMinValue(1970);
        yearpick.setValue(new Date().getYear()+1900);
        yearpick.setWrapSelectorWheel(false);
        monthpick.setMaxValue(12);
        monthpick.setMinValue(1);
        monthpick.setValue(new Date().getMonth()+1);
        monthpickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthDialog.show();
            }
        });

        initType();
        initTypeGroup(new Date());

        pieChart = findViewById(R.id.pic_chart);
        pieChart.setExtraOffsets(20,10,20,10);
        pieDataSetout = new PieDataSet(outlist,"");
        pieDataSetout.setColors(outColors);
        pieDataSetin = new PieDataSet(inlist,"");
        pieDataSetin.setColors(inColors);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        pieDataout = new PieData(pieDataSetout);
        pieDataout.setDrawValues(true);
        pieDataout.setValueTextSize(0);
        pieDatain = new PieData(pieDataSetin);
        pieDatain.setDrawValues(true);
        pieDatain.setValueTextSize(0);
        pieChart.setData(pieDataout);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.setDrawEntryLabels(false);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
        pieChart.invalidate();//刷新

        listView = findViewById(R.id.count_list);
        countItemAdapter = new CountItemAdapter(this,outlist,outl);
        listView.setAdapter(countItemAdapter);


        switchChart = findViewById(R.id.switch_chart);
        switchChart.setText("总支出\n"+out);
        switchChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOut == 1){
                    pieChart.setData(pieDatain);
                    pieChart.invalidate();
                    isOut = 0;
                    countItemAdapter.setList(inl);
                    countItemAdapter.setPieList(inlist);
                    countItemAdapter.notifyDataSetChanged();
                    switchChart.setText("总收入\n"+in);
                }else{
                    pieChart.setData(pieDataout);
                    pieChart.invalidate();
                    isOut = 1;
                    countItemAdapter.setList(outl);
                    countItemAdapter.setPieList(outlist);
                    countItemAdapter.notifyDataSetChanged();
                    switchChart.setText("总支出\n"+out);
                }
            }
        });

        Button picksure = monthDialog.findViewById(R.id.pick_sure);
        picksure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = yearpick.getValue();
                mMonth = monthpick.getValue();
                String d = mYear+"-";
                if(mMonth<10){
                    d += "0"+mMonth+"-01";
                }else{
                    d += mMonth+"-01";
                }
                initTypeGroup(formatStr(d));

                pieDataSetout.setValues(outlist);
                pieDataSetout.setColors(outColors);
                pieDataSetin.setValues(inlist);
                pieDataSetin.setColors(inColors);
                pieDataout = new PieData(pieDataSetout);
                pieDataout.setDrawValues(true);
                pieDataout.setValueTextSize(0);
                pieChart.setData(pieDataout);
                pieChart.invalidate();
                isOut = 1;
                countItemAdapter.setList(outl);
                countItemAdapter.setPieList(outlist);
                countItemAdapter.notifyDataSetChanged();
                switchChart.setText("总支出\n"+out);
                pieChart.invalidate();
                countItemAdapter.notifyDataSetChanged();
                monthDialog.dismiss();
                monthpickButton.setText(mYear+"年"+mMonth+"月");
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

    public void initType(){
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.type_1));
        colors.add(getResources().getColor(R.color.type_2));
        colors.add(getResources().getColor(R.color.type_3));
        colors.add(getResources().getColor(R.color.type_4));
        colors.add(getResources().getColor(R.color.type_5));
        colors.add(getResources().getColor(R.color.type_6));
        colors.add(getResources().getColor(R.color.type_7));
        colors.add(getResources().getColor(R.color.type_8));
        colors.add(getResources().getColor(R.color.type_9));
        colors.add(getResources().getColor(R.color.type_10));
        colors.add(getResources().getColor(R.color.type_11));
        colors.add(getResources().getColor(R.color.type_12));
        colors.add(getResources().getColor(R.color.type_13));
        colors.add(getResources().getColor(R.color.type_14));
        colors.add(getResources().getColor(R.color.type_15));
        int i = 0;
        for(i = 0;i< BeanRecordType.outlist.size();i++){
            BeanRecordType.outlist.get(i).setColor(colors.get(i));
        }
        for(int j = 0;j<BeanRecordType.inlist.size();j++){
            BeanRecordType.inlist.get(j).setColor(colors.get(i+j));
        }
    }

    public void initTypeGroup(Date date){

        inlist = new ArrayList<>();
        outlist = new ArrayList<>();
        inColors = new ArrayList<>();
        outColors = new ArrayList<>();
        inl = new ArrayList<>();
        outl = new ArrayList<>();
        in = 0;
        out = 0;

        BeanRecordGroup beanRecordGroup = new BeanRecordGroup();
        beanRecordGroup.setList(db.getRecordsByMonth(date));
        HashMap<Integer,Double> map1 = new HashMap<>();
        HashMap<Integer,Double> map2 = new HashMap<>();
        for(int i = 0;i<beanRecordGroup.getList().size();i++){
            System.out.println(beanRecordGroup.getList().get(i).getType()+"hi");
            int id = beanRecordGroup.getList().get(i).getType();
            if(beanRecordGroup.getList().get(i).getIn() == 0){
                if(map1.containsKey(id)){
                    double sum = beanRecordGroup.getList().get(i).getSum()+map1.get(id);
                    map1.put(id,sum);
                }else{
                    map1.put(id,beanRecordGroup.getList().get(i).getSum());
                }
                out += map1.get(id);
            }else{
                if(map2.containsKey(id)){
                    double sum = beanRecordGroup.getList().get(i).getSum()+map2.get(id);
                    map2.put(id,sum);
                }else{
                    map2.put(id,beanRecordGroup.getList().get(i).getSum());
                }
                in += map2.get(id);
            }
        }
        int i;
        for(i = 0;i<BeanRecordType.outlist.size();i++){
            int id = BeanRecordType.outlist.get(i).getId();
            if(map1.containsKey(id)){
                BeanRecordType.outlist.get(i).setSum(map1.get(id));
                double j = BeanRecordType.outlist.get(i).getSum()/out;
                PieEntry pieEntry = new PieEntry((float)j,BeanRecordType.outlist.get(i).getName());
                outlist.add(pieEntry);
                outColors.add(BeanRecordType.outlist.get(i).getColor());
                outl.add(BeanRecordType.outlist.get(i));
            }
        }
        for(i =0;i<BeanRecordType.inlist.size();i++){
            int id = BeanRecordType.inlist.get(i).getId();
            if(map2.containsKey(id)){
                BeanRecordType.inlist.get(i).setSum(map2.get(id));
                double j = BeanRecordType.inlist.get(i).getSum()/in;
                PieEntry pieEntry =new PieEntry((float)j,BeanRecordType.inlist.get(i).getName());
                inlist.add(pieEntry);
                inColors.add(BeanRecordType.inlist.get(i).getColor());
                inl.add(BeanRecordType.inlist.get(i));
            }
        }
    }


}
